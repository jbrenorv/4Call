package com.jbrenorv.acall.core.data.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.jbrenorv.acall.core.data.repository.auth.AuthRepository
import com.jbrenorv.acall.core.data.repository.auth.FirebaseAuthRepository
import com.jbrenorv.acall.core.data.repository.room.OnlineOnlyRoomRepository
import com.jbrenorv.acall.core.data.repository.room.RoomRepository
import com.jbrenorv.acall.core.data.repository.user.OfflineFirstUserRepository
import com.jbrenorv.acall.core.data.repository.user.UserRepository
import com.jbrenorv.acall.core.data.util.DefaultRoomServiceConnectionManager
import com.jbrenorv.acall.core.data.util.RoomServiceConnectionManager
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
    @Singleton
    internal abstract fun bindsAuthRepository(
        impl: FirebaseAuthRepository,
    ): AuthRepository

    @Binds
    internal abstract fun bindsUserRepository(
        impl: OfflineFirstUserRepository,
    ): UserRepository

    @Binds
    @Singleton
    internal abstract fun bindsRoomServiceConnectionManager(
        impl: DefaultRoomServiceConnectionManager,
    ): RoomServiceConnectionManager

    @Binds
    internal abstract fun bindsRoomRepository(
        impl: OnlineOnlyRoomRepository,
    ): RoomRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth =
            Firebase.auth.apply {
                useEmulator("192.168.1.9", 9099)
            }
    }
}
