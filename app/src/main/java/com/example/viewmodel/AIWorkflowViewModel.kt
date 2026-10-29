package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AIAnalysisResult
import com.example.data.ai.AIService
import com.example.data.model.AISummaryData
import com.example.data.model.AskContext
import com.example.data.model.BuilderProfile
import com.example.data.model.Discussion
import com.example.data.model.MemoryItem
import com.example.data.model.Question
import com.example.data.repository.CommunityRepository
import com.example.data.repository.MemoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class AIWorkflowUiState(
  val processingStage: Int = 1, // 1: Understanding, 2: People, 3: Knowledge, 4: Done
  val statusMessage: String = "Understanding your question...",
  val subMessage: String = "Extracting problem intent and architectural boundaries",
  val question: Question? = null,
  val analysisResult: AIAnalysisResult? = null,
  val matchedBuilders: List<BuilderProfile> = emptyList(),
  val matchedDiscussions: List<Question> = emptyList(),
  val matchedMemories: List<MemoryItem> = emptyList(),
  val currentDiscussion: Discussion? = null,
  val aiSummary: AISummaryData? = null,
  val isDiscussionSolved: Boolean = false,
  val isConnectingBuilder: BuilderProfile? = null, // for Connect bottom sheet
  val connectionSentForId: String? = null,
  val isSummarizing: Boolean = false,
  val isSavedToMemory: Boolean = false
)

class AIWorkflowViewModel(
  private val communityRepository: CommunityRepository,
  private val memoryRepository: MemoryRepository,
  private val aiService: AIService
) : ViewModel() {

  private val _uiState = MutableStateFlow(AIWorkflowUiState())
  val uiState: StateFlow<AIWorkflowUiState> = _uiState.asStateFlow()

  fun startProcessing(questionId: String, onFinished: () -> Unit) {
    viewModelScope.launch {
      val question = communityRepository.getQuestionById(questionId)
        ?: communityRepository.observeQuestions().first().first()

      _uiState.value = _uiState.value.copy(
        question = question,
        processingStage = 1,
        statusMessage = "Understanding your question...",
        subMessage = "Extracting technical intent for '${question.title}'"
      )

      // Fetch memory context if linked
      val memoryContext: AskContext? = if (question.memoryContextId != null) {
        memoryRepository.getMemoryById(question.memoryContextId)?.let { mem ->
          AskContext(
            memoryId = mem.id,
            problem = mem.problem,
            solution = mem.solution,
            technologies = mem.technologies,
            keywords = mem.keywords
          )
        }
      } else null

      val structuredAnalysis = aiService.analyzeQuestionWithContext(
        text = question.title + " " + question.description,
        imageUri = question.imageUrl,
        category = question.category,
        memoryContext = memoryContext
      )

      val analysis = AIAnalysisResult(
        problemSummary = structuredAnalysis.summary,
        topics = structuredAnalysis.keywords,
        technologies = structuredAnalysis.technologies,
        difficulty = structuredAnalysis.difficulty,
        keywords = structuredAnalysis.keywords,
        suggestedCategory = structuredAnalysis.category,
        searchTerms = structuredAnalysis.keywords,
        intent = structuredAnalysis.intent
      )

      delay(900)

      _uiState.value = _uiState.value.copy(
        processingStage = 2,
        analysisResult = analysis,
        statusMessage = "Finding relevant people...",
        subMessage = "Evaluating domain relevance across ${analysis.technologies.joinToString(", ")}"
      )

      val allBuilders = communityRepository.observeBuilders().first()
      val matchedPeople = aiService.matchPeopleStructured(
        analysis = structuredAnalysis,
        allBuilders = allBuilders
      ).map { it.user }

      delay(900)

      _uiState.value = _uiState.value.copy(
        processingStage = 3,
        matchedBuilders = matchedPeople,
        statusMessage = "Searching community knowledge...",
        subMessage = "Cross-referencing verified architectural memory records"
      )

      val allQuestions = communityRepository.observeQuestions().first()
      val matchedDiscussions = aiService.matchDiscussionsStructured(
        analysis = structuredAnalysis,
        query = question.title,
        allDiscussions = allQuestions.filter { it.id != question.id }
      ).map { it.discussion }

      val allMemories = memoryRepository.observeMemories().first()
      val matchedMemories = aiService.matchMemoriesStructured(
        analysis = structuredAnalysis,
        query = question.title,
        allMemories = allMemories
      ).map { it.memory }

      delay(800)

      _uiState.value = _uiState.value.copy(
        processingStage = 4,
        matchedDiscussions = matchedDiscussions,
        matchedMemories = matchedMemories
      )

      onFinished()
    }
  }

  fun loadDiscussion(questionId: String) {
    viewModelScope.launch {
      val discussion = communityRepository.getDiscussionByQuestionId(questionId)
      _uiState.value = _uiState.value.copy(
        currentDiscussion = discussion,
        isDiscussionSolved = discussion.isSolved,
        aiSummary = discussion.summary
      )
    }
  }

  fun addReply(questionId: String, content: String) {
    if (content.isBlank()) return
    viewModelScope.launch {
      val updated = communityRepository.addReply(questionId, content)
      _uiState.value = _uiState.value.copy(currentDiscussion = updated)
    }
  }

  fun markReplyAccepted(replyId: String) {
    val discussion = _uiState.value.currentDiscussion ?: return
    viewModelScope.launch {
      val updated = communityRepository.markReplyAccepted(discussion.question.id, replyId)
      _uiState.value = _uiState.value.copy(currentDiscussion = updated)
    }
  }

  fun openConnectSheet(builder: BuilderProfile) {
    _uiState.value = _uiState.value.copy(isConnectingBuilder = builder)
  }

  fun closeConnectSheet() {
    _uiState.value = _uiState.value.copy(isConnectingBuilder = null)
  }

  fun sendConnectRequest(builder: BuilderProfile, sharedTopic: String) {
    viewModelScope.launch {
      communityRepository.sendConnectionRequest(builder.id, builder.name, sharedTopic)
      _uiState.value = _uiState.value.copy(
        connectionSentForId = builder.id,
        isConnectingBuilder = null
      )
    }
  }

  fun generateSummaryForDiscussion(discussion: Discussion) {
    _uiState.value = _uiState.value.copy(isSummarizing = true)
    viewModelScope.launch {
      delay(800)
      val summary = aiService.summarizeDiscussion(discussion)
      _uiState.value = _uiState.value.copy(
        aiSummary = summary,
        isSummarizing = false
      )
    }
  }

  fun markDiscussionSolvedAndSaveMemory(onCompleted: () -> Unit) {
    val discussion = _uiState.value.currentDiscussion ?: return
    val summary = _uiState.value.aiSummary ?: return

    viewModelScope.launch {
      communityRepository.markDiscussionSolved(discussion.question.id, summary)
      val memory = aiService.createMemory(discussion, summary)
      memoryRepository.saveMemory(memory)
      _uiState.value = _uiState.value.copy(
        isDiscussionSolved = true,
        isSavedToMemory = true
      )
      onCompleted()
    }
  }
}
