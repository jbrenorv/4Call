package com.jbrenorv.acall.core.network.document

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ServerTimestamp
import com.jbrenorv.acall.core.common.exception.MappingException
import com.jbrenorv.acall.core.model.room.LanguageLevel
import com.jbrenorv.acall.core.model.room.Room
import com.jbrenorv.acall.core.model.room.RoomData
import com.jbrenorv.acall.core.model.user.User
import com.jbrenorv.acall.core.network.exception.DocumentMappingException
import com.jbrenorv.acall.core.network.util.requireObject
import com.jbrenorv.acall.core.network.util.toKotlinInstant
import kotlin.time.ExperimentalTime

data class RoomDocument(
    val id: String = "",
    val topic: String = "",
    val languageLevel: LanguageLevel = LanguageLevel.ANY,
    val language: String = "",
    val host: String = "",
    val guests: List<String> = emptyList(),
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    companion object
}

@OptIn(ExperimentalTime::class)
fun RoomDocument.toRoomOrNull(
    host: User?,
    guests: List<User>?
): Room? {
    if (host == null || guests == null || createdAt == null) {
        return null
    }
    return Room(
        id = id,
        topic = topic,
        languageLevel = languageLevel,
        language = language,
        host = host,
        guests = guests,
        createdAt = createdAt.toKotlinInstant()
    )
}

@OptIn(ExperimentalTime::class)
fun RoomDocument.toRoom(host: User): Room {
    if (createdAt == null) {
        throw MappingException("RoomDocument.createdAt must not be null")
    }
    return Room(
        id = id,
        topic = topic,
        languageLevel = languageLevel,
        language = language,
        host = host,
        guests = emptyList(),
        createdAt = createdAt.toKotlinInstant()
    )
}

internal fun RoomDocument.Companion.create(
    id: String,
    host: String,
    roomData: RoomData
) = RoomDocument(
    id = id,
    topic = roomData.topic,
    languageLevel = roomData.languageLevel,
    language = roomData.language,
    host = host,
    guests = emptyList()
)

internal fun DocumentSnapshot.toRoomDocument() =
    requireObject(RoomDocument::class.java)
