package com.jbrenorv.acall.core.data.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.jbrenorv.acall.core.data.repository.FakeRoomRepository
import com.jbrenorv.acall.core.data.repository.RoomRepository
import com.jbrenorv.acall.core.data.repository.auth.AuthRepository
import com.jbrenorv.acall.core.data.repository.auth.FirebaseAuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsRoomRepository(
        roomsRepository: FakeRoomRepository,
    ): RoomRepository

    @Binds
    @Singleton
    internal abstract fun bindsAuthRepository(
        authRepository: FirebaseAuthRepository,
    ): AuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth =
            Firebase.auth.apply {
                useEmulator("192.168.1.9", 9099)
            }
    }
}
