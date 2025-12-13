package com.jbrenorv.acall.core.network.datasource.user

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.jbrenorv.acall.core.common.exception.UserNotFoundException
import com.jbrenorv.acall.core.model.user.User
import com.jbrenorv.acall.core.network.document.UserDocument
import com.jbrenorv.acall.core.network.document.create
import com.jbrenorv.acall.core.network.document.toUser
import com.jbrenorv.acall.core.network.document.toUserDocument
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirebaseRemoteUserDatasource @Inject constructor(
    firebaseFirestore: FirebaseFirestore
): RemoteUserDatasource{
    private val userCollection = firebaseFirestore
        .collection(USER_COLLECTION_NAME)

    override suspend fun saveUser(user: User): Result<Unit> {
        return runCatching {
            val userDocument = UserDocument.create(user)

            userCollection
                .document(userDocument.id)
                .set(userDocument)
                .await()
        }
    }

    override suspend fun getUser(userId: String): Result<User> {
        return runCatching {
            val documentSnapshot = userCollection.document(userId).get().await()

            if (!documentSnapshot.exists()) {
                throw UserNotFoundException(userId)
            }

            val user = documentSnapshot
                .toUserDocument()
                .toUser()

            return Result.success(user)
        }
    }

    override suspend fun getUsers(ids: List<String>): Result<List<User>> {
        return runCatching {
            val querySnapshot = userCollection
                .whereIn(FieldPath.documentId(), ids)
                .get()
                .await()

            val foundIds = querySnapshot.documents.map { it.id }
            val missingIds = ids - foundIds

            if (missingIds.isNotEmpty()) {
                throw UserNotFoundException(missingIds.first())
            }

            val users = querySnapshot
                .documents
                .map { documentSnapshot ->
                    documentSnapshot
                        .toUserDocument()
                        .toUser()
                }

            return Result.success(users)
        }
    }

    companion object {
        private const val USER_COLLECTION_NAME = "users"
    }
}
