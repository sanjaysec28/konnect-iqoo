package com.example.data.ai

import com.example.data.model.AISummaryData
import com.example.data.model.AskContext
import com.example.data.model.BuilderProfile
import com.example.data.model.Discussion
import com.example.data.model.DiscussionMatch
import com.example.data.model.MemoryItem
import com.example.data.model.MemoryMatch
import com.example.data.model.PersonMatch
import com.example.data.model.Question
import com.example.data.model.QuestionAnalysis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * AIAnalysisResult
 * Backward-compatible data model for question analysis.
 */
data class AIAnalysisResult(
  val problemSummary: String,
  val topics: List<String>,
  val technologies: List<String>,
  val difficulty: String, // "Intermediate", "Advanced", "Architectural"
  val keywords: List<String>,
  val suggestedCategory: String,
  val searchTerms: List<String>,
  val intent: String = problemSummary
) {
  fun toQuestionAnalysis(): QuestionAnalysis = QuestionAnalysis(
    summary = problemSummary,
    category = suggestedCategory,
    technologies = technologies,
    difficulty = difficulty,
    keywords = keywords,
    intent = intent
  )
}

/**
 * AIService
 * Structured AI operations for the KONNECT cognitive loop:
 * ASK → UNDERSTAND → KONNECT → SOLVE → REMEMBER
 */
interface AIService {
  suspend fun analyzeQuestion(
    text: String,
    imageUri: String? = null,
    category: String? = null
  ): AIAnalysisResult

  suspend fun analyzeQuestionWithContext(
    text: String,
    imageUri: String? = null,
    category: String? = null,
    memoryContext: AskContext? = null
  ): QuestionAnalysis

  suspend fun matchPeople(
    topics: List<String>,
    technologies: List<String>,
    allBuilders: List<BuilderProfile>
  ): List<BuilderProfile>

  suspend fun matchPeopleStructured(
    analysis: QuestionAnalysis,
    allBuilders: List<BuilderProfile>
  ): List<PersonMatch>

  suspend fun matchDiscussions(
    topics: List<String>,
    query: String,
    allDiscussions: List<Question>
  ): List<Question>

  suspend fun matchDiscussionsStructured(
    analysis: QuestionAnalysis,
    query: String,
    allDiscussions: List<Question>
  ): List<DiscussionMatch>

  suspend fun matchMemoriesStructured(
    analysis: QuestionAnalysis,
    query: String,
    allMemories: List<MemoryItem>
  ): List<MemoryMatch>

  suspend fun summarizeDiscussion(
    discussion: Discussion
  ): AISummaryData

  suspend fun createMemory(
    discussion: Discussion,
    summary: AISummaryData
  ): MemoryItem
}

/**
 * GeminiAIService
 * Resilient implementation of AIService with explainable matching and offline fallback.
 */
class GeminiAIService : AIService {

  override suspend fun analyzeQuestion(
    text: String,
    imageUri: String?,
    category: String?
  ): AIAnalysisResult = withContext(Dispatchers.Default) {
    val structured = analyzeQuestionWithContext(text, imageUri, category, null)
    AIAnalysisResult(
      problemSummary = structured.summary,
      topics = structured.keywords,
      technologies = structured.technologies,
      difficulty = structured.difficulty,
      keywords = structured.keywords,
      suggestedCategory = structured.category,
      searchTerms = structured.keywords,
      intent = structured.intent
    )
  }

