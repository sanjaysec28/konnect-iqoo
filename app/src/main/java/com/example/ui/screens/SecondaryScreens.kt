package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BuilderProfile
import com.example.data.model.Discussion
import com.example.data.model.DiscussionReply
import com.example.data.model.MemoryItem
import com.example.data.model.NotificationItem
import com.example.data.model.Question
import com.example.ui.components.KonnectAICore
import com.example.ui.components.KonnectAICoreState
import com.example.ui.components.KonnectAISuccess
import com.example.ui.components.KonnectAvatar
import com.example.ui.components.KonnectAvatarSize
import com.example.ui.components.KonnectFloatingObject
import com.example.ui.components.KonnectHeroCard
import com.example.ui.components.KonnectObjectType
import com.example.ui.components.KonnectPill
import com.example.ui.components.KonnectPillVariant
import com.example.ui.components.KonnectPrimaryButton
import com.example.ui.components.KonnectSecondaryButton
import com.example.ui.components.KonnectTextField
import com.example.ui.components.KonnectTopBar
import com.example.ui.components.Konnect3DCard
import com.example.ui.theme.KonnectBackground
import com.example.ui.theme.KonnectBadgeSuccess
import com.example.ui.theme.KonnectBorder
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondarySurface
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectShapes
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography
import com.example.viewmodel.AIWorkflowViewModel
import com.example.viewmodel.CommunityViewModel

/**
 * 3. AI PROCESSING SCREEN (Spatial Cognitive Pipeline)
 */
@Composable
fun AIProcessingScreen(
  viewModel: AIWorkflowViewModel,
  questionId: String,
  onBack: () -> Unit,
  onNavigateToResults: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler(onBack = onBack)
  val uiState by viewModel.uiState.collectAsState()

  LaunchedEffect(questionId) {
    viewModel.startProcessing(questionId) {
      onNavigateToResults()
    }
  }

  val coreState = when (uiState.processingStage) {
    1 -> KonnectAICoreState.THINKING
    2 -> KonnectAICoreState.FINDING_PEOPLE
    3 -> KonnectAICoreState.FINDING_DISCUSSIONS
    else -> KonnectAICoreState.SUCCESS
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(KonnectBackground)
      .testTag("ai_processing_screen")
  ) {
    KonnectTopBar(
      title = "Cognitive Engine",
      subtitle = "Dissecting architectural boundaries",
      navigationIcon = {
        IconButton(onClick = onBack) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = KonnectPrimaryText
          )
        }
      }
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = KonnectSpacing.screenHorizontal),
      verticalArrangement = Arrangement.spacedBy(KonnectSpacing.l)
    ) {
      // 1. Central Spatial Processing Environment
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
              drawRoundRect(
                color = Color(0x0C111111),
                topLeft = Offset(0f, 6.dp.toPx()),
                size = Size(size.width, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(26.dp.toPx(), 26.dp.toPx())
              )
            }
            .shadow(
              elevation = 6.dp,
              shape = RoundedCornerShape(26.dp),
              ambientColor = Color(0x10171717),
              spotColor = Color(0x18171717)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(KonnectPrimarySurface)
            .drawBehind {
              drawRect(
                brush = Brush.verticalGradient(
                  colors = listOf(KonnectHighlight.copy(alpha = 0.95f), Color.Transparent),
                  startY = 0f,
                  endY = 5.dp.toPx()
                )
              )
            }
            .border(1.dp, Color(0x16111111), RoundedCornerShape(26.dp))
            .padding(KonnectSpacing.cardPaddingHero),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            KonnectPill(
              text = "REASONING PIPELINE",
              variant = KonnectPillVariant.Primary
            )
            Spacer(modifier = Modifier.height(KonnectSpacing.m))

            // Center: Signature AI Core
            KonnectAICore(
              size = 100.dp,
              state = coreState,
              testTag = "processing_ai_core"
            )

            Spacer(modifier = Modifier.height(KonnectSpacing.m))

            Text(
              text = uiState.statusMessage,
              style = Typography.titleLarge,
              color = KonnectPrimaryText
            )
            Spacer(modifier = Modifier.height(KonnectSpacing.xxs))
            Text(
              text = uiState.subMessage,
              style = Typography.bodySmall,
              color = KonnectSecondaryText
            )
          }
        }
      }

      // 2. 3 Connected Spatial Stages
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
              drawRoundRect(
                color = Color(0x0A111111),
                topLeft = Offset(0f, 4.dp.toPx()),
                size = Size(size.width, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(22.dp.toPx(), 22.dp.toPx())
              )
            }
            .shadow(
              elevation = 4.dp,
              shape = RoundedCornerShape(22.dp),
              ambientColor = Color(0x0E171717),
              spotColor = Color(0x14171717)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(KonnectPrimarySurface)
            .border(1.dp, Color(0x14111111), RoundedCornerShape(22.dp))
            .padding(KonnectSpacing.l)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            Text(
              text = "CONNECTED REASONING STAGES",
              style = Typography.labelSmall.copy(fontSize = 11.sp),
              color = KonnectSecondaryText
            )
            Spacer(modifier = Modifier.height(KonnectSpacing.m))

            SpatialPipelineStage(
              stageNumber = 1,
              title = "UNDERSTAND",
              subtitle = if (uiState.processingStage >= 1) "Extracted: ${uiState.analysisResult?.technologies?.joinToString(", ") ?: "Android, Systems"}" else "Awaiting neural parse",
              isActive = uiState.processingStage == 1,
              isCompleted = uiState.processingStage > 1
            )

            StageConnectingBridge(isCompleted = uiState.processingStage > 1)

            SpatialPipelineStage(
              stageNumber = 2,
              title = "FIND PEOPLE",
              subtitle = if (uiState.processingStage >= 2) "Matching domain architects from knowledge graph" else "Queued",
              isActive = uiState.processingStage == 2,
              isCompleted = uiState.processingStage > 2
            )

            StageConnectingBridge(isCompleted = uiState.processingStage > 2)

            SpatialPipelineStage(
              stageNumber = 3,
              title = "SEARCH KNOWLEDGE",
              subtitle = if (uiState.processingStage >= 3) "Cross-referencing verified Community Memory" else "Queued",
              isActive = uiState.processingStage == 3,
              isCompleted = uiState.processingStage >= 4
            )

            Spacer(modifier = Modifier.height(KonnectSpacing.xl))

            KonnectPrimaryButton(
              text = "View Connections",
              onClick = onNavigateToResults,
              testTag = "view_results_button"
            )
          }
        }
      }
    }
  }
}

