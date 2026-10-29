package com.example.data.firebase

/**
 * FirebaseService
 * Core Firebase-ready abstraction for Firestore real-time sync, Authentication, and App Check.
 * Prepared for Firebase integration in subsequent phases.
 */
interface FirebaseService {
  suspend fun initialize(): Boolean
  fun isUserAuthenticated(): Boolean
  suspend fun getSyncToken(): String?
}

class DefaultFirebaseService : FirebaseService {
  override suspend fun initialize(): Boolean {
    // Architectural hook for FirebaseApp.initializeApp
    return true
  }

  override fun isUserAuthenticated(): Boolean {
    return true
  }

  override suspend fun getSyncToken(): String? {
    return null
  }
}
