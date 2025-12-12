package com.jbrenorv.acall.core.data.repository.room

import com.jbrenorv.acall.core.data.model.RoomData
import com.jbrenorv.acall.core.model.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun getRooms(/* add query */): Flow<List<Room>>

    suspend fun createRoom(roomData: RoomData): Result<Room>
}
