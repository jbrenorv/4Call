package com.jbrenorv.acall.core.model.room

import com.jbrenorv.acall.core.model.user.User
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant

@OptIn(ExperimentalTime::class)
data class Room(
    val id: String,
    val topic: String,
    val languageLevel: LanguageLevel,
    val language: String,
    val host: User,
    val guests: List<User>,
    val createdAt: Instant
)
