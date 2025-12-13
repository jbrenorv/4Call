package com.jbrenorv.acall.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jbrenorv.acall.core.database.dao.UserDao
import com.jbrenorv.acall.core.database.entity.UserEntity
import com.jbrenorv.acall.core.database.util.InstantConverter

@Database(
    entities = [
        UserEntity::class
    ],
    version = 1,
    autoMigrations = [],
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
)
internal abstract class ACallDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
