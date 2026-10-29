package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.MemoryEntity
import com.example.data.local.entity.QuestionEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
  @Query("SELECT * FROM questions ORDER BY createdAt DESC")
  fun getAllQuestions(): Flow<List<QuestionEntity>>

  @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
  suspend fun getQuestionById(id: String): QuestionEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuestion(question: QuestionEntity)
}

@Dao
interface MemoryDao {
  @Query("SELECT * FROM memories ORDER BY savedAt DESC")
  fun getAllMemories(): Flow<List<MemoryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMemory(memory: MemoryEntity)

  @Query("DELETE FROM memories WHERE id = :id")
  suspend fun deleteMemory(id: String)
}

@Dao
interface UserDao {
  @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
  fun getUserById(id: String): Flow<UserEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user: UserEntity)
}