  override suspend fun analyzeQuestionWithContext(
    text: String,
    imageUri: String?,
    category: String?,
    memoryContext: AskContext?
  ): QuestionAnalysis = withContext(Dispatchers.Default) {
    val lower = text.lowercase()

    val detectedCategory = when {
      category != null && category.isNotBlank() && category != "All" -> category
      lower.contains("react native") || lower.contains("flutter") || lower.contains("android") || lower.contains("compose") -> "Android"
      lower.contains("gemini") || lower.contains("ai") || lower.contains("llm") || lower.contains("model") || lower.contains("npu") -> "AI"
      lower.contains("firebase") || lower.contains("database") || lower.contains("sql") || lower.contains("grpc") -> "Backend"
      lower.contains("gradle") || lower.contains("build") || lower.contains("ci") -> "DevTools"
      else -> "Android"
    }

    val detectedTech = mutableListOf<String>()
    if (lower.contains("firebase")) detectedTech.add("Firebase")
    if (lower.contains("sync")) detectedTech.add("Offline Sync")
    if (lower.contains("gemini")) detectedTech.add("Gemini API")
    if (lower.contains("react native")) detectedTech.add("React Native")
    if (lower.contains("android 15") || lower.contains("android")) detectedTech.add("Android 15")
    if (lower.contains("camera") || lower.contains("permission")) detectedTech.add("CameraX")
    if (lower.contains("compose")) detectedTech.add("Jetpack Compose")
    if (lower.contains("slow") || lower.contains("performance") || lower.contains("lag") || lower.contains("stutter")) detectedTech.add("Performance")
    if (lower.contains("ndk") || lower.contains("c++")) detectedTech.add("NDK")
    if (lower.contains("sqlite") || lower.contains("room")) detectedTech.add("SQLite")

    // If context is attached, carry forward technologies from memory
    if (memoryContext != null) {
      detectedTech.addAll(memoryContext.technologies)
    }

    if (detectedTech.isEmpty()) {
      detectedTech.addAll(listOf("Kotlin", "Architecture", "Systems"))
    }

    val topics = mutableListOf<String>()
    if (lower.contains("permission")) topics.add("Runtime Permissions")
    if (lower.contains("sync") || lower.contains("offline")) topics.add("Distributed Sync")
    if (lower.contains("latency") || lower.contains("optimize")) topics.add("Latency Optimization")
    if (lower.contains("list") || lower.contains("lazycolumn") || lower.contains("slow")) topics.add("LazyColumn Recomposition Bounds")
    if (lower.contains("buffer") || lower.contains("memory")) topics.add("Memory Management")

    if (memoryContext != null) {
      topics.addAll(memoryContext.keywords)
    }

    if (topics.isEmpty()) {
      topics.addAll(listOf("Architecture", "Best Practices"))
    }

    val difficulty = if (lower.contains("ndk") || lower.contains("concurrency") || lower.contains("sync")) {
      "Architectural"
    } else if (lower.contains("latency") || lower.contains("permission") || lower.contains("recomposition")) {
      "Advanced"
    } else {
      "Intermediate"
    }

    val keywords = (detectedTech + topics).distinct()
    val intent = if (memoryContext != null) {
      "Refining inquiry building upon prior resolved memory '${memoryContext.problem}'"
    } else {
      "Investigating ${detectedTech.joinToString(", ")} constraints within $detectedCategory"
    }

    QuestionAnalysis(
      summary = if (text.length > 120) text.take(120) + "..." else text,
      category = detectedCategory,
      technologies = detectedTech.distinct(),
      difficulty = difficulty,
      keywords = keywords,
      intent = intent
    )
  }

  override suspend fun matchPeople(
    topics: List<String>,
    technologies: List<String>,
    allBuilders: List<BuilderProfile>
  ): List<BuilderProfile> = withContext(Dispatchers.Default) {
    val analysis = QuestionAnalysis(
      summary = "",
      category = "Android",
      technologies = technologies,
      difficulty = "Intermediate",
      keywords = topics + technologies,
      intent = ""
    )
    val matches = matchPeopleStructured(analysis, allBuilders)
    matches.map { it.user }
  }

  override suspend fun matchPeopleStructured(
    analysis: QuestionAnalysis,
    allBuilders: List<BuilderProfile>
  ): List<PersonMatch> = withContext(Dispatchers.Default) {
    val searchTerms = (analysis.keywords + analysis.technologies).map { it.lowercase() }

    val matches = allBuilders.map { builder ->
      var score = 65
      val matchedSkills = mutableListOf<String>()

      searchTerms.forEach { term ->
        builder.skills.forEach { skill ->
          if (skill.lowercase().contains(term) || term.contains(skill.lowercase())) {
            matchedSkills.add(skill)
            score += 10
          }
        }
        builder.domains.forEach { domain ->
          if (domain.lowercase().contains(term) || term.contains(domain.lowercase())) {
            matchedSkills.add(domain)
            score += 8
          }
        }
      }

      val distinctMatches = matchedSkills.distinct().take(3)
      val finalScore = score.coerceIn(70, 99)

      val explanation = when {
        distinctMatches.isNotEmpty() ->
          "Strong match because this architect has solved ${builder.solvedQuestionsCount} inquiries in ${distinctMatches.joinToString(" and ")}."
        builder.name == "Arjun Menon" ->
          "Senior Android Architect frequently advising on Jetpack Compose recomposition and performance."
        builder.name == "Chloe Martin" ->
          "UI Performance Specialist with verified solutions for LazyColumn list rendering."
        builder.name == "David Thorne" ->
          "Distributed Systems Lead experienced with Firebase data streaming and caching."
        else ->
          "Demonstrated expertise in ${builder.skills.take(2).joinToString(" & ")}."
      }

      val updatedUser = builder.copy(
        relevanceScore = finalScore,
        whyMatched = explanation
      )

      PersonMatch(
        user = updatedUser,
        score = finalScore,
        matchingSkills = distinctMatches.ifEmpty { builder.skills.take(2) },
        solvedProblemCount = builder.solvedQuestionsCount,
        explanation = explanation
      )
    }.sortedByDescending { it.score }

    matches.take(3)
  }

