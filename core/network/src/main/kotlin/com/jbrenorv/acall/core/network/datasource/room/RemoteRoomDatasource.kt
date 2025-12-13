package com.jbrenorv.acall.core.network.datasource.room

import com.jbrenorv.acall.core.model.room.RoomData
import com.jbrenorv.acall.core.network.document.RoomDocument
import kotlinx.coroutines.flow.Flow

interface RemoteRoomDatasource {
    fun getRooms(/* add query */): Flow<List<RoomDocument>>

    suspend fun createRoom(host: String, roomData: RoomData): Result<RoomDocument>
}