/**
 * 5. AI RESULTS SCREEN (Spatial Result Environment)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIResultsScreen(
  viewModel: AIWorkflowViewModel,
  onBack: () -> Unit,
  onNavigateToDiscussion: (String) -> Unit,
  onNavigateToMemoryDetail: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler(onBack = onBack)
  val uiState by viewModel.uiState.collectAsState()
  val sheetState = rememberModalBottomSheetState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(KonnectBackground)
      .testTag("ai_results_screen")
  ) {
    KonnectTopBar(
      title = "AI Results",
      subtitle = "Found people, discussions & memory",
      navigationIcon = {
        IconButton(onClick = onBack) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = KonnectPrimaryText
          )
        }
      }
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = KonnectSpacing.screenHorizontal),
      verticalArrangement = Arrangement.spacedBy(KonnectSpacing.l)
    ) {
      // Spatial Header
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
              drawRoundRect(
                color = Color(0x0C111111),
                topLeft = Offset(0f, 6.dp.toPx()),
                size = Size(size.width, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(26.dp.toPx(), 26.dp.toPx())
              )
            }
            .shadow(
              elevation = 6.dp,
              shape = RoundedCornerShape(26.dp),
              ambientColor = Color(0x10171717),
              spotColor = Color(0x18171717)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(KonnectPrimarySurface)
            .border(1.dp, Color(0x16111111), RoundedCornerShape(26.dp))
            .padding(KonnectSpacing.cardPaddingHero)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              KonnectPill(
                text = "STEP 3: KONNECT",
                variant = KonnectPillVariant.Primary
              )
              Spacer(modifier = Modifier.height(KonnectSpacing.s))
              Text(
                text = "We found people and knowledge that can help.",
                style = Typography.headlineLarge.copy(fontSize = 22.sp),
                color = KonnectPrimaryText
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Matched architects, active discussions, and verified collective memories.",
                style = Typography.bodyMedium,
                color = KonnectSecondaryText
              )
            }

            Spacer(modifier = Modifier.width(KonnectSpacing.m))

            KonnectAICore(
              size = 72.dp,
              state = KonnectAICoreState.SUCCESS,
              testTag = "results_ai_core"
            )
          }
        }
      }

      // SECTION 1: PEOPLE (Dimensional Avatars)
      item {
        Text(
          text = "Architects",
          style = Typography.titleLarge,
          color = KonnectPrimaryText
        )
      }

      items(uiState.matchedBuilders.take(1), key = { it.id }) { builder ->
        PersonMatchCard(
          builder = builder,
          isPrimaryMatch = true,
          isSent = uiState.connectionSentForId == builder.id,
          onConnect = { viewModel.openConnectSheet(builder) }
        )
      }

      items(uiState.matchedBuilders.drop(1), key = { it.id }) { builder ->
        PersonMatchCard(
          builder = builder,
          isPrimaryMatch = false,
          isSent = uiState.connectionSentForId == builder.id,
          onConnect = { viewModel.openConnectSheet(builder) }
        )
      }

      // SECTION 2: DISCUSSIONS (Dimensional Knowledge Objects)
      item {
        Text(
          text = "Discussions",
          style = Typography.titleLarge,
          color = KonnectPrimaryText
        )
      }

      items(uiState.matchedDiscussions, key = { it.id }) { question ->
        DiscussionMatchCard(
          question = question,
          onView = { onNavigateToDiscussion(question.id) }
        )
      }

      // SECTION 3: COMMUNITY MEMORY (Knowledge Capsule Objects)
      item {
        Text(
          text = "Community Memory",
          style = Typography.titleLarge,
          color = KonnectPrimaryText
        )
      }

      items(uiState.matchedMemories, key = { it.id }) { memory ->
        MemoryMatchCard(
          memory = memory,
          onRead = { onNavigateToMemoryDetail(memory.id) }
        )
      }

      item {
        Spacer(modifier = Modifier.height(KonnectSpacing.xxxl))
      }
    }
  }

  // 7. Spatial Connection Bottom Sheet
  if (uiState.isConnectingBuilder != null) {
    val builder = uiState.isConnectingBuilder!!
    ModalBottomSheet(
      onDismissRequest = { viewModel.closeConnectSheet() },
      sheetState = sheetState,
      containerColor = KonnectPrimarySurface,
      shape = KonnectShapes.BottomSheet
    ) {
      ConnectBottomSheetContent(
        builder = builder,
        sharedTopic = uiState.question?.title ?: "Android Architecture Inquiry",
        onSendRequest = { topic ->
          viewModel.sendConnectRequest(builder, topic)
        }
      )
    }
  }
}

/**
 * 6. DISCUSSION DETAIL SCREEN (Layered Conversation Environment)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionScreen(
  viewModel: AIWorkflowViewModel,
  questionId: String,
  onBack: () -> Unit,
  onNavigateToSummary: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler(onBack = onBack)
  val uiState by viewModel.uiState.collectAsState()
  var followUpText by remember { mutableStateOf("") }

  LaunchedEffect(questionId) {
    viewModel.loadDiscussion(questionId)
  }

  val discussion = uiState.currentDiscussion
  val question = discussion?.question

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(KonnectBackground)
      .testTag("discussion_screen")
  ) {
    KonnectTopBar(
      title = "Discussion Space",
      subtitle = question?.category ?: "Inquiry",
      navigationIcon = {
        IconButton(onClick = onBack) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = KonnectPrimaryText
          )
        }
      }
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = KonnectSpacing.screenHorizontal),
      verticalArrangement = Arrangement.spacedBy(KonnectSpacing.l)
    ) {
      // Depth Level 1: Original Question (Strongest visual depth)
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
              drawRoundRect(
                color = Color(0x10111111),
                topLeft = Offset(0f, 6.dp.toPx()),
                size = Size(size.width, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
              )
            }
            .shadow(
              elevation = 6.dp,
              shape = RoundedCornerShape(24.dp),
              ambientColor = Color(0x14171717),
              spotColor = Color(0x1C171717)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(KonnectPrimarySurface)
            .border(1.dp, Color(0x20111111), RoundedCornerShape(24.dp))
            .padding(KonnectSpacing.cardPaddingHero)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                KonnectPill(text = question?.category ?: "Android", variant = KonnectPillVariant.Primary)
                question?.technologies?.forEach { tech ->
                  KonnectPill(text = tech, variant = KonnectPillVariant.Secondary)
                }
              }
              Text(
                text = question?.timeAgo ?: "Active",
                style = Typography.labelSmall,
                color = KonnectSecondaryText
              )
            }

            Spacer(modifier = Modifier.height(KonnectSpacing.m))

            Text(
              text = question?.title ?: "Technical Inquiry",
              style = Typography.headlineMedium.copy(fontSize = 20.sp),
              color = KonnectPrimaryText
            )

            Spacer(modifier = Modifier.height(KonnectSpacing.s))

            Text(
              text = question?.description ?: "",
              style = Typography.bodyMedium,
              color = KonnectPrimaryText
            )

            Spacer(modifier = Modifier.height(KonnectSpacing.m))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                KonnectAvatar(initials = question?.authorName ?: "Builder", size = KonnectAvatarSize.Small)
                Spacer(modifier = Modifier.width(KonnectSpacing.xs))
                Text(
                  text = "Posed by ${question?.authorName ?: "Builder"}",
                  style = Typography.labelSmall,
                  color = KonnectSecondaryText
                )
              }

              KonnectSecondaryButton(
                text = "Summarize with AI",
                leadingIcon = Icons.Default.AutoAwesome,
                fullWidth = false,
                onClick = {
                  if (discussion != null) {
                    viewModel.generateSummaryForDiscussion(discussion)
                  }
                  onNavigateToSummary()
                },
                testTag = "summarize_with_ai_button"
              )
            }
          }
        }
      }

      // Community Responses Header
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Responses",
            style = Typography.titleLarge,
            color = KonnectPrimaryText
          )
          Text(
            text = "${discussion?.replies?.size ?: 0} contributions",
            style = Typography.labelSmall,
            color = KonnectSecondaryText
          )
        }
      }

      // Depth Level 2: Community Replies
      items(discussion?.replies ?: emptyList(), key = { it.id }) { reply ->
        ReplyCard(
          reply = reply,
          onConnect = {
            val builder = BuilderProfile(
              id = reply.authorId,
              name = reply.authorName,
              title = reply.authorRole,
              bio = "Active architect participant in inquiry.",
              role = reply.authorRole,
              domains = listOf(question?.category ?: "Android")
            )
            viewModel.openConnectSheet(builder)
          }
        )
      }

      // Follow-up Input Card
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
              drawRoundRect(
                color = Color(0x08111111),
                topLeft = Offset(0f, 4.dp.toPx()),
                size = Size(size.width, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(20.dp.toPx(), 20.dp.toPx())
              )
            }
            .shadow(4.dp, RoundedCornerShape(20.dp), ambientColor = Color(0x0E171717), spotColor = Color(0x14171717))
            .clip(RoundedCornerShape(20.dp))
            .background(KonnectPrimarySurface)
            .border(1.dp, Color(0x14111111), RoundedCornerShape(20.dp))
            .padding(KonnectSpacing.l)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            Text(
              text = "CONTRIBUTE BENCHMARK OR FOLLOW-UP",
              style = Typography.labelSmall.copy(fontSize = 11.sp),
              color = KonnectSecondaryText
            )
            Spacer(modifier = Modifier.height(KonnectSpacing.xs))

            KonnectTextField(
              value = followUpText,
              onValueChange = { followUpText = it },
              placeholder = "Add your benchmark, mitigation, or follow-up question...",
              singleLine = false,
              minHeight = 80.dp,
              testTag = "follow_up_input"
            )

            Spacer(modifier = Modifier.height(KonnectSpacing.m))

            KonnectPrimaryButton(
              text = "Post Follow-Up",
              leadingIcon = Icons.Default.Send,
              onClick = {
                if (followUpText.isNotBlank()) {
                  viewModel.addReply(questionId, followUpText)
                  followUpText = ""
                }
              },
              testTag = "post_follow_up_button"
            )
          }
        }
        Spacer(modifier = Modifier.height(KonnectSpacing.xxxl))
      }
    }
  }

  // Connect bottom sheet
  if (uiState.isConnectingBuilder != null) {
    val builder = uiState.isConnectingBuilder!!
    ModalBottomSheet(
      onDismissRequest = { viewModel.closeConnectSheet() },
      containerColor = KonnectPrimarySurface,
      shape = KonnectShapes.BottomSheet
    ) {
      ConnectBottomSheetContent(
        builder = builder,
        sharedTopic = question?.title ?: "Inquiry",
        onSendRequest = { topic ->
          viewModel.sendConnectRequest(builder, topic)
        }
      )
    }
  }
}

/**
 * 8. AI SUMMARY & MEMORY CAPSULE SCREEN (Transforming Solved Inquiry to Memory)
 */
