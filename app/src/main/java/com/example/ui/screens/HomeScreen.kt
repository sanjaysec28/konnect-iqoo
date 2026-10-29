package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.KonnectAvatar
import com.example.ui.components.KonnectAvatarSize
import com.example.ui.components.KonnectLayeredDiscussions
import com.example.ui.components.KonnectLivingInsight
import com.example.ui.components.KonnectQuestionWorkspace
import com.example.ui.components.KonnectSpatialActionDock
import com.example.ui.theme.KonnectBackground
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography
import com.example.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  onNavigateToAsk: () -> Unit,
  onNavigateToAskWithVoice: () -> Unit,
  onNavigateToDiscussion: (String) -> Unit,
  onNavigateToCommunity: () -> Unit,
  onNavigateToMemory: () -> Unit,
  onNavigateToNotifications: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(KonnectBackground)
      .testTag("home_screen")
      .padding(horizontal = KonnectSpacing.screenHorizontal),
    verticalArrangement = Arrangement.spacedBy(KonnectSpacing.l)
  ) {

    // 1. Top Minimal Greeting & Notification Header
    item {
      Spacer(modifier = Modifier.height(KonnectSpacing.xs))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          KonnectAvatar(
            initials = uiState.userName,
            size = KonnectAvatarSize.Medium,
            isOnline = true,
            testTag = "home_user_avatar"
          )
          Spacer(modifier = Modifier.width(KonnectSpacing.m))
          Column {
            Text(
              text = "${uiState.greeting},",
              style = Typography.labelSmall.copy(fontSize = 11.sp),
              color = KonnectSecondaryText
            )
            Text(
              text = uiState.userName,
              style = Typography.titleLarge.copy(fontSize = 20.sp),
              color = KonnectPrimaryText
            )
          }
        }

        Box(contentAlignment = Alignment.TopEnd) {
          IconButton(
            onClick = onNavigateToNotifications,
            modifier = Modifier.testTag("home_notifications_button")
          ) {
            Icon(
              imageVector = Icons.Default.NotificationsNone,
              contentDescription = "Notifications",
              tint = KonnectPrimaryText,
              modifier = Modifier.size(24.dp)
            )
          }
          if (uiState.unreadNotificationsCount > 0) {
            Box(
              modifier = Modifier
                .padding(top = 8.dp, end = 8.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(KonnectPrimaryDarkAction)
            )
          }
        }
      }
    }

    // 2. ONE Dominant Spatial Hero Environment: Floating Question Workspace
    item {
      KonnectQuestionWorkspace(
        onAskClick = onNavigateToAsk,
        onVoiceClick = onNavigateToAskWithVoice
      )
    }

    // 3. Spatial Action Dock: 3 Independent Floating 3D Objects (Ask, Community, Memory)
    item {
      KonnectSpatialActionDock(
        onAskClick = onNavigateToAsk,
        onCommunityClick = onNavigateToCommunity,
        onMemoryClick = onNavigateToMemory
      )
    }

    // 4. Living Intelligence Data Object: 142 Problems Solved
    item {
      KonnectLivingInsight(
        solvedCount = uiState.questionsSolvedCount
      )
    }

    // 5. Trending / Layered Horizontal Discussions Section Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Trending Inquiries",
          style = Typography.titleLarge,
          color = KonnectPrimaryText
        )
        Text(
          text = "${uiState.recentQuestions.size} active in orbit",
          style = Typography.labelSmall,
          color = KonnectSecondaryText
        )
      }
    }

    // 6. Layered Horizontal Discussion Environment
    item {
      KonnectLayeredDiscussions(
        questions = uiState.recentQuestions,
        onDiscussionClick = onNavigateToDiscussion
      )
    }

    item {
      Spacer(modifier = Modifier.height(KonnectSpacing.xxxl))
    }
  }
}
