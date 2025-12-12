package com.jbrenorv.acall.core.data.repository.user

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.jbrenorv.acall.core.data.exception.NoAuthUserException
import com.jbrenorv.acall.core.data.exception.UserNotFoundException
import com.jbrenorv.acall.core.data.model.UserDocument
import com.jbrenorv.acall.core.data.model.create
import com.jbrenorv.acall.core.data.model.toUser
import com.jbrenorv.acall.core.data.model.toUserDocument
import com.jbrenorv.acall.core.data.repository.auth.AuthRepository
import com.jbrenorv.acall.core.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    firebaseFirestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : UserRepository {
    private val userCollection = firebaseFirestore
        .collection(USER_COLLECTION_NAME)

    // TODO: save user in a local database and sync later
    override suspend fun createUser(): Result<User> {
        return runCatching {
            val authUser = authRepository.getCurrentAuthUser()
                ?: throw NoAuthUserException()

            val userDocument = UserDocument.create(authUser)
            userCollection
                .document(userDocument.id)
                .set(userDocument)
                .await()

            return Result.success(userDocument.toUser())
        }
    }

    // TODO: try local database first
    override suspend fun getUser(id: String): Result<User> {
        return runCatching {
            val documentSnapshot = userCollection.document(id).get().await()

            if (!documentSnapshot.exists()) {
                throw UserNotFoundException(id)
            }

            val user = documentSnapshot
                .toUserDocument()
                .toUser()

            return Result.success(user)
        }
    }

    // TODO: try local database first
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
        private const val TAG = "FirebaseRoomRepository"
    }
}
