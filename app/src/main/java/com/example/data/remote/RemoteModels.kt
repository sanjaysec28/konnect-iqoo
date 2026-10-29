package com.example.data.remote

/**
 * Remote data transfer models and network responses for KONNECT.
 */
data class NetworkQuestionDto(
  val id: String,
  val title: String,
  val content: String,
  val tags: List<String>,
  val timestamp: Long
)

data class NetworkBuilderDto(
  val id: String,
  val name: String,
  val title: String,
  val bio: String,
  val domains: List<String>,
  val solvedCount: Int,
  val reputation: Int
)

data class NetworkResult<T>(
  val data: T?,
  val error: String? = null,
  val isSuccessful: Boolean = error == null
)

/**
 * KonnectRemoteService
 * Remote networking interface prepared for REST/Ktor/Retrofit integration.
 */
interface KonnectRemoteService {
  suspend fun fetchActiveQuestions(): NetworkResult<List<NetworkQuestionDto>>
  suspend fun fetchBuilders(): NetworkResult<List<NetworkBuilderDto>>
  suspend fun postQuestion(question: NetworkQuestionDto): NetworkResult<NetworkQuestionDto>
}