@Composable
fun AISummaryScreen(
  viewModel: AIWorkflowViewModel,
  onBack: () -> Unit,
  onSaveToMemory: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler(onBack = onBack)
  val uiState by viewModel.uiState.collectAsState()
  val summary = uiState.aiSummary
  val discussion = uiState.currentDiscussion

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(KonnectBackground)
      .testTag("ai_summary_screen")
  ) {
    KonnectTopBar(
      title = "Solution Synthesis",
      subtitle = "Transforming into Memory Capsule",
      navigationIcon = {
        IconButton(onClick = onBack) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = KonnectPrimaryText
          )
        }
      }
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = KonnectSpacing.screenHorizontal),
      verticalArrangement = Arrangement.spacedBy(KonnectSpacing.l)
    ) {
      // 1. Memory Capsule Header with Visual Transformation Indicator
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
              drawRoundRect(
                color = Color(0x0C111111),
                topLeft = Offset(0f, 6.dp.toPx()),
                size = Size(size.width, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(26.dp.toPx(), 26.dp.toPx())
              )
            }
            .shadow(
              elevation = 6.dp,
              shape = RoundedCornerShape(26.dp),
              ambientColor = Color(0x10171717),
              spotColor = Color(0x18171717)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(KonnectPrimarySurface)
            .border(1.dp, Color(0x16111111), RoundedCornerShape(26.dp))
            .padding(KonnectSpacing.cardPaddingHero)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                KonnectPill(
                  text = "STEP 5: REMEMBER",
                  variant = KonnectPillVariant.Primary
                )
                Spacer(modifier = Modifier.height(KonnectSpacing.s))
                Text(
                  text = "Synthesized Resolution",
                  style = Typography.headlineLarge.copy(fontSize = 22.sp),
                  color = KonnectPrimaryText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "Consensus distilled into a verified Community Memory Capsule.",
                  style = Typography.bodyMedium,
                  color = KonnectSecondaryText
                )
              }

              Spacer(modifier = Modifier.width(KonnectSpacing.m))

              KonnectAICore(
                size = 72.dp,
                state = KonnectAICoreState.SUCCESS,
                testTag = "summary_ai_core"
              )
            }

            Spacer(modifier = Modifier.height(KonnectSpacing.m))

            // Visual transformation flow indicator
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFEAEAEC))
                .padding(horizontal = 12.dp, vertical = 8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "DISCUSSION", style = Typography.labelSmall.copy(fontSize = 10.sp), color = KonnectSecondaryText)
              Text(text = "→", style = Typography.labelSmall, color = KonnectSecondaryText)
              Text(text = "SOLUTION", style = Typography.labelSmall.copy(fontSize = 10.sp), color = KonnectPrimaryDarkAction)
              Text(text = "→", style = Typography.labelSmall, color = KonnectSecondaryText)
              Text(text = "MEMORY CAPSULE", style = Typography.labelSmall.copy(fontSize = 10.sp), color = KonnectPrimaryText)
            }
          }
        }
      }

      if (uiState.isSavedToMemory) {
        item {
          KonnectAISuccess(
            title = "Saved to Community Memory",
            summary = "This inquiry is now sealed as an architectural knowledge capsule. Other builders encountering this challenge will receive this verified resolution instantly."
          )
        }
      }

      // 2. Dedicated Physical Solution Capsule
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
              drawRoundRect(
                color = Color(0x0C111111),
                topLeft = Offset(0f, 6.dp.toPx()),
                size = Size(size.width, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
              )
            }
            .shadow(5.dp, RoundedCornerShape(24.dp), ambientColor = Color(0x10171717), spotColor = Color(0x16171717))
            .clip(RoundedCornerShape(24.dp))
            .background(KonnectPrimarySurface)
            .border(1.dp, Color(0x14111111), RoundedCornerShape(24.dp))
            .padding(KonnectSpacing.l)
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            // Layer 1: Problem Definition
            Text(text = "PROBLEM DEFINITION", style = Typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 0.5.sp), color = KonnectSecondaryText)
            Spacer(modifier = Modifier.height(KonnectSpacing.xxs))
            Text(
              text = summary?.problem ?: discussion?.question?.title ?: "React Native camera permission fails on Android 15",
              style = Typography.titleMedium,
              color = KonnectPrimaryText
            )

            Spacer(modifier = Modifier.height(KonnectSpacing.m))

            // Layer 2: Key Insight (Highlighted Substrate Plate)
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE8E9E6))
                .border(1.dp, Color(0x18111111), RoundedCornerShape(16.dp))
                .padding(KonnectSpacing.m)
            ) {
              Column {
                Text(text = "KEY ARCHITECTURAL INSIGHT", style = Typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 0.5.sp), color = KonnectPrimaryDarkAction)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = summary?.keyInsight ?: "Android 15 mandates foregroundServiceType='camera' before binding CameraX lifecycle.",
                  style = Typography.bodyLarge,
                  color = KonnectPrimaryText
                )
              }
            }

            Spacer(modifier = Modifier.height(KonnectSpacing.m))

            // Layer 3: Verified Solution Steps
            Text(text = "VERIFIED RESOLUTION STEPS", style = Typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 0.5.sp), color = KonnectSecondaryText)
            Spacer(modifier = Modifier.height(KonnectSpacing.s))

            (summary?.solutionSteps ?: listOf(
              "1. Declare FOREGROUND_SERVICE_CAMERA permission in AndroidManifest.xml.",
              "2. Set android:foregroundServiceType=\"camera\" in the Service element.",
              "3. Request runtime camera permission before calling ProcessCameraProvider.bindToLifecycle()."
            )).forEach { step ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
              ) {
                Box(
                  modifier = Modifier
                    .size(6.dp)
                    .padding(top = 7.dp)
                    .clip(CircleShape)
                    .background(KonnectPrimaryDarkAction)
                )
                Spacer(modifier = Modifier.width(KonnectSpacing.s))
                Text(
                  text = step,
                  style = Typography.bodyMedium,
                  color = KonnectPrimaryText
                )
              }
            }

            Spacer(modifier = Modifier.height(KonnectSpacing.m))

            // Layer 4: Resources
            Text(text = "COMMUNITY REFERENCES", style = Typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 0.5.sp), color = KonnectSecondaryText)
            Spacer(modifier = Modifier.height(KonnectSpacing.xxs))
            Text(
              text = "• Official Android Documentation: Foreground Services\n• CameraX Core Lifecycle Binding Best Practices",
              style = Typography.bodyMedium,
              color = KonnectSecondaryText
            )
          }
        }
      }

      // Action: Mark Solved & Save to Memory
      item {
        if (!uiState.isSavedToMemory) {
          KonnectPrimaryButton(
            text = "Mark Solved & Save to Memory",
            leadingIcon = Icons.Default.CheckCircle,
            onClick = {
              viewModel.markDiscussionSolvedAndSaveMemory(onSaveToMemory)
            },
            testTag = "mark_solved_save_memory_button"
          )
        } else {
          KonnectSecondaryButton(
            text = "Open Community Memory",
            leadingIcon = Icons.Default.FolderSpecial,
            onClick = onSaveToMemory,
            testTag = "open_memory_after_solved_button"
          )
        }
        Spacer(modifier = Modifier.height(KonnectSpacing.xxxl))
      }
    }
  }
}

