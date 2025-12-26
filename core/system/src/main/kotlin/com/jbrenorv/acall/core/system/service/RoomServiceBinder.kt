package com.jbrenorv.acall.core.system.service

import androidx.core.telecom.CallEndpointCompat
import com.jbrenorv.acall.core.system.model.RoomCommand
import com.jbrenorv.acall.core.system.model.TelecomRoomData
import kotlinx.coroutines.flow.Flow

interface RoomServiceBinder {
    interface Connector {
        fun getService(): RoomServiceBinder
    }

    val rooms: Flow<List<TelecomRoomData>>

    val externalCommand: Flow<RoomCommand>

    fun activate(roomId: String)

    fun close(roomId: String)

    fun setEndpoint(roomId: String, endpoint: CallEndpointCompat)
}
