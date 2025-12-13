package com.jbrenorv.acall.core.database.di

import com.jbrenorv.acall.core.database.ACallDatabase
import com.jbrenorv.acall.core.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesTopicsDao(
        database: ACallDatabase,
    ): UserDao = database.userDao()
}
