package com.jbrenorv.acall.core.data.repository

import com.jbrenorv.acall.core.model.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun getRooms(/* add query */): Flow<List<Room>>
}