/**
 * 13. NOTIFICATIONS SCREEN
 */
@Composable
fun NotificationsScreen(
  viewModel: CommunityViewModel,
  onBack: () -> Unit,
  onNavigateToDiscussion: (String) -> Unit,
  onNavigateToMemory: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler(onBack = onBack)
  val notifications = com.example.data.mock.CommunitySeedData.notifications

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(KonnectBackground)
      .testTag("notifications_screen")
  ) {
    KonnectTopBar(
      title = "Activity Stream",
      subtitle = "Inquiries, responses, and memory archives",
      navigationIcon = {
        IconButton(onClick = onBack) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = KonnectPrimaryText
          )
        }
      }
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = KonnectSpacing.screenHorizontal),
      verticalArrangement = Arrangement.spacedBy(KonnectSpacing.l)
    ) {
      items(notifications, key = { it.id }) { notif ->
        Konnect3DCard(
          modifier = Modifier.fillMaxWidth(),
          onClick = {
            if (notif.type == "memory") {
              onNavigateToMemory(notif.targetId ?: "mem-1")
            } else {
              onNavigateToDiscussion(notif.targetId ?: "q-001")
            }
          },
          testTag = "notification_${notif.id}"
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              // Signal Object Badge
              Box(
                modifier = Modifier
                  .size(34.dp)
                  .clip(CircleShape)
                  .background(
                    if (notif.type == "memory") Color(0xFFE5E6E3)
                    else if (notif.type == "connection") Color(0xFF2C2C2C)
                    else Color(0xFFEAEAEC)
                  ),
                contentAlignment = Alignment.Center
              ) {
                when (notif.type) {
                  "connection" -> {
                    Icon(
                      imageVector = Icons.Default.PersonAdd,
                      contentDescription = null,
                      tint = KonnectHighlight,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                  "memory" -> {
                    Icon(
                      imageVector = Icons.Default.FolderSpecial,
                      contentDescription = null,
                      tint = KonnectPrimaryDarkAction,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                  else -> {
                    Icon(
                      imageVector = Icons.Default.AutoAwesome,
                      contentDescription = null,
                      tint = KonnectPrimaryDarkAction,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.width(KonnectSpacing.m))

              Column {
                Text(
                  text = notif.title,
                  style = Typography.titleSmall,
                  color = KonnectPrimaryText
                )
                Spacer(modifier = Modifier.height(KonnectSpacing.xxs))
                Text(
                  text = notif.message,
                  style = Typography.bodyMedium,
                  color = KonnectSecondaryText
                )
              }
            }

            KonnectPill(text = notif.timeAgo, variant = KonnectPillVariant.Secondary)
          }
        }
      }
      item {
        Spacer(modifier = Modifier.height(KonnectSpacing.xxxl))
      }
    }
  }
}

/**
 * SETTINGS SCREEN
 */
@Composable
fun SettingsScreen(
  onBack: () -> Unit,
  onNavigateToDesignSystem: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  BackHandler(onBack = onBack)

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(KonnectBackground)
      .testTag("settings_screen")
  ) {
    KonnectTopBar(
      title = "Node Settings",
      subtitle = "Configuration & peer routing",
      navigationIcon = {
        IconButton(onClick = onBack) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = KonnectPrimaryText
          )
        }
      }
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = KonnectSpacing.screenHorizontal),
      verticalArrangement = Arrangement.spacedBy(KonnectSpacing.l)
    ) {
      if (onNavigateToDesignSystem != null) {
        item {
          Konnect3DCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToDesignSystem,
            testTag = "settings_design_system_preview_card"
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Design System Showcase",
                  style = Typography.titleMedium,
                  color = KonnectPrimaryText
                )
                Text(
                  text = "Inspect all 16 component sections, tokens, 3D cards, and micro-interactions.",
                  style = Typography.bodySmall,
                  color = KonnectSecondaryText
                )
              }
              KonnectPill(text = "Visual QA", variant = KonnectPillVariant.Primary)
            }
          }
        }
      }

      item {
        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          Text(text = "AI MODEL CONFIGURATION", style = Typography.labelSmall, color = KonnectSecondaryText)
          Spacer(modifier = Modifier.height(KonnectSpacing.m))
          Text(text = "Gemini Flash Runtime", style = Typography.titleMedium, color = KonnectPrimaryText)
          Text(
            text = "Active model: gemini-3.5-flash for real-time question understanding, matching, and solution synthesis.",
            style = Typography.bodyMedium,
            color = KonnectSecondaryText
          )
        }
      }

      item {
        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          Text(text = "COMMUNITY BACKEND", style = Typography.labelSmall, color = KonnectSecondaryText)
          Spacer(modifier = Modifier.height(KonnectSpacing.m))
          Text(text = "Firebase Firestore Architecture", style = Typography.titleMedium, color = KonnectPrimaryText)
          Text(
            text = "Cloud sync & credential manager auth prepared for production multi-user sync.",
            style = Typography.bodyMedium,
            color = KonnectSecondaryText
          )
        }
      }
    }
  }
}

