package com.jbrenorv.acall.core.model

data class Room(
    val id: String,
    val topic: String,
    val languageLevel: LanguageLevel,
    val language: String,
    val users: List<User>
)
