package com.jbrenorv.acall.core.data.model

import com.jbrenorv.acall.core.model.LanguageLevel

data class RoomData(
    val topic: String,
    val languageLevel: LanguageLevel,
    val language: String
)
