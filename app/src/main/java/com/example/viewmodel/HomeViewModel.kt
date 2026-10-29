package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.BuilderProfile
import com.example.data.model.Question
import com.example.data.model.QuestionLifecycleStage
import com.example.data.repository.CommunityRepository
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class HomeUiState(
  val greeting: String = "Good morning",
  val userName: String = "Alex",
  val unreadNotificationsCount: Int = 2,
  val currentLoopStage: QuestionLifecycleStage = QuestionLifecycleStage.ASK,
  val activeBuildersCount: Int = 8,
  val questionsSolvedCount: Int = 142,
  val recentQuestions: List<Question> = emptyList(),
  val topBuilders: List<BuilderProfile> = emptyList(),
  val isLoading: Boolean = false
)

class HomeViewModel(
  private val communityRepository: CommunityRepository,
  private val userRepository: UserRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
  val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

  init {
    loadHomeData()
  }

  private fun loadHomeData() {
    viewModelScope.launch {
      combine(
        communityRepository.observeQuestions(),
        communityRepository.observeBuilders(),
        userRepository.observeCurrentUser(),
        communityRepository.observeNotifications()
      ) { questions, builders, user, notifs ->
        HomeUiState(
          greeting = "Good morning",
          userName = user.name,
          unreadNotificationsCount = notifs.count { !it.isRead },
          currentLoopStage = QuestionLifecycleStage.ASK,
          activeBuildersCount = builders.count { it.isOnline },
          questionsSolvedCount = 142,
          recentQuestions = questions.take(6),
          topBuilders = builders.take(4),
          isLoading = false
        )
      }.collect { state ->
        _uiState.value = state
      }
    }
  }

  fun setLoopStage(stage: QuestionLifecycleStage) {
    _uiState.value = _uiState.value.copy(currentLoopStage = stage)
  }
}
