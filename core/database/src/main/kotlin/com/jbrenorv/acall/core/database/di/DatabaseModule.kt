package com.jbrenorv.acall.core.database.di

import android.content.Context
import androidx.room.Room
import com.jbrenorv.acall.core.database.ACallDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesNiaDatabase(
        @ApplicationContext context: Context,
    ): ACallDatabase = Room.databaseBuilder(
        context,
        ACallDatabase::class.java,
        "acall-database",
    ).build()
}