// Supporting Helper Composables
@Composable
private fun SpatialPipelineStage(
  stageNumber: Int,
  title: String,
  subtitle: String,
  isActive: Boolean,
  isCompleted: Boolean
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .size(32.dp)
        .shadow(
          elevation = if (isActive || isCompleted) 4.dp else 0.dp,
          shape = CircleShape,
          ambientColor = Color(0x20111111),
          spotColor = Color(0x30111111)
        )
        .clip(CircleShape)
        .background(
          if (isCompleted) KonnectBadgeSuccess
          else if (isActive) KonnectPrimaryDarkAction
          else KonnectSecondarySurface
        )
        .border(1.dp, Color(0x20FFFFFF), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      if (isCompleted) {
        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = KonnectHighlight, modifier = Modifier.size(16.dp))
      } else {
        Text(
          text = "$stageNumber",
          style = Typography.labelMedium,
          color = if (isActive) KonnectHighlight else KonnectPrimaryText
        )
      }
    }
    Spacer(modifier = Modifier.width(KonnectSpacing.m))
    Column {
      Text(
        text = title,
        style = Typography.titleSmall,
        color = if (isActive) KonnectPrimaryDarkAction else KonnectPrimaryText
      )
      Text(
        text = subtitle,
        style = Typography.bodySmall,
        color = KonnectSecondaryText
      )
    }
  }
}

