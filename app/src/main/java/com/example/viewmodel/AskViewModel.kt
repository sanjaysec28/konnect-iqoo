package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AIService
import com.example.data.model.AskContext
import com.example.data.model.Question
import com.example.data.model.QuestionLifecycleStage
import com.example.data.repository.CommunityRepository
import com.example.ui.components.KonnectVoiceState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AskUiState(
  val title: String = "",
  val description: String = "",
  val selectedCategory: String = "Android",
  val availableCategories: List<String> = listOf("Android", "AI", "Web", "Backend", "Data", "DevTools"),
  val imageUri: String? = null,
  val isCameraActive: Boolean = false,
  val voiceState: KonnectVoiceState = KonnectVoiceState.Idle,
  val voiceTranscript: String = "",
  val isSubmitting: Boolean = false,
  val errorMessage: String? = null,
  val infoMessage: String? = null,
  val memoryContext: AskContext? = null,
  val submittedQuestionId: String? = null
)

class AskViewModel(
  private val communityRepository: CommunityRepository,
  private val aiService: AIService
) : ViewModel() {

  private val _uiState = MutableStateFlow(AskUiState())
  val uiState: StateFlow<AskUiState> = _uiState.asStateFlow()

  fun setInitialContent(
    title: String,
    description: String = "",
    imageUri: String? = null,
    memoryContext: AskContext? = null
  ) {
    _uiState.value = _uiState.value.copy(
      title = title,
      description = description,
      imageUri = imageUri ?: _uiState.value.imageUri,
      memoryContext = memoryContext,
      infoMessage = if (memoryContext != null || title.startsWith("Follow-up on"))
        "Context from Community Memory attached to inquiry."
      else null
    )
  }

  fun onTitleChanged(newTitle: String) {
    _uiState.value = _uiState.value.copy(title = newTitle, errorMessage = null, infoMessage = null)
  }

  fun onDescriptionChanged(newDescription: String) {
    _uiState.value = _uiState.value.copy(description = newDescription, errorMessage = null)
  }

  fun onCategorySelected(category: String) {
    _uiState.value = _uiState.value.copy(selectedCategory = category)
  }

  fun onImageSelected(uriString: String?) {
    _uiState.value = _uiState.value.copy(imageUri = uriString, errorMessage = null)
  }

  fun removeImage() {
    _uiState.value = _uiState.value.copy(imageUri = null)
  }

  fun activateVoiceDirectly() {
    onVoiceClick(hasRecordAudioPermission = true)
  }

  fun onVoiceClick(hasRecordAudioPermission: Boolean = true) {
    if (!hasRecordAudioPermission) {
      _uiState.value = _uiState.value.copy(errorMessage = "Microphone permission is required for voice input.")
      return
    }

    val currentState = _uiState.value.voiceState
    when (currentState) {
      KonnectVoiceState.Idle -> {
        _uiState.value = _uiState.value.copy(
          voiceState = KonnectVoiceState.Listening,
          voiceTranscript = "Listening to inquiry...",
          errorMessage = null
        )
      }
      KonnectVoiceState.Listening -> {
        _uiState.value = _uiState.value.copy(
          voiceState = KonnectVoiceState.Processing,
          voiceTranscript = "Transcribing audio input..."
        )
        viewModelScope.launch {
          delay(1100)
          val transcribed = "My Jetpack Compose screen becomes slow when displaying a large list from Firebase."
          _uiState.value = _uiState.value.copy(
            voiceState = KonnectVoiceState.Complete,
            voiceTranscript = transcribed,
            title = if (_uiState.value.title.isBlank()) transcribed else _uiState.value.title,
            description = if (_uiState.value.description.isBlank())
              "When observing the query snapshot from Firestore, scrolling through 200+ list items exhibits significant frame drops and recomposition spikes."
            else _uiState.value.description
          )
        }
      }
      KonnectVoiceState.Processing -> {
        _uiState.value = _uiState.value.copy(voiceState = KonnectVoiceState.Idle)
      }
      KonnectVoiceState.Complete -> {
        _uiState.value = _uiState.value.copy(voiceState = KonnectVoiceState.Idle)
      }
    }
  }

  fun submitQuestion(onSuccess: (String) -> Unit) {
    val current = _uiState.value
    if (current.title.isBlank() && current.description.isBlank()) {
      _uiState.value = current.copy(errorMessage = "Please enter a question or problem description.")
      return
    }

    val finalTitle = if (current.title.isNotBlank()) current.title.trim() else current.description.take(80).trim()
    val finalDesc = if (current.description.isNotBlank()) current.description.trim() else finalTitle

    _uiState.value = current.copy(isSubmitting = true, errorMessage = null)

    viewModelScope.launch {
      val questionId = "q-${System.currentTimeMillis()}"

      // Perform structured technical analysis
      val analysis = aiService.analyzeQuestionWithContext(
        text = "$finalTitle $finalDesc",
        imageUri = current.imageUri,
        category = current.selectedCategory,
        memoryContext = current.memoryContext
      )

      val question = Question(
        id = questionId,
        title = finalTitle,
        description = finalDesc,
        authorId = "u-primary",
        authorName = "Alex",
        category = analysis.category,
        technologies = analysis.technologies,
        tags = analysis.keywords,
        imageUrl = current.imageUri,
        stage = QuestionLifecycleStage.UNDERSTAND,
        matchedBuilderCount = 3,
        replyCount = 0,
        timeAgo = "Just now",
        originalText = "$finalTitle $finalDesc".trim(),
        normalizedText = "$finalTitle $finalDesc".trim().lowercase(),
        summary = analysis.summary,
        difficulty = analysis.difficulty,
        keywords = analysis.keywords,
        memoryContextId = current.memoryContext?.memoryId,
        status = "OPEN"
      )

      communityRepository.submitQuestion(question)

      _uiState.value = _uiState.value.copy(
        isSubmitting = false,
        submittedQuestionId = questionId
      )
      onSuccess(questionId)
    }
  }

  fun resetState() {
    _uiState.value = AskUiState()
  }
}
