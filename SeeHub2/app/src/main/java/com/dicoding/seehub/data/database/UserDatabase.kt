package com.dicoding.seehub.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, UserFollowingEntity::class, UserFollowersEntity::class, UserFavoriteEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null
        fun getDatabaseInstance(context: Context) : UserDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user.db"
                ).build().also { instance = it }
            }
        }
    }
}