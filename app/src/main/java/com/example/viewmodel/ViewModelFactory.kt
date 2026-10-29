package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.di.KonnectContainer

class KonnectViewModelFactory(
  private val container: KonnectContainer
) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {
      modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
        HomeViewModel(
          communityRepository = container.communityRepository,
          userRepository = container.userRepository
        ) as T
      }
      modelClass.isAssignableFrom(AskViewModel::class.java) -> {
        AskViewModel(
          communityRepository = container.communityRepository,
          aiService = container.aiService
        ) as T
      }
      modelClass.isAssignableFrom(CommunityViewModel::class.java) -> {
        CommunityViewModel(
          communityRepository = container.communityRepository
        ) as T
      }
      modelClass.isAssignableFrom(MemoryViewModel::class.java) -> {
        MemoryViewModel(
          memoryRepository = container.memoryRepository
        ) as T
      }
      modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
        ProfileViewModel(
          userRepository = container.userRepository,
          communityRepository = container.communityRepository
        ) as T
      }
      modelClass.isAssignableFrom(AIWorkflowViewModel::class.java) -> {
        AIWorkflowViewModel(
          communityRepository = container.communityRepository,
          memoryRepository = container.memoryRepository,
          aiService = container.aiService
        ) as T
      }
      else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
  }
}
