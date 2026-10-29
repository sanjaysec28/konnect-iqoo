package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.MemoryDao
import com.example.data.local.dao.QuestionDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.MemoryEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.entity.UserEntity

@Database(
  entities = [QuestionEntity::class, MemoryEntity::class, UserEntity::class],
  version = 1,
  exportSchema = false
)
abstract class KonnectDatabase : RoomDatabase() {
  abstract fun questionDao(): QuestionDao
  abstract fun memoryDao(): MemoryDao
  abstract fun userDao(): UserDao

  companion object {
    @Volatile
    private var INSTANCE: KonnectDatabase? = null

    fun getDatabase(context: Context): KonnectDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          KonnectDatabase::class.java,
          "konnect_database"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
