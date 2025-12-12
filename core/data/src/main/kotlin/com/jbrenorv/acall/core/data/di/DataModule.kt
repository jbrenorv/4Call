package com.jbrenorv.acall.core.data.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.jbrenorv.acall.core.data.repository.auth.AuthRepository
import com.jbrenorv.acall.core.data.repository.auth.FirebaseAuthRepository
import com.jbrenorv.acall.core.data.repository.room.FirebaseRoomRepository
import com.jbrenorv.acall.core.data.repository.room.RoomRepository
import com.jbrenorv.acall.core.data.repository.user.FirebaseUserRepository
import com.jbrenorv.acall.core.data.repository.user.UserRepository
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
        impl: FirebaseUserRepository,
    ): UserRepository

    @Binds
    internal abstract fun bindsRoomRepository(
        impl: FirebaseRoomRepository,
    ): RoomRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth =
            Firebase.auth.apply {
                useEmulator("192.168.1.9", 9099)
            }

        @Provides
        @Singleton
        fun provideFirebaseFirestore(): FirebaseFirestore =
            Firebase.firestore.apply {
                useEmulator("192.168.1.9", 8080)
            }
    }
}
