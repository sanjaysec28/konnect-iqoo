package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.BuilderProfile
import com.example.data.model.ConnectionRequest
import com.example.data.model.Question
import com.example.data.repository.CommunityRepository
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class ProfileUiState(
  val profile: BuilderProfile? = null,
  val questionsAskedCount: Int = 4,
  val problemsSolvedCount: Int = 14,
  val connectionsCount: Int = 6,
  val isOnline: Boolean = true,
  val recentContributions: List<String> = listOf(
    "Added architectural pattern for Android 15 Foreground Service Camera bindings",
    "Optimized SQLite WAL2 buffer allocation on NVMe flash",
    "Verified zero-copy direct ByteBuffer inference pipeline on Qualcomm DSP",
    "Synthesized community memory for gRPC vs WebSocket radio state wakeup"
  ),
  val solvedProblems: List<String> = listOf(
    "Handling Firebase offline synchronization conflicts",
    "CameraX frame pooling without GC spikes",
    "Retrofit Authenticator synchronized token refresh"
  ),
  val isLoading: Boolean = false
)

class ProfileViewModel(
  private val userRepository: UserRepository,
  private val communityRepository: CommunityRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
  val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

  init {
    loadProfile()
  }

  private fun loadProfile() {
    viewModelScope.launch {
      combine(
        userRepository.observeCurrentUser(),
        communityRepository.observeConnections()
      ) { user, connections ->
        ProfileUiState(
          profile = user,
          questionsAskedCount = 4,
          problemsSolvedCount = user.solvedQuestionsCount,
          connectionsCount = 5 + connections.size,
          isOnline = user.isOnline,
          isLoading = false
        )
      }.collect { state ->
        _uiState.value = state
      }
    }
  }

  fun toggleOnlineStatus() {
    val current = _uiState.value.profile ?: return
    val updated = current.copy(isOnline = !current.isOnline)
    viewModelScope.launch {
      userRepository.updateProfile(updated)
    }
  }
}
