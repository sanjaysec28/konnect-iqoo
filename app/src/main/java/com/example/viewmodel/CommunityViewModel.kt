package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.BuilderProfile
import com.example.data.model.Question
import com.example.data.repository.CommunityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class CommunityUiState(
  val questions: List<Question> = emptyList(),
  val filteredQuestions: List<Question> = emptyList(),
  val builders: List<BuilderProfile> = emptyList(),
  val selectedCategory: String = "All",
  val availableCategories: List<String> = listOf("All", "Android", "AI", "Web", "Backend", "Data", "DevTools", "Other"),
  val searchQuery: String = "",
  val isLoading: Boolean = false
)

class CommunityViewModel(
  private val communityRepository: CommunityRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(CommunityUiState(isLoading = true))
  val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

  init {
    loadCommunity()
  }

  private fun loadCommunity() {
    viewModelScope.launch {
      combine(
        communityRepository.observeQuestions(),
        communityRepository.observeBuilders()
      ) { questions, builders ->
        val filtered = filterQuestions(questions, _uiState.value.selectedCategory, _uiState.value.searchQuery)
        CommunityUiState(
          questions = questions,
          filteredQuestions = filtered,
          builders = builders,
          selectedCategory = _uiState.value.selectedCategory,
          searchQuery = _uiState.value.searchQuery,
          isLoading = false
        )
      }.collect { state ->
        _uiState.value = state
      }
    }
  }

  fun onSearchQueryChanged(query: String) {
    _uiState.value = _uiState.value.copy(
      searchQuery = query,
      filteredQuestions = filterQuestions(_uiState.value.questions, _uiState.value.selectedCategory, query)
    )
  }

  fun onCategorySelected(category: String) {
    _uiState.value = _uiState.value.copy(
      selectedCategory = category,
      filteredQuestions = filterQuestions(_uiState.value.questions, category, _uiState.value.searchQuery)
    )
  }

  private fun filterQuestions(
    questions: List<Question>,
    category: String,
    query: String
  ): List<Question> {
    val cleanQuery = query.trim().lowercase()
    return questions.filter { q ->
      val matchesCategory = (category == "All") ||
          q.category.equals(category, ignoreCase = true) ||
          q.technologies.any { it.equals(category, ignoreCase = true) }

      val matchesQuery = cleanQuery.isBlank() ||
          q.title.lowercase().contains(cleanQuery) ||
          q.description.lowercase().contains(cleanQuery) ||
          q.authorName.lowercase().contains(cleanQuery) ||
          q.technologies.any { it.lowercase().contains(cleanQuery) }

      matchesCategory && matchesQuery
    }
  }
}