@Composable
private fun StageConnectingBridge(isCompleted: Boolean) {
  Box(
    modifier = Modifier
      .padding(start = 15.dp, top = 2.dp, bottom = 2.dp)
      .width(2.dp)
      .height(20.dp)
      .background(if (isCompleted) KonnectBadgeSuccess else Color(0x20111111))
  )
}

@Composable
private fun PersonMatchCard(
  builder: BuilderProfile,
  isSent: Boolean,
  isPrimaryMatch: Boolean = false,
  onConnect: () -> Unit
) {
  var isExpanded by remember { mutableStateOf(false) }

  Konnect3DCard(
    modifier = Modifier.fillMaxWidth(),
    elevation = if (isPrimaryMatch) 6.dp else 3.dp,
    testTag = "person_match_${builder.id}"
  ) {
    if (isPrimaryMatch) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = KonnectSpacing.xs),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        KonnectPill(
          text = "DIRECT DOMAIN MATCH",
          variant = KonnectPillVariant.Primary
        )
        Text(
          text = "${builder.reputationScore} reputation",
          style = Typography.labelSmall,
          color = KonnectSecondaryText
        )
      }
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        KonnectAvatar(initials = builder.name, size = KonnectAvatarSize.Medium, isOnline = builder.isOnline, relevanceScore = builder.relevanceScore)
        Spacer(modifier = Modifier.width(KonnectSpacing.m))
        Column {
          Text(text = builder.name, style = Typography.titleMedium, color = KonnectPrimaryText)
          Text(text = builder.title, style = Typography.labelSmall, color = KonnectSecondaryText)
        }
      }

      if (isSent) {
        KonnectPill(text = "Request sent", variant = KonnectPillVariant.Secondary)
      } else {
        KonnectSecondaryButton(
          text = "Connect",
          leadingIcon = Icons.Default.PersonAdd,
          fullWidth = false,
          onClick = onConnect,
          testTag = "connect_btn_${builder.id}"
        )
      }
    }

    Spacer(modifier = Modifier.height(KonnectSpacing.s))
    Text(
      text = builder.skills.joinToString(" • "),
      style = Typography.labelSmall,
      color = KonnectPrimaryDarkAction
    )
    Spacer(modifier = Modifier.height(KonnectSpacing.xxs))
    Text(
      text = "\"${builder.contributionSummary}\"",
      style = Typography.bodyMedium,
      color = KonnectSecondaryText
    )

    Spacer(modifier = Modifier.height(KonnectSpacing.s))

    // Interactive "Why this person?" trigger
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .background(if (isExpanded) Color(0xFFE8E9E6) else Color(0xFFF2F3F0))
        .border(1.dp, Color(0x14111111), RoundedCornerShape(12.dp))
        .clickable { isExpanded = !isExpanded }
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .fillMaxWidth()
    ) {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = KonnectPrimaryDarkAction,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "WHY THIS ARCHITECT?",
              style = Typography.labelSmall.copy(fontSize = 11.sp, letterSpacing = 0.5.sp),
              color = KonnectPrimaryDarkAction
            )
          }

          Text(
            text = if (isExpanded) "Hide" else "Explain match",
            style = Typography.labelSmall.copy(fontSize = 11.sp),
            color = KonnectSecondaryText
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = builder.whyMatched ?: "Strong domain overlap and active architectural contributions.",
          style = Typography.bodySmall,
          color = KonnectPrimaryText
        )

        if (isExpanded) {
          Spacer(modifier = Modifier.height(KonnectSpacing.m))

          // Match Strength Visualization
          Text(
            text = "DOMAIN MATCH STRENGTH",
            style = Typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 0.5.sp),
            color = KonnectSecondaryText
          )
          Spacer(modifier = Modifier.height(6.dp))

          builder.skills.take(2).forEachIndexed { idx, skill ->
            val strength = if (idx == 0) 0.95f else 0.88f
            Column(modifier = Modifier.padding(bottom = 6.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = skill, style = Typography.labelSmall, color = KonnectPrimaryText)
                Text(text = "${(strength * 100).toInt()}% match", style = Typography.labelSmall, color = KonnectSecondaryText)
              }
              Spacer(modifier = Modifier.height(2.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(4.dp)
                  .clip(RoundedCornerShape(2.dp))
                  .background(Color(0xFFDDDDD9))
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth(strength)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(KonnectPrimaryDarkAction)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "• ${builder.solvedQuestionsCount} solved architecture inquiries in community archive",
            style = Typography.labelSmall,
            color = KonnectSecondaryText
          )
        }
      }
    }
  }
}

