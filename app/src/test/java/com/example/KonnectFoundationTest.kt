package com.example

import com.example.data.model.Question
import com.example.data.model.QuestionLifecycleStage
import com.example.data.repository.DefaultAIRepository
import com.example.data.repository.DefaultCommunityRepository
import com.example.data.repository.DefaultMemoryRepository
import com.example.data.repository.DefaultUserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class KonnectFoundationTest {

  @Test
  fun testCommunityRepositoryObservesInitialQuestions() = runTest {
    val repository = DefaultCommunityRepository()
    val questions = repository.observeQuestions().first()
    assertTrue("Should have initial questions", questions.isNotEmpty())
  }

  @Test
  fun testSubmitQuestionAndQuery() = runTest {
    val repository = DefaultCommunityRepository()
    val newQuestion = Question(
      id = "test-q1",
      title = "Benchmarking Android gRPC vs WebSockets",
      description = "Network battery efficiency test",
      authorId = "u-test",
      authorName = "Tester",
      stage = QuestionLifecycleStage.ASK
    )

    repository.submitQuestion(newQuestion)
    val fetched = repository.getQuestionById("test-q1")
    assertNotNull(fetched)
    assertEquals("Benchmarking Android gRPC vs WebSockets", fetched?.title)
  }

  @Test
  fun testAIRepositoryUnderstanding() = runTest {
    val communityRepo = DefaultCommunityRepository()
    val aiRepo = DefaultAIRepository(communityRepo)

    val result = aiRepo.understandQuestion("Optimizing on-device AI model quantization", "Seeking lowest latency")
    assertTrue(result.isSuccess)
    val solution = result.getOrNull()
    assertNotNull(solution)
    assertTrue(solution!!.suggestedDomains.contains("On-Device AI"))
  }

  @Test
  fun testMemoryRepositorySearch() = runTest {
    val memoryRepo = DefaultMemoryRepository()
    val memories = memoryRepo.searchMemories("WebSocket")
    assertEquals(1, memories.size)
  }

  @Test
  fun testUserRepositoryCurrentUser() = runTest {
    val userRepo = DefaultUserRepository()
    val user = userRepo.getCurrentUser()
    assertEquals("Alex", user.name)
  }
}
