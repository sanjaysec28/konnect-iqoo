package com.example.data.repository

import com.example.data.mock.CommunitySeedData
import com.example.data.model.AISummaryData
import com.example.data.model.BuilderProfile
import com.example.data.model.ConnectionRequest
import com.example.data.model.Discussion
import com.example.data.model.DiscussionReply
import com.example.data.model.NotificationItem
import com.example.data.model.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * CommunityRepository
 * Architectural interface for managing builders, active channels, questions, and discussions.
 */
interface CommunityRepository {
  fun observeQuestions(): Flow<List<Question>>
  fun observeBuilders(): Flow<List<BuilderProfile>>
  fun observeNotifications(): Flow<List<NotificationItem>>
  fun observeConnections(): Flow<List<ConnectionRequest>>
  suspend fun getQuestionById(questionId: String): Question?
  suspend fun submitQuestion(question: Question): Result<Question>
  suspend fun getBuildersByDomain(domain: String): List<BuilderProfile>
  suspend fun getDiscussionByQuestionId(questionId: String): Discussion
  suspend fun addReply(questionId: String, content: String, authorName: String = "Alex"): Discussion
  suspend fun markReplyAccepted(questionId: String, replyId: String): Discussion
  suspend fun markDiscussionSolved(questionId: String, summary: AISummaryData): Discussion
  suspend fun sendConnectionRequest(targetBuilderId: String, targetBuilderName: String, sharedTopic: String): Result<ConnectionRequest>
}

class DefaultCommunityRepository : CommunityRepository {
  private val _questions = MutableStateFlow<List<Question>>(CommunitySeedData.questions)
  private val _builders = MutableStateFlow<List<BuilderProfile>>(CommunitySeedData.builders)
  private val _notifications = MutableStateFlow<List<NotificationItem>>(CommunitySeedData.notifications)
  private val _connections = MutableStateFlow<List<ConnectionRequest>>(emptyList())
  private val _discussions = MutableStateFlow<Map<String, Discussion>>(CommunitySeedData.sampleDiscussions)

  override fun observeQuestions(): Flow<List<Question>> = _questions.asStateFlow()

  override fun observeBuilders(): Flow<List<BuilderProfile>> = _builders.asStateFlow()

  override fun observeNotifications(): Flow<List<NotificationItem>> = _notifications.asStateFlow()

  override fun observeConnections(): Flow<List<ConnectionRequest>> = _connections.asStateFlow()

  override suspend fun getQuestionById(questionId: String): Question? {
    return _questions.value.firstOrNull { it.id == questionId }
  }

  override suspend fun submitQuestion(question: Question): Result<Question> {
    _questions.value = listOf(question) + _questions.value
    // Create new discussion room
    val newDiscussion = Discussion(
      id = "disc-${question.id}",
      questionId = question.id,
      question = question,
      replies = emptyList()
    )
    _discussions.value = _discussions.value + (question.id to newDiscussion)
    return Result.success(question)
  }

  override suspend fun getBuildersByDomain(domain: String): List<BuilderProfile> {
    if (domain == "All") return _builders.value
    return _builders.value.filter { builder ->
      builder.domains.any { it.equals(domain, ignoreCase = true) } ||
          builder.skills.any { it.equals(domain, ignoreCase = true) }
    }
  }

  override suspend fun getDiscussionByQuestionId(questionId: String): Discussion {
    val existing = _discussions.value[questionId]
    if (existing != null) return existing

    val question = getQuestionById(questionId) ?: _questions.value.first()
    val fallbackDiscussion = Discussion(
      id = "disc-$questionId",
      questionId = question.id,
      question = question,
      replies = listOf(
        DiscussionReply(
          id = "rep-gen-1",
          authorId = "b-1",
          authorName = "Arjun Menon",
          authorRole = "Senior Android Architect",
          content = "Looking into this inquiry. The primary pattern is to decouple the UI state from the background service lifecycle, ensuring state restoration upon process recreation.",
          isHelpful = true,
          timeAgo = "15m ago",
          helpfulVotes = 12
        ),
        DiscussionReply(
          id = "rep-gen-2",
          authorId = "b-3",
          authorName = "David Thorne",
          authorRole = "Distributed Systems Lead",
          content = "Make sure to benchmark the throughput against memory-mapped DirectByteBuffer allocations to prevent Garbage Collection pauses during high-frequency writes.",
          isHelpful = false,
          timeAgo = "5m ago",
          helpfulVotes = 5
        )
      )
    )
    _discussions.value = _discussions.value + (questionId to fallbackDiscussion)
    return fallbackDiscussion
  }

  override suspend fun addReply(questionId: String, content: String, authorName: String): Discussion {
    val current = getDiscussionByQuestionId(questionId)
    val newReply = DiscussionReply(
      id = "rep-${System.currentTimeMillis()}",
      authorId = "u-primary",
      authorName = authorName,
      authorRole = "Client Systems Engineer",
      content = content,
      isHelpful = false,
      timeAgo = "Just now"
    )
    val updated = current.copy(
      replies = current.replies + newReply
    )
    _discussions.value = _discussions.value + (questionId to updated)
    // Update reply count in question
    _questions.value = _questions.value.map {
      if (it.id == questionId) it.copy(replyCount = it.replyCount + 1) else it
    }
    return updated
  }

  override suspend fun markReplyAccepted(questionId: String, replyId: String): Discussion {
    val current = getDiscussionByQuestionId(questionId)
    val updatedReplies = current.replies.map {
      if (it.id == replyId) it.copy(isHelpful = true, isAccepted = true) else it
    }
    val updated = current.copy(
      replies = updatedReplies,
      acceptedReplyId = replyId
    )
    _discussions.value = _discussions.value + (questionId to updated)
    return updated
  }

  override suspend fun markDiscussionSolved(questionId: String, summary: AISummaryData): Discussion {
    val current = getDiscussionByQuestionId(questionId)
    val updated = current.copy(
      isSolved = true,
      status = "SOLVED",
      summary = summary
    )
    _discussions.value = _discussions.value + (questionId to updated)
    _questions.value = _questions.value.map {
      if (it.id == questionId) it.copy(
        stage = com.example.data.model.QuestionLifecycleStage.REMEMBER,
        status = "SOLVED"
      ) else it
    }
    // Add notification
    val notif = NotificationItem(
      id = "notif-${System.currentTimeMillis()}",
      title = "Discussion added to Community Memory",
      message = "Your solution for '${current.question.title}' is now preserved.",
      timeAgo = "Just now",
      type = "memory",
      targetId = questionId
    )
    _notifications.value = listOf(notif) + _notifications.value
    return updated
  }

  override suspend fun sendConnectionRequest(
    targetBuilderId: String,
    targetBuilderName: String,
    sharedTopic: String
  ): Result<ConnectionRequest> {
    val request = ConnectionRequest(
      id = "conn-${System.currentTimeMillis()}",
      senderId = "u-primary",
      receiverId = targetBuilderId,
      receiverName = targetBuilderName,
      receiverRole = _builders.value.firstOrNull { it.id == targetBuilderId }?.title ?: "Architect",
      sharedTopic = sharedTopic,
      status = "Pending"
    )
    _connections.value = listOf(request) + _connections.value
    // Notification feedback
    val notif = NotificationItem(
      id = "notif-${System.currentTimeMillis()}",
      title = "Connection request sent",
      message = "Sent connection request to $targetBuilderName regarding '$sharedTopic'.",
      timeAgo = "Just now",
      type = "connection",
      targetId = targetBuilderId
    )
    _notifications.value = listOf(notif) + _notifications.value
    return Result.success(request)
  }
}
