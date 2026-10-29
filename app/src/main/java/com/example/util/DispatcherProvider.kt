package com.example.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * DispatcherProvider
 * Clean abstraction for Kotlin Coroutines dispatchers to facilitate unit testing.
 */
interface DispatcherProvider {
  val main: CoroutineDispatcher
  val io: CoroutineDispatcher
  val default: CoroutineDispatcher
  val unconfined: CoroutineDispatcher
}

class DefaultDispatcherProvider : DispatcherProvider {
  override val main: CoroutineDispatcher = Dispatchers.Main
  override val io: CoroutineDispatcher = Dispatchers.IO
  override val default: CoroutineDispatcher = Dispatchers.Default
  override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}

/**
 * KonnectResult
 * Generic wrapper for UI states & repository operations.
 */
sealed class KonnectResult<out T> {
  data class Success<out T>(val data: T) : KonnectResult<T>()
  data class Error(val exception: Throwable, val message: String? = null) : KonnectResult<Nothing>()
  data object Loading : KonnectResult<Nothing>()
}
