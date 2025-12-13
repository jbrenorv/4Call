package com.jbrenorv.acall.core.data.repository.room

import com.jbrenorv.acall.core.common.exception.NoAuthUserException
import com.jbrenorv.acall.core.data.repository.auth.AuthRepository
import com.jbrenorv.acall.core.data.repository.user.UserRepository
import com.jbrenorv.acall.core.model.room.Room
import com.jbrenorv.acall.core.model.room.RoomData
import com.jbrenorv.acall.core.network.datasource.room.RemoteRoomDatasource
import com.jbrenorv.acall.core.network.document.toRoom
import com.jbrenorv.acall.core.network.document.toRoomOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class OnlineOnlyRoomRepository @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val remoteRoomDatasource: RemoteRoomDatasource
) : RoomRepository {
    override fun getRooms(): Flow<List<Room>> = remoteRoomDatasource
        .getRooms()
        .mapLatest { roomDocuments ->
            roomDocuments.mapNotNull { roomDocument ->
                val host = userRepository.getUser(roomDocument.host).getOrNull()
                val guests = userRepository.getUsers(roomDocument.guests).getOrNull()
                roomDocument.toRoomOrNull(host, guests)
            }
        }
        .flowOn(Dispatchers.IO)

    override suspend fun createRoom(roomData: RoomData): Result<Room> {
        return runCatching {
            val currentUser = authRepository.getCurrentUser()
                ?: throw NoAuthUserException()

            val result = remoteRoomDatasource.createRoom(
                host = currentUser.id,
                roomData = roomData
            )

            return result.map { roomDocument -> roomDocument.toRoom(currentUser) }
        }
    }
}
