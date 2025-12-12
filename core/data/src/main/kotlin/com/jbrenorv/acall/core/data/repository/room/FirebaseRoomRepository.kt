package com.jbrenorv.acall.core.data.repository.room

import com.google.firebase.firestore.FirebaseFirestore
import com.jbrenorv.acall.core.data.exception.NoAuthUserException
import com.jbrenorv.acall.core.data.model.RoomData
import com.jbrenorv.acall.core.data.model.RoomDocument
import com.jbrenorv.acall.core.data.model.create
import com.jbrenorv.acall.core.data.model.toRoom
import com.jbrenorv.acall.core.data.model.toRoomDocument
import com.jbrenorv.acall.core.data.repository.auth.AuthRepository
import com.jbrenorv.acall.core.data.repository.user.UserRepository
import com.jbrenorv.acall.core.model.Room
import com.jbrenorv.acall.core.model.toUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRoomRepository @Inject constructor(
    firebaseFirestore: FirebaseFirestore,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : RoomRepository {
    private val roomCollection = firebaseFirestore
        .collection(ROOM_COLLECTION_NAME)

    override fun getRooms(): Flow<List<Room>> =
        callbackFlow {
            val registration = roomCollection.addSnapshotListener { querySnapshot, err ->
                if (err != null || querySnapshot == null) {
                    return@addSnapshotListener
                }
                trySend(querySnapshot)
            }

            awaitClose {
                registration.remove()
            }
        }.mapLatest { querySnapshot ->
            querySnapshot.documents.mapNotNull { documentSnapshot ->
                val roomDocument = documentSnapshot.toRoomDocument()
                val getUsersResult = userRepository.getUsers(roomDocument.users)
                getUsersResult.getOrNull()?.let { users ->
                    roomDocument.toRoom(users)
                }
            }
        }

    // TODO: move this logic to a Firebase Cloud Function
    override suspend fun createRoom(roomData: RoomData): Result<Room> {
        return runCatching {
            val authUser = authRepository.getCurrentAuthUser()
                ?: throw NoAuthUserException()

            val roomDocumentReference = roomCollection.document()
            val roomDocument = RoomDocument.create(
                id = roomDocumentReference.id,
                userId = authUser.id,
                roomData = roomData
            )

            roomDocumentReference
                .set(roomDocument)
                .await()

            val users = listOf(authUser.toUser())
            val room = roomDocument.toRoom(users)

            return Result.success(room)
        }
    }

    companion object {
        private const val ROOM_COLLECTION_NAME = "rooms"
    }
}
