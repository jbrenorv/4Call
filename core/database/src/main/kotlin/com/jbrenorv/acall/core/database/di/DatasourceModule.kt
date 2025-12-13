package com.jbrenorv.acall.core.database.di

import com.jbrenorv.acall.core.database.datasource.user.LocalUserDatasource
import com.jbrenorv.acall.core.database.datasource.user.RoomLocalUserDatasource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DatasourceModule {
    @Binds
    internal abstract fun bindsRemoteRoomDatasource(
        impl: RoomLocalUserDatasource,
    ): LocalUserDatasource
}
