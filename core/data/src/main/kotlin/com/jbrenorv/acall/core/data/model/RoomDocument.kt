package com.jbrenorv.acall.core.data.model

import com.google.firebase.firestore.DocumentSnapshot
import com.jbrenorv.acall.core.model.LanguageLevel
import com.jbrenorv.acall.core.model.Room
import com.jbrenorv.acall.core.model.User

internal data class RoomDocument(
    val id: String = "",
    val topic: String = "",
    val languageLevel: LanguageLevel = LanguageLevel.ANY,
    val language: String = "",
    val users: List<String> = emptyList()
) {
    companion object
}

internal fun RoomDocument.Companion.create(
    id: String,
    userId: String,
    roomData: RoomData
) = RoomDocument(
    id = id,
    topic = roomData.topic,
    languageLevel = roomData.languageLevel,
    language = roomData.language,
    users = listOf(userId)
)

internal fun RoomDocument.toRoom(users: List<User>) = Room(
    id = id,
    topic = topic,
    languageLevel = languageLevel,
    language = language,
    users = users
)

internal fun DocumentSnapshot.toRoomDocument() =
    requireObject(RoomDocument::class.java)
