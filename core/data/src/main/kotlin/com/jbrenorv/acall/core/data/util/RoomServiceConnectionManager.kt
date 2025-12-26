package com.jbrenorv.acall.core.data.util

import com.jbrenorv.acall.core.model.room.Room
import com.jbrenorv.acall.core.system.service.RoomServiceBinder

interface RoomServiceConnectionManager {
    fun getServiceBinder(): RoomServiceBinder?

    fun setListener(listener: Listener?)

    fun addRoom(room: Room)

    fun maybeDisconnectService()

    interface Listener {
        fun onServiceDisconnected()

        fun onServiceConnected(serviceBinder: RoomServiceBinder)

        fun onServiceAddRoomSuccess(room: Room)

        fun onServiceAddRoomError(room: Room, exception: Exception? = null)
    }
}
