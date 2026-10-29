package com.example.di

import android.content.Context
import com.example.data.ai.AIService
import com.example.data.ai.GeminiAIService
import com.example.data.firebase.DefaultFirebaseService
import com.example.data.firebase.FirebaseService
import com.example.data.local.KonnectDatabase
import com.example.data.repository.AIRepository
import com.example.data.repository.CommunityRepository
import com.example.data.repository.DefaultAIRepository
import com.example.data.repository.DefaultCommunityRepository
import com.example.data.repository.DefaultMemoryRepository
import com.example.data.repository.DefaultUserRepository
import com.example.data.repository.MemoryRepository
import com.example.data.repository.UserRepository
import com.example.util.DefaultDispatcherProvider
import com.example.util.DispatcherProvider
import com.example.util.LiveNetworkMonitor
import com.example.util.NetworkMonitor

/**
 * KonnectContainer
 * Modular service locator / dependency injection container.
 * Provides clean access to repositories and services across the application.
 */
class KonnectContainer(private val appContext: Context) {

  val dispatcherProvider: DispatcherProvider by lazy {
    DefaultDispatcherProvider()
  }

  val networkMonitor: NetworkMonitor by lazy {
    LiveNetworkMonitor(appContext)
  }

  val database: KonnectDatabase by lazy {
    KonnectDatabase.getDatabase(appContext)
  }

  val communityRepository: CommunityRepository by lazy {
    DefaultCommunityRepository()
  }

  val aiRepository: AIRepository by lazy {
    DefaultAIRepository(communityRepository)
  }

  val userRepository: UserRepository by lazy {
    DefaultUserRepository()
  }

  val memoryRepository: MemoryRepository by lazy {
    DefaultMemoryRepository()
  }

  val aiService: AIService by lazy {
    GeminiAIService()
  }

  val firebaseService: FirebaseService by lazy {
    DefaultFirebaseService()
  }

  companion object {
    @Volatile
    private var instance: KonnectContainer? = null

    fun initialize(context: Context): KonnectContainer {
      return instance ?: synchronized(this) {
        KonnectContainer(context.applicationContext).also { instance = it }
      }
    }

    fun get(): KonnectContainer {
      return checkNotNull(instance) { "KonnectContainer has not been initialized." }
    }
  }
}
