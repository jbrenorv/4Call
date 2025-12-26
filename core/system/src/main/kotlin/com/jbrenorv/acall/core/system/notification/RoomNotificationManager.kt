package com.jbrenorv.acall.core.system.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.jbrenorv.acall.core.common.CommonDrawable
import com.jbrenorv.acall.core.common.deeplink.ACallDeepLinks
import com.jbrenorv.acall.core.system.model.TelecomRoomData
import com.jbrenorv.acall.core.system.service.RoomService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = NotificationManagerCompat.from(context)
    private val activeNotifications = mutableMapOf<Int, String>()

    fun createChannelIfNeeded() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Active rooms",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Active rooms notifications"
            setShowBadge(false)
        }

        notificationManager.createNotificationChannel(channel)
    }

    fun buildHostRoomNotification(telecomRoomData: TelecomRoomData): Notification {
        return buildDefaultRoomNotification(
            telecomRoomData = telecomRoomData,
            actionTitle = "Close room"
        )
    }

    fun buildGuestRoomNotification(telecomRoomData: TelecomRoomData): Notification {
        return buildDefaultRoomNotification(
            telecomRoomData = telecomRoomData,
            actionTitle = "Leave room"
        )
    }

    fun postNotification(
        notificationId: Int,
        roomId: String,
        notification: Notification,
        tag: String? = null
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            activeNotifications.put(notificationId, roomId)
            notificationManager.notify(tag, notificationId, notification)
        }
    }

    fun cancelAllNotification() {
        val notifications = activeNotifications.keys.toList()
        notifications.forEach { cancelNotification(it) }
    }

    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
        activeNotifications.remove(notificationId)
    }

    fun showOrUpdateCallNotification(notificationId: Int, roomData: TelecomRoomData) {
//        when (callData.callState) {
//            TelecomCallState.RINGING -> {
//                showIncomingCallNotification(notificationId, callData)
//            }
//
//            TelecomCallState.DIALING -> {
//                showOutgoingCallNotification(notificationId, callData)
//            }
//
//            TelecomCallState.ACTIVE -> {
//                updateActiveCallNotification(notificationId, callData)
//            }
//
//            TelecomCallState.DISCONNECTED -> {
//                updateDisconnectedCallNotification(notificationId, callData)
//            }
//
//            else -> {
//                // NO-OP
//            }
//        }
    }

    private fun buildDefaultRoomNotification(
        telecomRoomData: TelecomRoomData,
        actionTitle: String
    ): Notification {
        val contentIntent = getRoomContentPendingIntent()
        val closeIntent = getRoomActionPendingIntent(telecomRoomData.roomId)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(CommonDrawable.ic_users_group)
            .setContentTitle("Active room")
            .setContentText(telecomRoomData.attributes.displayName)
            .setContentIntent(contentIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .addAction(
                CommonDrawable.ic_close_sm,
                actionTitle,
                closeIntent
            )
            .build()
    }

    private fun getRoomContentPendingIntent(): PendingIntent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            ACallDeepLinks.ROOM.toUri()
        )

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getRoomActionPendingIntent(roomId: String): PendingIntent {
        return RoomService.getClosePendingIntent(context, roomId)
    }

    companion object {
        const val CHANNEL_ID = "room_foreground_channel"
    }
}
