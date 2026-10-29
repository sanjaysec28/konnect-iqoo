package com.example.data.repository

import com.example.data.mock.CommunitySeedData
import com.example.data.model.BuilderProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * UserRepository
 * Architectural interface for managing current user profile, skills, and builder identity.
 */
interface UserRepository {
  fun observeCurrentUser(): Flow<BuilderProfile>
  suspend fun getCurrentUser(): BuilderProfile
  suspend fun updateProfile(updated: BuilderProfile): Result<Unit>
}

class DefaultUserRepository : UserRepository {
  private val _currentUser = MutableStateFlow(CommunitySeedData.currentUser)

  override fun observeCurrentUser(): Flow<BuilderProfile> = _currentUser.asStateFlow()

  override suspend fun getCurrentUser(): BuilderProfile = _currentUser.value

  override suspend fun updateProfile(updated: BuilderProfile): Result<Unit> {
    _currentUser.value = updated
    return Result.success(Unit)
  }
}
