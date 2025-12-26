package com.jbrenorv.acall.core.data.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.jbrenorv.acall.core.data.repository.auth.AuthRepository
import com.jbrenorv.acall.core.model.room.Room
import com.jbrenorv.acall.core.system.service.RoomService
import com.jbrenorv.acall.core.system.service.RoomServiceBinder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import javax.inject.Inject

class DefaultRoomServiceConnectionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) : RoomServiceConnectionManager {
    private var isBound = false
    private var serviceBinder: RoomServiceBinder? = null
    private val isServiceConnected get() = isBound && serviceBinder != null
    private var boundContext: Context? = null
    private var externalCommandsJob: Job? = null
    private var pendingRoom: Room? = null
    private var listener: RoomServiceConnectionManager.Listener? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            try {
                serviceBinder = (service as RoomServiceBinder.Connector).getService()
                isBound = true
                listener?.onServiceConnected(serviceBinder!!)
            } catch (_: Exception) {
                isBound = false
            } finally {
                // Process anyway
                processPendingRoom()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {

            cleanServiceConnectionState()
            listener?.onServiceDisconnected()
        }
    }

    override fun getServiceBinder() = serviceBinder

    override fun setListener(listener: RoomServiceConnectionManager.Listener?) {
        this.listener = listener
    }

    override fun addRoom(room: Room) {
        enqueuePendingRoom(room)
        val intent = startService(room)
        if (isServiceConnected) {
            processPendingRoom()
        } else {
            connectService(intent)
        }
    }

    override fun maybeDisconnectService() {
        if (isServiceConnected) {
            try {
                boundContext?.unbindService(serviceConnection)
            } catch (_: Exception) {
                // NO-OP
            }
            cleanServiceConnectionState()
        }
    }

    private fun startService(room: Room): Intent {
        val applicationContext = context.applicationContext
        val startServiceParams = RoomService.StartServiceParams(
            isMine = room.isMine(),
            roomId = room.id,
            languageLevel = room.languageLevel.name,
            language = room.language
        )
        return RoomService.start(applicationContext, startServiceParams)
    }

    private fun connectService(intent: Intent) {
        if (isServiceConnected) {
            processPendingRoom()
            return
        }
        val applicationContext = context.applicationContext
        var didBind = false
        try {
            didBind = applicationContext.bindService(
                intent,
                serviceConnection,
                Context.BIND_AUTO_CREATE or Context.BIND_IMPORTANT
            )
            if (didBind) {
                Log.e(TAG, "Service successfully bound")
                boundContext = applicationContext
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Unexpected error starting/binding service: $exception")
        } finally {
            if (!didBind) {
                try {
                    applicationContext.stopService(intent)
                } catch (stopServiceException: Exception) {
                    Log.e(TAG, "Unexpected error stoping service: $stopServiceException")
                }
                // If the bind fails, onServiceConnected() will never be called.
                // Process any pending call now to avoid leaving it stuck indefinitely.
                processPendingRoom()
            }
        }
    }

    private fun processPendingRoom() {
        dequeuePendingRoom()?.let { room ->
            val service = serviceBinder
            if (service == null) {
                listener?.onServiceAddRoomError(room)
            } else {
                listener?.onServiceAddRoomSuccess(room)
            }
        }
    }

    private fun cleanServiceConnectionState() {
        isBound = false
        serviceBinder = null
        boundContext = null
        externalCommandsJob?.cancel()
        externalCommandsJob = null
    }

    private fun enqueuePendingRoom(room: Room) {
        pendingRoom = room
    }

    private fun dequeuePendingRoom(): Room? {
        val room = pendingRoom
        pendingRoom = null
        return room
    }

    private fun Room.isMine(): Boolean =
        authRepository.getCurrentUser()?.id == host.id

    companion object {
        private const val TAG = "RoomServiceConnectionManager"
    }
}
