package com.jbrenorv.acall.core.system.telecom

import android.content.Context
import android.telecom.DisconnectCause
import androidx.core.telecom.CallAttributesCompat.Companion.CALL_TYPE_AUDIO_CALL
import androidx.core.telecom.CallControlResult
import androidx.core.telecom.CallControlScope
import androidx.core.telecom.CallEndpointCompat
import androidx.core.telecom.CallException
import androidx.core.telecom.CallsManager
import com.jbrenorv.acall.core.system.model.RoomCommand
import com.jbrenorv.acall.core.system.model.TelecomRoomData
import com.jbrenorv.acall.core.system.model.TelecomRoomState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class RoomTelecomController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private lateinit var callsManager: CallsManager
    private val activeRooms = mutableMapOf<String, TelecomRoomControl>()
    private val _telecomRooms = MutableStateFlow<List<TelecomRoomData>>(emptyList())
    val telecomRooms = _telecomRooms.asStateFlow()
    private val _externalCommand = MutableSharedFlow<RoomCommand>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val externalCommand = _externalCommand.asSharedFlow()
    private var listener: Listener? = null

    fun start() {
        callsManager = CallsManager(context)
        callsManager.registerAppWithTelecom(CallsManager.Companion.CAPABILITY_BASELINE)
    }

    fun stop() {
        disconnectAndRemoveAllRooms()
    }

    fun addRoom(roomData: TelecomRoomData, coroutineContext: CoroutineContext) {
        // Ensure no other active room
        disconnectAndRemoveAllRooms()

        // Add room
        addToState(roomData)
        addToCallsManager(roomData, coroutineContext)
    }

    fun handleCommand(command: RoomCommand) {
        val roomControl = activeRooms[command.roomId] ?: return
        val roomData = getFromState(command.roomId) ?: return
        handleCommand(roomControl, roomData, command)
    }

    private fun handleCommand(
        roomControl: TelecomRoomControl,
        roomData: TelecomRoomData,
        command: RoomCommand
    ) {
        when (command) {
            is RoomCommand.Activate -> {
                if (roomData.isMine()) {
                    roomControl.actionChannel.setActiveChannel.trySend(Unit)
                } else {
                    roomControl.actionChannel.answerChannel.trySend(Unit)
                }
            }

            is RoomCommand.Disconnect -> {
                roomControl.actionChannel.disconnectChannel.trySend(command.cause)
            }

            is RoomCommand.SetEndpoint -> {
                roomControl.actionChannel.switchAudioChannel.trySend(command.endpoint)
            }
        }
    }

    private fun addToCallsManager(
        roomData: TelecomRoomData,
        coroutineContext: CoroutineContext
    ) {
        val roomId = roomData.roomId
        val actionChannel = TelecomRoomActionChannel()
        val roomJob = CoroutineScope(coroutineContext).launch {
            try {
                callsManager.addCall(
                    callAttributes = roomData.attributes,
                    onAnswer = {
                        onCallExternallyActivated(roomId)
                    },
                    onSetActive = {
                        onCallExternallyActivated(roomId)
                    },
                    onSetInactive = {
                        onCallExternallyInactivated(roomId)
                    },
                    onDisconnect = { cause ->
                        onCallExternallyDisconnected(roomId, cause)
                    }
                ) {
                    val roomControlScope: CallControlScope = this
                    launch {
                        initializeAndMonitorCall(roomData)
                    }
                    launch {
                        handleCallActions(roomData, actionChannel, roomControlScope)
                    }
                }
            } catch (_: CancellationException) {
                // Expected cancellation during cleanup
            } catch (exception: Exception) {
                val callException = (exception as? CallException)
                    ?: CallException(CallException.ERROR_UNKNOWN)
                updateState(roomId) {
                    copy(
                        state = TelecomRoomState.UNKNOWN,
                        callException = callException,
                    )
                }
            } finally {
                // Just for safety
                disconnectAndRemoveRoom(roomId, null)
            }
        }
        activeRooms[roomId] = TelecomRoomControl(roomJob, actionChannel)
    }

    private fun CallControlScope.initializeAndMonitorCall(roomData: TelecomRoomData) {
        launch {
            currentCallEndpoint.collect { endpoint ->
                updateState(roomData.roomId) {
                    copy(currentEndpoint = endpoint)
                }
            }
        }

        launch {
            availableEndpoints.collect { endpoints ->
                updateState(roomData.roomId) {
                    copy(availableEndpoints = endpoints)
                }
            }
        }

        launch {
            isMuted.collect { muted ->
                updateState(roomData.roomId) {
                    copy(isAudioMuted = muted)
                }
            }
        }
    }

    private suspend fun handleCallActions(
        roomData: TelecomRoomData,
        actionChannel: TelecomRoomActionChannel,
        callControlScope: CallControlScope
    ) {
        suspend fun executeControlAction(
            action: suspend () -> CallControlResult,
            successState: TelecomRoomState? = null,
        ) {
            handleControlResult(roomData.roomId, action(), successState)
        }

        try {
            while (currentCoroutineContext().isActive) {
                select {
                    actionChannel.answerChannel.onReceive {
                        executeControlAction(
                            { callControlScope.answer(CALL_TYPE_AUDIO_CALL) },
                            TelecomRoomState.ACTIVE
                        )
                    }

                    actionChannel.setActiveChannel.onReceive {
                        executeControlAction(
                            { callControlScope.setActive() },
                            TelecomRoomState.ACTIVE
                        )
                    }

                    actionChannel.disconnectChannel.onReceive { cause ->
                        callControlScope.disconnect(cause)
                        handleRoomDisconnected(roomData.roomId, cause)
                    }

                    actionChannel.switchAudioChannel.onReceive { e ->
                        executeControlAction({ callControlScope.requestEndpointChange(e) })
                    }
                }
            }
        } finally {
            // Action handler loop finished
        }
    }

    private fun handleControlResult(
        roomId: String,
        result: CallControlResult,
        successState: TelecomRoomState? = null,
    ) {
        when (result) {
            is CallControlResult.Success -> {
                if (successState != null) {
                    updateState(roomId) {
                        copy(
                            state = successState,
                            callException = null
                        )
                    }
                } else {
                    // Clear previous errors if any
                    updateState(roomId) {
                        copy(callException = null)
                    }
                }
            }

            is CallControlResult.Error -> {
                updateState(roomId) {
                    copy(callException = CallException(result.errorCode))
                }
            }
        }
    }

    private fun onCallExternallyActivated(roomId: String) {
        _externalCommand.tryEmit(RoomCommand.Activate(roomId))
        updateState(roomId) {
            val updatedData = copy(state = TelecomRoomState.ACTIVE)
            updatedData
        }
    }

    private fun onCallExternallyInactivated(roomId: String) {
        // Inactivate call is unsupported. This method won't be called
        val cause = DisconnectCause(DisconnectCause.LOCAL)
        onCallExternallyDisconnected(roomId, cause)
    }

    private fun onCallExternallyDisconnected(roomId: String, cause: DisconnectCause) {
        _externalCommand.tryEmit(RoomCommand.Disconnect(roomId, cause))
        handleRoomDisconnected(roomId, cause)
    }

    private fun handleRoomDisconnected(roomId: String, cause: DisconnectCause?) {
        updateState(roomId) {
            copy(
                state = TelecomRoomState.DISCONNECTED,
                disconnectCause = cause
            )
        }
        disconnectAndRemoveRoom(roomId, cause)
    }

    private fun disconnectAndRemoveAllRooms() {
        val rooms = activeRooms.keys.toList()
        rooms.forEach { disconnectAndRemoveRoom(it, null) }
    }

    private fun disconnectAndRemoveRoom(roomId: String, disconnectCause: DisconnectCause?) {
        val roomControl = activeRooms.remove(roomId) ?: return
        val cause = disconnectCause ?: DisconnectCause(DisconnectCause.UNKNOWN)
        roomControl.actionChannel.disconnectChannel.trySend(cause)
        roomControl.job.cancel()
        removeFromState(roomId)
        listener?.onRoomDisconnected(roomId)
    }

    private fun addToState(roomData: TelecomRoomData) {
        _telecomRooms.update { currentList ->
            if (currentList.any { it.roomId == roomData.roomId }) currentList
            else currentList + roomData
        }
    }

    private fun getFromState(roomId: String): TelecomRoomData? {
        return _telecomRooms.value.firstOrNull { it.roomId == roomId }
    }

    private fun removeFromState(roomId: String) {
        _telecomRooms.update { current -> current.filterNot { it.roomId == roomId } }
    }

    private fun updateState(
        roomId: String,
        update: TelecomRoomData.() -> TelecomRoomData
    ): TelecomRoomData? {
        var oldData: TelecomRoomData? = null
        var updatedData: TelecomRoomData? = null
        _telecomRooms.update { currentList ->
            currentList.map { roomData ->
                if (roomData.roomId == roomId) {
                    oldData = roomData
                    updatedData = roomData.update()
                    updatedData
                } else {
                    roomData
                }
            }
        }
        if (oldData != null && updatedData != null && oldData != updatedData) {
            onTelecomRoomDataChanged(oldData, updatedData)
        }
        return updatedData
    }

    private fun onTelecomRoomDataChanged(
        previousData: TelecomRoomData,
        currentData: TelecomRoomData
    ) {
        listener?.onRoomDataChanged(previousData, currentData)
        if ((previousData.state in listOf(TelecomRoomState.RINGING, TelecomRoomState.DIALING)) &&
            currentData.state == TelecomRoomState.ACTIVE
        ) {
            listener?.onRoomBecameActive(currentData)
        }
    }

    private data class TelecomRoomActionChannel(
        val answerChannel: Channel<Unit> = Channel(Channel.CONFLATED),
        val setActiveChannel: Channel<Unit> = Channel(Channel.CONFLATED),
        val disconnectChannel: Channel<DisconnectCause> = Channel(Channel.CONFLATED),
        val switchAudioChannel: Channel<CallEndpointCompat> = Channel(Channel.CONFLATED),
    )

    private class TelecomRoomControl(
        val job: Job,
        val actionChannel: TelecomRoomActionChannel,
    )

    interface Listener {
        fun onRoomDataChanged(previousData: TelecomRoomData, currentData: TelecomRoomData)

        fun onRoomBecameActive(telecomRoomData: TelecomRoomData)

        fun onRoomDisconnected(roomId: String)
    }
}
