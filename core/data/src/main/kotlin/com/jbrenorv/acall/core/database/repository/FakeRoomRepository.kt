package com.jbrenorv.acall.core.database.repository

import com.jbrenorv.acall.core.model.LanguageLevel
import com.jbrenorv.acall.core.model.Room
import com.jbrenorv.acall.core.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeRoomRepository @Inject constructor() : RoomRepository {
    override fun getRooms(): Flow<List<Room>> = flowOf(
        List(10) { index ->
            Room(
                id = index.toString(),
                topic = listOf("Studies", "Language leaning", "Relationships").random(),
                languageLevel = LanguageLevel.entries.random(),
                language = "English",
                users = listOf(
                    listOf(
                        User(
                            id = (2 * index).toString(),
                            name = "Person ${2 * index}",
                            photoUrl = null
                        ),
                        User(
                            id = (2 * index + 1).toString(),
                            name = "Person ${2 * index + 1}",
                            photoUrl = null
                        )
                    ),
                    listOf(
                        User(
                            id = (2 * index).toString(),
                            name = "Person ${2 * index}",
                            photoUrl = null
                        ),
                    )
                ).random()
            )
        }
    )
}