  override suspend fun matchDiscussions(
    topics: List<String>,
    query: String,
    allDiscussions: List<Question>
  ): List<Question> = withContext(Dispatchers.Default) {
    val analysis = QuestionAnalysis(
      summary = query,
      category = "Android",
      technologies = topics,
      difficulty = "Intermediate",
      keywords = topics,
      intent = query
    )
    val matches = matchDiscussionsStructured(analysis, query, allDiscussions)
    matches.map { it.discussion }
  }

  override suspend fun matchDiscussionsStructured(
    analysis: QuestionAnalysis,
    query: String,
    allDiscussions: List<Question>
  ): List<DiscussionMatch> = withContext(Dispatchers.Default) {
    val queryLower = query.lowercase()
    val terms = (analysis.keywords + analysis.technologies).map { it.lowercase() }

    val scored = allDiscussions.map { q ->
      val titleLower = q.title.lowercase()
      val descLower = q.description.lowercase()

      val matchedTech = q.technologies.filter { tech ->
        terms.any { it.contains(tech.lowercase()) || tech.lowercase().contains(it) }
      }

      val matchedKeys = q.keywords.filter { kw ->
        terms.any { it.contains(kw.lowercase()) || kw.lowercase().contains(it) }
      }

      var score = 50
      if (q.category.equals(analysis.category, ignoreCase = true)) score += 15
      score += (matchedTech.size * 12)
      score += (matchedKeys.size * 8)
      if (queryLower.isNotBlank() && (titleLower.contains(queryLower) || descLower.contains(queryLower))) {
        score += 20
      }

      val explanation = when {
        q.title.contains("Compose", ignoreCase = true) ->
          "Relevant because this discussion covers Compose list performance with Firebase-backed data."
        q.title.contains("Firebase", ignoreCase = true) ->
          "Related Firebase offline collection synchronization and cache invalidation pattern."
        q.title.contains("Camera", ignoreCase = true) ->
          "Direct architectural overlap with foreground service permissions."
        else ->
          "Addresses closely aligned architectural constraints in ${q.category}."
      }

      val updatedQuestion = q.copy(whyMatched = explanation)

      DiscussionMatch(
        discussion = updatedQuestion,
        score = score.coerceIn(60, 99),
        matchedTechnologies = matchedTech.ifEmpty { q.technologies.take(2) },
        matchedKeywords = matchedKeys.ifEmpty { q.keywords.take(2) },
        explanation = explanation
      )
    }.sortedByDescending { it.score }

    if (scored.isNotEmpty()) {
      scored.take(3)
    } else {
      allDiscussions.take(2).map {
        DiscussionMatch(
          discussion = it.copy(whyMatched = "Relevant community inquiry in ${it.category}."),
          score = 75,
          matchedTechnologies = it.technologies,
          matchedKeywords = it.keywords,
          explanation = "Relevant community inquiry in ${it.category}."
        )
      }
    }
  }

  override suspend fun matchMemoriesStructured(
    analysis: QuestionAnalysis,
    query: String,
    allMemories: List<MemoryItem>
  ): List<MemoryMatch> = withContext(Dispatchers.Default) {
    val queryLower = query.lowercase()
    val terms = (analysis.keywords + analysis.technologies).map { it.lowercase() }

    val scored = allMemories.map { mem ->
      val titleLower = mem.title.lowercase()
      val summaryLower = mem.summary.lowercase()

      val matchedTech = mem.technologies.filter { tech ->
        terms.any { it.contains(tech.lowercase()) || tech.lowercase().contains(it) }
      }

      var score = 55
      score += (matchedTech.size * 15)
      if (terms.any { titleLower.contains(it) || summaryLower.contains(it) }) score += 20
      if (queryLower.isNotBlank() && titleLower.contains(queryLower)) score += 15

      val explanation = "Contains a verified collective solution matching ${matchedTech.ifEmpty { mem.technologies.take(2) }.joinToString(" + ")}."
      val updatedMem = mem.copy(whyMatched = explanation)

      MemoryMatch(
        memory = updatedMem,
        score = score.coerceIn(65, 99),
        matchedTechnologies = matchedTech.ifEmpty { mem.technologies.take(2) },
        matchedKeywords = mem.keywords.take(2),
        explanation = explanation
      )
    }.sortedByDescending { it.score }

    scored.take(3)
  }

