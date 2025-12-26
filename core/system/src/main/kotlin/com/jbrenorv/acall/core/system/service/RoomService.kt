package com.jbrenorv.acall.core.system.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Parcelable
import android.telecom.DisconnectCause
import androidx.core.content.ContextCompat
import androidx.core.telecom.CallAttributesCompat
import androidx.core.telecom.CallAttributesCompat.Companion.CALL_TYPE_AUDIO_CALL
import androidx.core.telecom.CallAttributesCompat.Companion.DIRECTION_INCOMING
import androidx.core.telecom.CallAttributesCompat.Companion.DIRECTION_OUTGOING
import androidx.core.telecom.CallEndpointCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.jbrenorv.acall.core.system.model.RoomCommand
import com.jbrenorv.acall.core.system.model.TelecomRoomData
import com.jbrenorv.acall.core.system.model.TelecomRoomState
import com.jbrenorv.acall.core.system.notification.RoomNotificationManager
import com.jbrenorv.acall.core.system.telecom.RoomTelecomController
import com.jbrenorv.acall.core.system.util.parcelableExtra
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

/**
 * This service is responsible for handle foreground service, notifications and telecom integration
 */
@AndroidEntryPoint
class RoomService : RoomServiceBinder, RoomTelecomController.Listener, LifecycleService() {
    @Inject
    lateinit var notificationManager: RoomNotificationManager

    @Inject
    lateinit var controller: RoomTelecomController

    override val rooms: Flow<List<TelecomRoomData>>
        get() = controller.telecomRooms

    override val externalCommand: Flow<RoomCommand>
        get() = controller.externalCommand

    private val localBinder = object : RoomServiceBinder.Connector, Binder() {
        override fun getService(): RoomServiceBinder {
            return this@RoomService
        }
    }

    override fun onCreate() {
        super.onCreate()
        controller.start()
        notificationManager.createChannelIfNeeded()
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.stop()
        notificationManager.cancelAllNotification()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return localBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        super.onUnbind(intent)
        return false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_START_SERVICE -> {
                val params = intent.parcelableExtra<StartServiceParams>(EXTRA_START_SERVICE_PARAMS)
                params?.let { startService(it) }
            }

            ACTION_CLOSE_ROOM -> {
                val roomId = intent.getStringExtra(EXTRA_ROOM_ID)
                roomId?.let { close(it) }
            }
        }
        return START_STICKY
    }

    fun startService(params: StartServiceParams) {
        val callData = createInitialRoomData(params)
        startForegroundWithNotification(callData)
        addRoom(callData)
    }

    override fun activate(roomId: String) {
        controller.handleCommand(RoomCommand.Activate(roomId))
    }

    override fun close(roomId: String) {
        val cause = DisconnectCause(DisconnectCause.LOCAL)
        controller.handleCommand(RoomCommand.Disconnect(roomId, cause))
    }

    override fun setEndpoint(roomId: String, endpoint: CallEndpointCompat) {
        controller.handleCommand(RoomCommand.SetEndpoint(roomId, endpoint))
    }

    override fun onRoomDataChanged(
        previousData: TelecomRoomData,
        currentData: TelecomRoomData
    ) {
        notificationManager.showOrUpdateCallNotification(
            notificationId = getNotificationId(currentData.roomId),
            roomData = currentData
        )
    }

    override fun onRoomBecameActive(telecomRoomData: TelecomRoomData) {
//        getPreferredStartingCallEndpoint(callData.isVideoCall)?.let { endpoint ->
//            switchCallEndpoint(callData.callId, endpoint)
//        }
    }

    override fun onRoomDisconnected(roomId: String) {
        notificationManager.cancelNotification(getNotificationId(roomId))
        checkAndStopForegroundIfNeeded()
    }

    private fun startForegroundWithNotification(telecomRoomData: TelecomRoomData) {
        val roomId = telecomRoomData.roomId
        val notificationId = getNotificationId(roomId)
        val notification = if (telecomRoomData.isMine()) {
            notificationManager.buildHostRoomNotification(telecomRoomData)
        } else {
            notificationManager.buildGuestRoomNotification(telecomRoomData)
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    notificationId,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL
                )
            } else {
                startForeground(notificationId, notification)
            }
            notificationManager.postNotification(notificationId, roomId, notification)
        } catch (_: Exception) {
            stopSelf()
        }
    }

    private fun addRoom(roomData: TelecomRoomData) {
        val roomId = roomData.roomId
        val coroutineContext =
            lifecycleScope.coroutineContext + SupervisorJob() + CoroutineName("Room-$roomId")
        controller.addRoom(roomData, coroutineContext)
    }

    private fun checkAndStopForegroundIfNeeded() {
        val currentCalls = controller.telecomRooms.value
        val shouldStopForeground = currentCalls.all { it.state == TelecomRoomState.DISCONNECTED }
        if (shouldStopForeground) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun getNotificationId(roomId: String): Int {
        return roomId.hashCode()
    }

    private fun createInitialRoomData(params: StartServiceParams): TelecomRoomData {
        val attributes = createCallAttributes(params)
        val initialState =
            if (params.isMine) TelecomRoomState.DIALING else TelecomRoomState.RINGING
        return TelecomRoomData(
            roomId = params.roomId,
            attributes = attributes,
            state = initialState,
            callException = null,
            currentEndpoint = null,
            availableEndpoints = emptyList(),
            disconnectCause = null,
            isAudioMuted = false
        )
    }

    private fun createCallAttributes(params: StartServiceParams): CallAttributesCompat {
        return CallAttributesCompat(
            displayName = "${params.language} - ${params.languageLevel}",
            address = Uri.fromParts("webrtc", params.roomId, null),
            direction = if (params.isMine) DIRECTION_OUTGOING else DIRECTION_INCOMING,
            callType = CALL_TYPE_AUDIO_CALL,
            callCapabilities = CallAttributesCompat.SUPPORTS_STREAM,
        )
    }

    @Parcelize
    data class StartServiceParams(
        val isMine: Boolean,
        val roomId: String,
        val languageLevel: String,
        val language: String
    ) : Parcelable

    companion object {
        private const val EXTRA_START_SERVICE_PARAMS = "acall.extra.EXTRA_START_SERVICE_PARAMS"
        private const val EXTRA_ROOM_ID = "acall.extra.EXTRA_ROOM_ID"
        private const val ACTION_START_SERVICE = "acall.action.START_SERVICE"
        private const val ACTION_CLOSE_ROOM = "acall.action.CLOSE_ROOM"

        fun getClosePendingIntent(context: Context, roomId: String): PendingIntent {
            val intent = Intent(context, RoomService::class.java).apply {
                putExtra(EXTRA_ROOM_ID, roomId)
                action = ACTION_CLOSE_ROOM
            }
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            return PendingIntent.getService(context, 0, intent, flags)
        }

        fun start(context: Context, params: StartServiceParams): Intent {
            val intent = Intent(context, RoomService::class.java).apply {
                putExtra(EXTRA_START_SERVICE_PARAMS, params)
                action = ACTION_START_SERVICE
            }
            ContextCompat.startForegroundService(context, intent)
            return intent
        }
    }
}
