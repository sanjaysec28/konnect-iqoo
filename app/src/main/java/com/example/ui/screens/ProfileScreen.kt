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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.KonnectAvatar
import com.example.ui.components.KonnectAvatarSize
import com.example.ui.components.KonnectFloatingObject
import com.example.ui.components.KonnectHeroCard
import com.example.ui.components.KonnectObjectType
import com.example.ui.components.KonnectPill
import com.example.ui.components.KonnectPillVariant
import com.example.ui.components.KonnectSecondaryButton
import com.example.ui.components.Konnect3DCard
import com.example.ui.theme.KonnectBackground
import com.example.ui.theme.KonnectBadgeSuccess
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography
import com.example.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
  viewModel: ProfileViewModel,
  onNavigateToSettings: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val profile = uiState.profile

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(KonnectBackground)
      .testTag("profile_screen")
      .padding(horizontal = KonnectSpacing.screenHorizontal),
    verticalArrangement = Arrangement.spacedBy(KonnectSpacing.l)
  ) {

    // 1. Profile Hero Card
    item {
      Spacer(modifier = Modifier.height(KonnectSpacing.xs))
      KonnectHeroCard(
        title = profile?.name ?: "Alex",
        subtitle = profile?.title ?: "Client Systems Engineer",
        badgeText = "BUILDER NODE #0042",
        floatingObject = {
          KonnectAvatar(
            initials = profile?.name ?: "Alex",
            size = KonnectAvatarSize.Profile,
            isOnline = uiState.isOnline
          )
        }
      )
    }

    // 2. Metrics Counter
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.m)
      ) {
        MetricTile("Questions Asked", "${uiState.questionsAskedCount}", Modifier.weight(1f))
        MetricTile("Problems Solved", "${uiState.problemsSolvedCount}", Modifier.weight(1f))
        MetricTile("Connections", "${uiState.connectionsCount}", Modifier.weight(1f))
      }
    }

    // 3. Section: My Skills
    item {
      Konnect3DCard(
        modifier = Modifier.fillMaxWidth(),
        testTag = "profile_skills_card"
      ) {
        Text(
          text = "MY SKILLS & DOMAINS",
          style = Typography.labelSmall,
          color = KonnectSecondaryText
        )
        Spacer(modifier = Modifier.height(KonnectSpacing.m))

        Row(
          horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.xs),
          modifier = Modifier.fillMaxWidth()
        ) {
          (profile?.skills ?: listOf("Android", "Kotlin", "Compose", "Gemini API", "Room")).forEach { skill ->
            KonnectPill(
              text = skill,
              variant = KonnectPillVariant.Surface
            )
          }
        }

        Spacer(modifier = Modifier.height(KonnectSpacing.m))

        Text(
          text = profile?.bio ?: "Building high-performance mobile client infrastructure, local-first data pipelines, and AI integrations.",
          style = Typography.bodyMedium,
          color = KonnectSecondaryText
        )
      }
    }

    // 4. Section: My Contributions
    item {
      Text(
        text = "My Contributions",
        style = Typography.titleLarge,
        color = KonnectPrimaryText
      )
    }

    items(uiState.recentContributions) { itemText ->
      Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(KonnectPrimaryDarkAction),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = com.example.ui.theme.KonnectHighlight,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(KonnectSpacing.m))
          Text(
            text = itemText,
            style = Typography.bodyMedium,
            color = KonnectPrimaryText
          )
        }
      }
    }

    // 5. Section: Problems I Solved
    item {
      Text(
        text = "Problems I Solved",
        style = Typography.titleLarge,
        color = KonnectPrimaryText
      )
    }

    items(uiState.solvedProblems) { problemTitle ->
      Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = problemTitle,
              style = Typography.titleSmall,
              color = KonnectPrimaryText
            )
            Text(
              text = "Solution verified & archived into Community Memory",
              style = Typography.bodySmall,
              color = KonnectSecondaryText
            )
          }
          KonnectPill(text = "Verified", variant = KonnectPillVariant.Primary)
        }
      }
    }

    // 6. Settings Link Button
    item {
      KonnectSecondaryButton(
        text = "Node Settings & Configuration",
        leadingIcon = Icons.Default.Settings,
        onClick = onNavigateToSettings,
        testTag = "profile_settings_button"
      )
      Spacer(modifier = Modifier.height(KonnectSpacing.xxxl))
    }
  }
}

@Composable
private fun MetricTile(label: String, value: String, modifier: Modifier = Modifier) {
  Konnect3DCard(
    modifier = modifier,
    cornerRadius = 16.dp,
    contentPadding = androidx.compose.foundation.layout.PaddingValues(KonnectSpacing.m)
  ) {
    Text(
      text = label.uppercase(),
      style = Typography.labelSmall,
      color = KonnectSecondaryText
    )
    Spacer(modifier = Modifier.height(KonnectSpacing.xxs))
    Text(
      text = value,
      style = Typography.headlineMedium,
      color = KonnectPrimaryText
    )
  }
}
