package com.jbrenorv.acall.core.database.di

import com.jbrenorv.acall.core.database.repository.FakeRoomRepository
import com.jbrenorv.acall.core.database.repository.RoomRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsTopicRepository(
        roomsRepository: FakeRoomRepository,
    ): RoomRepository
}
