package com.jbrenorv.acall.core.network.datasource.room

import com.google.firebase.firestore.FirebaseFirestore
import com.jbrenorv.acall.core.model.room.RoomData
import com.jbrenorv.acall.core.network.document.RoomDocument
import com.jbrenorv.acall.core.network.document.create
import com.jbrenorv.acall.core.network.document.toRoomDocument
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirebaseRemoteRoomDatasource @Inject constructor(
    firebaseFirestore: FirebaseFirestore
): RemoteRoomDatasource {
    private val roomCollection = firebaseFirestore
        .collection(ROOM_COLLECTION_NAME)

    override fun getRooms(): Flow<List<RoomDocument>> = callbackFlow {
        val registration = roomCollection.addSnapshotListener { querySnapshot, err ->
            if (err != null || querySnapshot == null) {
                return@addSnapshotListener
            }
            val roomDocuments = querySnapshot.documents.map { it.toRoomDocument() }
            trySend(roomDocuments)
        }

        awaitClose {
            registration.remove()
        }
    }

    override suspend fun createRoom(host: String, roomData: RoomData): Result<RoomDocument> {
        return runCatching {
            val roomDocumentReference = roomCollection.document()
            val roomDocument = RoomDocument.create(
                id = roomDocumentReference.id,
                host = host,
                roomData = roomData
            )

            roomDocumentReference
                .set(roomDocument)
                .await()

            return Result.success(roomDocument)
        }
    }

    companion object {
        private const val ROOM_COLLECTION_NAME = "rooms"
    }
}
