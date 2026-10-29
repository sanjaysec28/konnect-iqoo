package com.example.data.model

/**
 * QuestionLifecycleStage
 * Represents the 5 core stages of KONNECT:
 * ASK -> UNDERSTAND -> KONNECT -> SOLVE -> REMEMBER
 */
enum class QuestionLifecycleStage {
  ASK,
  UNDERSTAND,
  KONNECT,
  SOLVE,
  REMEMBER
}

/**
 * Question
 * Core question entity submitted by a builder.
 */
data class Question(
  val id: String,
  val title: String,
  val description: String,
  val authorId: String,
  val authorName: String,
  val category: String = "Android",
  val technologies: List<String> = emptyList(),
  val tags: List<String> = technologies,
  val imageUrl: String? = null,
  val stage: QuestionLifecycleStage = QuestionLifecycleStage.ASK,
  val matchedBuilderCount: Int = 0,
  val replyCount: Int = 0,
  val timeAgo: String = "Just now",
  val whyMatched: String? = null,
  val originalText: String = "$title $description".trim(),
  val normalizedText: String = "$title $description".trim().lowercase(),
  val summary: String = title,
  val difficulty: String = "INTERMEDIATE",
  val keywords: List<String> = technologies,
  val memoryContextId: String? = null,
  val status: String = if (stage == QuestionLifecycleStage.REMEMBER) "SOLVED" else "OPEN",
  val createdAtEpochMs: Long = System.currentTimeMillis()
)

/**
 * BuilderProfile
 * Represents a builder / developer within the community.
 */
data class BuilderProfile(
  val id: String,
  val name: String,
  val title: String,
  val bio: String,
  val role: String = title,
  val domains: List<String> = emptyList(),
  val skills: List<String> = domains,
  val contributionSummary: String = "Frequently contributes architectural advice.",
  val solvedQuestionsCount: Int = 0,
  val reputationScore: Int = 100,
  val isOnline: Boolean = false,
  val relevanceScore: Int = 95,
  val whyMatched: String? = null
)

/**
 * DiscussionReply
 * Individual community answer or technical discussion post.
 */
data class DiscussionReply(
  val id: String,
  val authorId: String,
  val authorName: String,
  val authorRole: String,
  val content: String,
  val isHelpful: Boolean = false,
  val isAccepted: Boolean = isHelpful,
  val timeAgo: String = "2h ago",
  val helpfulVotes: Int = 0,
  val createdAtEpochMs: Long = System.currentTimeMillis()
)

/**
 * AISummaryData
 * Structured technical summary synthesized by AI for a solved inquiry.
 */
data class AISummaryData(
  val problem: String,
  val keyInsight: String,
  val solutionSteps: List<String>,
  val usefulResources: List<String> = emptyList()
)

/**
 * Discussion
 * Complete discussion room containing original question, community replies, and resolution state.
 */
data class Discussion(
  val id: String,
  val questionId: String,
  val question: Question,
  val replies: List<DiscussionReply> = emptyList(),
  val isSolved: Boolean = false,
  val status: String = if (isSolved) "SOLVED" else "OPEN",
  val acceptedReplyId: String? = null,
  val summary: AISummaryData? = null
)

/**
 * MemoryItem
 * Represents solved architectural knowledge preserved for the future ("REMEMBER" stage).
 */
data class MemoryItem(
  val id: String,
  val questionId: String,
  val title: String,
  val summary: String,
  val problem: String = title,
  val insight: String = summary,
  val solution: String = summary,
  val tags: List<String> = emptyList(),
  val technologies: List<String> = tags,
  val keywords: List<String> = technologies,
  val references: List<String> = emptyList(),
  val solverName: String,
  val contributors: List<String> = listOf(solverName),
  val discussionCount: Int = 1,
  val whyMatched: String? = null,
  val status: String = "ACTIVE",
  val sourceDiscussionId: String? = "disc-$questionId",
  val acceptedReplyId: String? = null,
  val savedAtEpochMs: Long = System.currentTimeMillis()
)

/**
 * AskContext
 * Structured context carried forward from Community Memory into a new inquiry.
 */
data class AskContext(
  val memoryId: String,
  val problem: String,
  val solution: String,
  val technologies: List<String> = emptyList(),
  val keywords: List<String> = emptyList()
)

/**
 * QuestionAnalysis
 * Formal structured intelligence output from AIService.
 */
data class QuestionAnalysis(
  val summary: String,
  val category: String,
  val technologies: List<String>,
  val difficulty: String,
  val keywords: List<String>,
  val intent: String
)

/**
 * PersonMatch
 * Explainable matching result for an architect.
 */
data class PersonMatch(
  val user: BuilderProfile,
  val score: Int,
  val matchingSkills: List<String>,
  val solvedProblemCount: Int,
  val explanation: String
)

/**
 * DiscussionMatch
 * Explainable matching result for a related discussion.
 */
data class DiscussionMatch(
  val discussion: Question,
  val score: Int,
  val matchedTechnologies: List<String>,
  val matchedKeywords: List<String>,
  val explanation: String
)

/**
 * MemoryMatch
 * Explainable matching result for a community memory capsule.
 */
data class MemoryMatch(
  val memory: MemoryItem,
  val score: Int,
  val matchedTechnologies: List<String>,
  val matchedKeywords: List<String>,
  val explanation: String
)

/**
 * ConnectionRequest
 * Lightweight connection model between builders.
 */
data class ConnectionRequest(
  val id: String,
  val senderId: String,
  val receiverId: String,
  val receiverName: String,
  val receiverRole: String,
  val sharedTopic: String,
  val status: String = "Pending", // "Pending" or "Connected"
  val createdAt: Long = System.currentTimeMillis()
)

/**
 * NotificationItem
 * User notification updates.
 */
data class NotificationItem(
  val id: String,
  val title: String,
  val message: String,
  val timeAgo: String,
  val type: String, // "reply", "connection", "memory", "mention"
  val targetId: String? = null,
  val isRead: Boolean = false
)

/**
 * AISolution
 * AI comprehension and builder matching result.
 */
data class AISolution(
  val questionId: String,
  val understoodIntent: String,
  val suggestedDomains: List<String>,
  val recommendedBuilderIds: List<String>,
  val keyTakeaways: List<String> = emptyList(),
  val suggestedCategory: String = "Android"
)
