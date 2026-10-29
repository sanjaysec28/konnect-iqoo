package com.example.data.repository

import com.example.data.mock.CommunitySeedData
import com.example.data.model.MemoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * MemoryRepository
 * Architectural interface for the REMEMBER phase of KONNECT:
 * Storing, searching, and recalling verified architectural insights and solved discussions.
 */
interface MemoryRepository {
  fun observeMemories(): Flow<List<MemoryItem>>
  suspend fun getMemoryById(id: String): MemoryItem?
  suspend fun saveMemory(item: MemoryItem): Result<MemoryItem>
  suspend fun deleteMemory(id: String): Result<Unit>
  suspend fun searchMemories(query: String): List<MemoryItem>
}

class DefaultMemoryRepository : MemoryRepository {
  private val _memories = MutableStateFlow<List<MemoryItem>>(CommunitySeedData.memories)

  override fun observeMemories(): Flow<List<MemoryItem>> = _memories.asStateFlow()

  override suspend fun getMemoryById(id: String): MemoryItem? {
    return _memories.value.firstOrNull { it.id == id || it.questionId == id }
  }

  override suspend fun saveMemory(item: MemoryItem): Result<MemoryItem> {
    // Avoid duplicate memory entry if same question
    val filtered = _memories.value.filterNot { it.questionId == item.questionId && it.id == item.id }
    _memories.value = listOf(item) + filtered
    return Result.success(item)
  }

  override suspend fun deleteMemory(id: String): Result<Unit> {
    _memories.value = _memories.value.filterNot { it.id == id }
    return Result.success(Unit)
  }

  override suspend fun searchMemories(query: String): List<MemoryItem> {
    if (query.isBlank()) return _memories.value
    val clean = query.trim().lowercase()
    return _memories.value.filter { item ->
      item.title.lowercase().contains(clean) ||
          item.summary.lowercase().contains(clean) ||
          item.problem.lowercase().contains(clean) ||
          item.solution.lowercase().contains(clean) ||
          item.solverName.lowercase().contains(clean) ||
          item.technologies.any { it.lowercase().contains(clean) } ||
          item.tags.any { it.lowercase().contains(clean) }
    }
  }
}
