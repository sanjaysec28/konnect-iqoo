package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.MemoryItem
import com.example.data.repository.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MemoryUiState(
  val memories: List<MemoryItem> = emptyList(),
  val filteredMemories: List<MemoryItem> = emptyList(),
  val searchQuery: String = "",
  val selectedMemory: MemoryItem? = null,
  val isLoading: Boolean = false
)

class MemoryViewModel(
  private val memoryRepository: MemoryRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(MemoryUiState(isLoading = true))
  val uiState: StateFlow<MemoryUiState> = _uiState.asStateFlow()

  init {
    loadMemories()
  }

  private fun loadMemories() {
    viewModelScope.launch {
      memoryRepository.observeMemories().collect { items ->
        _uiState.value = _uiState.value.copy(
          memories = items,
          filteredMemories = if (_uiState.value.searchQuery.isBlank()) items else filterMemories(items, _uiState.value.searchQuery),
          isLoading = false
        )
      }
    }
  }

  fun onSearchQueryChanged(query: String) {
    _uiState.value = _uiState.value.copy(
      searchQuery = query,
      filteredMemories = filterMemories(_uiState.value.memories, query)
    )
  }

  fun selectMemory(memory: MemoryItem) {
    _uiState.value = _uiState.value.copy(selectedMemory = memory)
  }

  fun clearSelectedMemory() {
    _uiState.value = _uiState.value.copy(selectedMemory = null)
  }

  private fun filterMemories(items: List<MemoryItem>, query: String): List<MemoryItem> {
    if (query.isBlank()) return items
    val clean = query.trim().lowercase()
    return items.filter { item ->
      item.title.lowercase().contains(clean) ||
          item.summary.lowercase().contains(clean) ||
          item.problem.lowercase().contains(clean) ||
          item.solution.lowercase().contains(clean) ||
          item.solverName.lowercase().contains(clean) ||
          item.technologies.any { it.lowercase().contains(clean) }
    }
  }
}