@Composable
private fun DiscussionMatchCard(question: Question, onView: () -> Unit) {
  Konnect3DCard(modifier = Modifier.fillMaxWidth(), onClick = onView, testTag = "disc_match_${question.id}") {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      KonnectPill(text = question.category, variant = KonnectPillVariant.Primary)
      Text(text = "${question.replyCount} replies", style = Typography.labelSmall, color = KonnectSecondaryText)
    }
    Spacer(modifier = Modifier.height(KonnectSpacing.xs))
    Text(text = question.title, style = Typography.titleSmall, color = KonnectPrimaryText)
    
    Spacer(modifier = Modifier.height(KonnectSpacing.xs))
    Text(
      text = "WHY THIS DISCUSSION?",
      style = Typography.labelSmall.copy(fontSize = 11.sp),
      color = KonnectSecondaryText
    )
    Text(
      text = question.whyMatched ?: "Similar technical challenge in ${question.category}.",
      style = Typography.bodySmall,
      color = KonnectSecondaryText
    )

    Spacer(modifier = Modifier.height(KonnectSpacing.s))
    KonnectSecondaryButton(text = "View discussion", fullWidth = false, onClick = onView)
  }
}

@Composable
private fun MemoryMatchCard(memory: MemoryItem, onRead: () -> Unit) {
  Konnect3DCard(modifier = Modifier.fillMaxWidth(), onClick = onRead, testTag = "mem_match_${memory.id}") {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = "Verified Knowledge", style = Typography.labelSmall, color = KonnectSecondaryText)
      KonnectPill(text = "${memory.discussionCount} discussions", variant = KonnectPillVariant.Secondary)
    }
    Spacer(modifier = Modifier.height(KonnectSpacing.xs))
    Text(text = memory.title, style = Typography.titleSmall, color = KonnectPrimaryText)

    Spacer(modifier = Modifier.height(KonnectSpacing.xs))
    Text(
      text = "WHY THIS MEMORY?",
      style = Typography.labelSmall.copy(fontSize = 11.sp),
      color = KonnectSecondaryText
    )
    Text(
      text = memory.whyMatched ?: "Contains a previously verified solution for this architectural domain.",
      style = Typography.bodySmall,
      color = KonnectSecondaryText
    )

    Spacer(modifier = Modifier.height(KonnectSpacing.s))
    KonnectSecondaryButton(text = "Read solution", fullWidth = false, onClick = onRead)
  }
}