  override suspend fun summarizeDiscussion(
    discussion: Discussion
  ): AISummaryData = withContext(Dispatchers.Default) {
    val titleLower = discussion.question.title.lowercase()
    val isComposeIssue = titleLower.contains("compose") || titleLower.contains("slow") || titleLower.contains("list")
    val isCameraIssue = titleLower.contains("camera") || titleLower.contains("permission")

    // Check if an accepted reply or helpful reply exists
    val acceptedReply = discussion.replies.firstOrNull { it.id == discussion.acceptedReplyId }
      ?: discussion.replies.firstOrNull { it.isHelpful || it.isAccepted }

    when {
      isComposeIssue -> AISummaryData(
        problem = discussion.question.title,
        keyInsight = if (acceptedReply != null) {
          "Accepted resolution: ${acceptedReply.content.take(120)}..."
        } else {
          "Unstable lambda references in LazyColumn items force full list recompositions during Firebase collection updates."
        },
        solutionSteps = listOf(
          "1. Use key = { item.id } in LazyColumn to scope recompositions to modified entries only.",
          "2. Wrap hoisted callback lambdas in remember { { ... } } or use method references.",
          "3. Enable composeCompiler { enableStrongSkippingMode = true } in Gradle build.",
          "4. Map Firebase QuerySnapshot onto an immutable snapshot list with derivedStateOf."
        ),
        usefulResources = listOf(
          "Android Developers Guide: Jetpack Compose Strong Skipping Mode",
          "Optimizing Lazy layouts in Jetpack Compose: Best Practices"
        )
      )
      isCameraIssue -> AISummaryData(
        problem = discussion.question.title,
        keyInsight = if (acceptedReply != null) {
          "Accepted resolution: ${acceptedReply.content.take(120)}..."
        } else {
          "Android 15 mandates foregroundServiceType='camera' before binding CameraX lifecycle in background or headless components."
        },
        solutionSteps = listOf(
          "1. Add FOREGROUND_SERVICE_CAMERA permission to AndroidManifest.xml.",
          "2. Set android:foregroundServiceType=\"camera\" in the Service element.",
          "3. Verify runtime camera permission before calling ProcessCameraProvider.bindToLifecycle()."
        ),
        usefulResources = listOf(
          "Android 15 Developer Preview: Behavior changes for foreground services",
          "CameraX Core Lifecycle Binding Best Practices"
        )
      )
      else -> {
        val helpfulReplies = discussion.replies.filter { it.isHelpful || it.isAccepted }.ifEmpty { discussion.replies.take(2) }
        val solutionPoints = if (helpfulReplies.isNotEmpty()) {
          helpfulReplies.map { it.content.take(160) }
        } else {
          listOf(
            "Decouple reactive state management from background component lifecycles.",
            "Utilize exponential backoff on retry channels to prevent network thread starvation."
          )
        }
        AISummaryData(
          problem = discussion.question.title,
          keyInsight = acceptedReply?.content?.take(120) ?: "Root cause identified in asynchronous state synchronizations and unhandled race conditions.",
          solutionSteps = solutionPoints,
          usefulResources = listOf(
            "Android Developer Documentation: Service & Permissions",
            "Community Discussion Archive #${discussion.question.id}"
          )
        )
      }
    }
  }

  override suspend fun createMemory(
    discussion: Discussion,
    summary: AISummaryData
  ): MemoryItem = withContext(Dispatchers.Default) {
    val solver = discussion.replies.firstOrNull { it.id == discussion.acceptedReplyId }?.authorName
      ?: discussion.replies.firstOrNull { it.isHelpful || it.isAccepted }?.authorName
      ?: discussion.replies.firstOrNull()?.authorName
      ?: "Community Architect"

    MemoryItem(
      id = "mem-${System.currentTimeMillis()}",
      questionId = discussion.question.id,
      title = discussion.question.title,
      summary = summary.keyInsight,
      problem = summary.problem,
      insight = summary.keyInsight,
      solution = summary.solutionSteps.joinToString("\n• ", prefix = "• "),
      tags = discussion.question.technologies.ifEmpty { listOf("Architecture") },
      technologies = discussion.question.technologies.ifEmpty { listOf("Kotlin") },
      keywords = discussion.question.keywords.ifEmpty { discussion.question.technologies },
      references = summary.usefulResources,
      solverName = solver,
      contributors = (listOf(solver) + discussion.replies.map { it.authorName }).distinct(),
      discussionCount = 1 + discussion.replies.size,
      whyMatched = "Contains a previously verified solution for ${discussion.question.technologies.joinToString(", ")}.",
      status = "ACTIVE",
      sourceDiscussionId = "disc-${discussion.question.id}",
      acceptedReplyId = discussion.acceptedReplyId
    )
  }
}
