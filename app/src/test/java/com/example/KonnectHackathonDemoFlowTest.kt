package com.example

import com.example.data.ai.GeminiAIService
import com.example.data.mock.CommunitySeedData
import com.example.data.model.Discussion
import com.example.data.model.Question
import com.example.data.repository.DefaultCommunityRepository
import com.example.data.repository.DefaultMemoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class KonnectHackathonDemoFlowTest {

  @Test
  fun testHackathonDemoFullCognitiveLoop() = runTest {
    val aiService = GeminiAIService()
    val communityRepo = DefaultCommunityRepository()
    val memoryRepo = DefaultMemoryRepository()

    // 1. Analyze Hackathon question
    val demoInput = "My Jetpack Compose screen becomes slow when displaying a large list from Firebase."
    val analysis = aiService.analyzeQuestion(demoInput)

    assertEquals("Android", analysis.suggestedCategory)
    assertTrue("Should detect Jetpack Compose", analysis.technologies.contains("Jetpack Compose"))
    assertTrue("Should detect Firebase", analysis.technologies.contains("Firebase"))

    // 2. Match People
    val allBuilders = communityRepo.observeBuilders().first()
    val matchedBuilders = aiService.matchPeople(analysis.topics, analysis.technologies, allBuilders)

    assertTrue("Should match architects", matchedBuilders.isNotEmpty())
    val topExpert = matchedBuilders.first()
    assertNotNull("Must provide explainable whyMatched reason", topExpert.whyMatched)
    assertTrue("Why matched must be informative", topExpert.whyMatched!!.isNotEmpty())

    // 3. Match Discussions
    val allQuestions = communityRepo.observeQuestions().first()
    val matchedDiscussions = aiService.matchDiscussions(analysis.topics, demoInput, allQuestions)
    assertTrue("Should match discussions", matchedDiscussions.isNotEmpty())

    // 4. Discussion & AI Summary
    val sampleQuestion = Question(
      id = "demo-q1",
      title = demoInput,
      description = "LazyColumn recomposes excessively during Firestore updates",
      authorId = "u-primary",
      authorName = "Alex",
      category = "Android",
      technologies = listOf("Jetpack Compose", "Firebase")
    )
    communityRepo.submitQuestion(sampleQuestion)

    val discussion = Discussion(
      id = "disc-demo-1",
      questionId = sampleQuestion.id,
      question = sampleQuestion,
      replies = emptyList()
    )

    val summary = aiService.summarizeDiscussion(discussion)
    assertNotNull(summary.problem)
    assertTrue(summary.keyInsight.isNotEmpty())
    assertTrue(summary.solutionSteps.isNotEmpty())

    // 5. Create and Save Memory
    val memory = aiService.createMemory(discussion, summary)
    memoryRepo.saveMemory(memory)

    // 6. Search Memory
    val searchResults = memoryRepo.searchMemories("Compose")
    assertTrue("Newly saved memory should be discoverable", searchResults.any { it.title == demoInput })
  }
}