@Composable
private fun ReplyCard(reply: DiscussionReply, onConnect: () -> Unit) {
  Konnect3DCard(
    modifier = Modifier.fillMaxWidth(),
    backgroundColor = if (reply.isHelpful) KonnectPrimarySurface else KonnectSecondarySurface,
    showHighlight = reply.isHelpful,
    testTag = "reply_card_${reply.id}"
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        KonnectAvatar(initials = reply.authorName, size = KonnectAvatarSize.Small)
        Spacer(modifier = Modifier.width(KonnectSpacing.xs))
        Column {
          Text(text = reply.authorName, style = Typography.titleSmall, color = KonnectPrimaryText)
          Text(text = reply.authorRole, style = Typography.labelSmall, color = KonnectSecondaryText)
        }
      }
      if (reply.isHelpful) {
        KonnectPill(text = "Most Helpful", variant = KonnectPillVariant.Primary)
      } else {
        Text(text = reply.timeAgo, style = Typography.labelSmall, color = KonnectSecondaryText)
      }
    }

    Spacer(modifier = Modifier.height(KonnectSpacing.m))

    Text(text = reply.content, style = Typography.bodyMedium, color = KonnectPrimaryText)

    Spacer(modifier = Modifier.height(KonnectSpacing.m))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = Icons.Default.ThumbUp, contentDescription = null, tint = KonnectSecondaryText, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(KonnectSpacing.xxs))
        Text(text = "${reply.helpfulVotes} helpful", style = Typography.labelSmall, color = KonnectSecondaryText)
      }

      KonnectSecondaryButton(
        text = "Connect with this person",
        fullWidth = false,
        onClick = onConnect,
        testTag = "connect_person_${reply.id}"
      )
    }
  }
}

/**
 * 13. SPATIAL CONNECTION BRIDGE EXPERIENCE
 * Shows: AVATAR ──[ Animated Connection Bridge ]── AVATAR
 */
@Composable
private fun ConnectBottomSheetContent(
  builder: BuilderProfile,
  sharedTopic: String,
  onSendRequest: (String) -> Unit
) {
  var isSent by remember { mutableStateOf(false) }

  val infiniteTransition = rememberInfiniteTransition(label = "bridge_pulse")
  val bridgePulse by infiniteTransition.animateFloat(
    initialValue = 0.3f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "bridge_alpha"
  )

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(KonnectSpacing.cardPaddingHero)
  ) {
    Text(
      text = "Form Builder Connection",
      style = Typography.headlineMedium,
      color = KonnectPrimaryText
    )

    Spacer(modifier = Modifier.height(KonnectSpacing.l))

    // Visual Connection Bridge: Alex (Primary) ── Bridge ── Receiver (Builder)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Alex Node
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        KonnectAvatar(initials = "Alex", size = KonnectAvatarSize.Medium, isOnline = true)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "You (Alex)", style = Typography.labelSmall, color = KonnectSecondaryText)
      }

      // Animated Bridge Filaments
      Box(
        modifier = Modifier
          .width(100.dp)
          .height(30.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val start = Offset(8.dp.toPx(), size.height / 2f)
          val end = Offset(size.width - 8.dp.toPx(), size.height / 2f)

          // Background track
          drawLine(
            color = Color(0x18111111),
            start = start,
            end = end,
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
          )

          // Animated pulsing bridge
          drawLine(
            color = KonnectPrimaryDarkAction.copy(alpha = bridgePulse),
            start = start,
            end = end,
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
          )

          // Center spark
          drawCircle(
            color = KonnectHighlight,
            radius = 3.dp.toPx(),
            center = Offset(size.width / 2f, size.height / 2f)
          )
        }
      }

      // Receiver Node
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        KonnectAvatar(initials = builder.name, size = KonnectAvatarSize.Medium, isOnline = builder.isOnline)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = builder.name.split(" ").first(), style = Typography.labelSmall, color = KonnectPrimaryText)
      }
    }

    Spacer(modifier = Modifier.height(KonnectSpacing.l))

    Text(
      text = "WHY THIS ARCHITECT",
      style = Typography.labelSmall.copy(fontSize = 11.sp),
      color = KonnectSecondaryText
    )
    Text(
      text = "${builder.name} (${builder.role}) has solved ${builder.solvedQuestionsCount} inquiries across ${builder.domains.joinToString(", ")} with ${builder.reputationScore} reputation.",
      style = Typography.bodyMedium,
      color = KonnectPrimaryText
    )

    Spacer(modifier = Modifier.height(KonnectSpacing.m))

    Text(
      text = "SHARED INQUIRY TOPIC",
      style = Typography.labelSmall.copy(fontSize = 11.sp),
      color = KonnectSecondaryText
    )
    Text(
      text = sharedTopic,
      style = Typography.titleSmall,
      color = KonnectPrimaryDarkAction
    )

    Spacer(modifier = Modifier.height(KonnectSpacing.l))

    if (isSent) {
      KonnectAISuccess(
        title = "Connection Request Sent",
        summary = "Your request was bridged to ${builder.name}. You will be notified immediately when they accept."
      )
    } else {
      KonnectPrimaryButton(
        text = "Send connection request",
        leadingIcon = Icons.Default.PersonAdd,
        onClick = {
          isSent = true
          onSendRequest(sharedTopic)
        },
        testTag = "send_connect_request_button"
      )
    }

    Spacer(modifier = Modifier.height(KonnectSpacing.xl))
  }
}
