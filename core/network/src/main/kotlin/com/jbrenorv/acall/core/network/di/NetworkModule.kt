package com.jbrenorv.acall.core.network.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.jbrenorv.acall.core.network.datasource.room.FirebaseRemoteRoomDatasource
import com.jbrenorv.acall.core.network.datasource.room.RemoteRoomDatasource
import com.jbrenorv.acall.core.network.datasource.user.FirebaseRemoteUserDatasource
import com.jbrenorv.acall.core.network.datasource.user.RemoteUserDatasource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    @Binds
    internal abstract fun bindsRemoteUserDatasource(
        impl: FirebaseRemoteUserDatasource,
    ): RemoteUserDatasource

    @Binds
    internal abstract fun bindsRemoteRoomDatasource(
        impl: FirebaseRemoteRoomDatasource,
    ): RemoteRoomDatasource

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseFirestore(): FirebaseFirestore =
            Firebase.firestore.apply {
                useEmulator("192.168.1.9", 8080)
            }
    }
}
