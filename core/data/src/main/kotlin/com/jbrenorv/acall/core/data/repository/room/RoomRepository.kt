package com.jbrenorv.acall.core.data.repository.room

import com.jbrenorv.acall.core.model.room.Room
import com.jbrenorv.acall.core.model.room.RoomData
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun getRooms(/* add query */): Flow<List<Room>>

    suspend fun createRoom(roomData: RoomData): Result<Room>
}
