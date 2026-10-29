package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
  @PrimaryKey val id: String,
  val title: String,
  val description: String,
  val authorId: String,
  val authorName: String,
  val tags: String, // comma-separated
  val stage: String,
  val matchedCount: Int,
  val createdAt: Long
)

@Entity(tableName = "memories")
data class MemoryEntity(
  @PrimaryKey val id: String,
  val questionId: String,
  val title: String,
  val summary: String,
  val tags: String,
  val solverName: String,
  val savedAt: Long
)

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey val id: String,
  val name: String,
  val title: String,
  val bio: String,
  val domains: String,
  val reputationScore: Int
)
