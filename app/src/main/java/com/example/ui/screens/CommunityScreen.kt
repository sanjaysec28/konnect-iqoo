package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BuilderProfile
import com.example.data.model.Question
import com.example.ui.components.KonnectAIEmptyState
import com.example.ui.components.KonnectAvatar
import com.example.ui.components.KonnectAvatarSize
import com.example.ui.components.KonnectFloatingObject
import com.example.ui.components.KonnectObjectType
import com.example.ui.components.KonnectPill
import com.example.ui.components.KonnectPillVariant
import com.example.ui.components.KonnectSearchField
import com.example.ui.components.KonnectSecondaryButton
import com.example.ui.components.Konnect3DCard
import com.example.ui.theme.KonnectBackground
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography
import com.example.viewmodel.CommunityViewModel

@Composable
fun CommunityScreen(
  viewModel: CommunityViewModel,
  onQuestionClick: (String) -> Unit,
  onConnectClick: (BuilderProfile) -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(KonnectBackground)
      .testTag("community_screen")
      .padding(horizontal = KonnectSpacing.screenHorizontal),
    verticalArrangement = Arrangement.spacedBy(KonnectSpacing.l)
  ) {

    // 1. Spatial Network Header
    item {
      Spacer(modifier = Modifier.height(KonnectSpacing.xs))
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
          .padding(KonnectSpacing.cardPaddingHero)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            KonnectPill(
              text = "COMMUNITY NETWORK SPACE",
              variant = KonnectPillVariant.Primary
            )
            Spacer(modifier = Modifier.height(KonnectSpacing.s))
            Text(
              text = "Peer Architects",
              style = Typography.headlineLarge.copy(fontSize = 24.sp),
              color = KonnectPrimaryText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Active inquiries and verified engineers collaborating across technical boundaries.",
              style = Typography.bodyMedium,
              color = KonnectSecondaryText
            )
          }

          Spacer(modifier = Modifier.width(KonnectSpacing.m))

          KonnectFloatingObject(
            type = KonnectObjectType.Community,
            size = 64.dp
          )
        }
      }
    }

    // 2. Floating Search Surface
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(4.dp, RoundedCornerShape(22.dp), ambientColor = Color(0x0E171717), spotColor = Color(0x14171717))
          .clip(RoundedCornerShape(22.dp))
          .background(KonnectPrimarySurface)
          .border(1.dp, Color(0x14111111), RoundedCornerShape(22.dp))
          .padding(KonnectSpacing.l)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          KonnectSearchField(
            query = uiState.searchQuery,
            onQueryChange = { viewModel.onSearchQueryChanged(it) },
            placeholder = "Search across problems, architectures, tags...",
            testTag = "community_search_input"
          )

          Spacer(modifier = Modifier.height(KonnectSpacing.m))

          Text(
            text = "FILTER ARCHITECTURAL DOMAIN",
            style = Typography.labelSmall.copy(fontSize = 11.sp),
            color = KonnectSecondaryText
          )
          Spacer(modifier = Modifier.height(KonnectSpacing.xs))

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.xs),
            modifier = Modifier.fillMaxWidth()
          ) {
            items(uiState.availableCategories) { category ->
              val isSelected = category == uiState.selectedCategory
              KonnectPill(
                text = category,
                isSelected = isSelected,
                onClick = { viewModel.onCategorySelected(category) },
                testTag = "community_category_$category"
              )
            }
          }
        }
      }
    }

    // 3. "People Solving Problems" (Connected Network Nodes)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Hub,
            contentDescription = null,
            tint = KonnectPrimaryText,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "People Solving Problems",
            style = Typography.titleMedium,
            color = KonnectPrimaryText
          )
        }
        Text(
          text = "${uiState.builders.size} active in network",
          style = Typography.labelSmall,
          color = KonnectSecondaryText
        )
      }
      Spacer(modifier = Modifier.height(KonnectSpacing.xs))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.m),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(uiState.builders, key = { it.id }) { builder ->
          NetworkArchitectNode(
            builder = builder,
            onConnect = { onConnectClick(builder) }
          )
        }
      }
    }

    // 4. Questions Feed Section Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Connected Inquiries",
          style = Typography.titleLarge,
          color = KonnectPrimaryText
        )
        Text(
          text = "${uiState.filteredQuestions.size} active",
          style = Typography.labelSmall,
          color = KonnectSecondaryText
        )
      }
    }

    // 5. Feed Items
    if (uiState.filteredQuestions.isEmpty()) {
      item {
        KonnectAIEmptyState(
          title = "No Inquiries Found",
          description = "Your next useful conversation starts here.",
          objectType = KonnectObjectType.Community,
          actionText = "Clear Search",
          onActionClick = {
            viewModel.onSearchQueryChanged("")
            viewModel.onCategorySelected("All")
          }
        )
      }
    } else {
      items(uiState.filteredQuestions, key = { it.id }) { question ->
        CommunityQuestionCard(
          question = question,
          onClick = { onQuestionClick(question.id) }
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(KonnectSpacing.xxxl))
    }
  }
}

@Composable
private fun NetworkArchitectNode(
  builder: BuilderProfile,
  onConnect: () -> Unit
) {
  Box(
    modifier = Modifier
      .width(224.dp)
      .drawBehind {
        drawRoundRect(
          color = Color(0x0A111111),
          topLeft = Offset(0f, 4.dp.toPx()),
          size = Size(size.width, size.height),
          cornerRadius = androidx.compose.ui.geometry.CornerRadius(20.dp.toPx(), 20.dp.toPx())
        )
      }
      .shadow(3.dp, RoundedCornerShape(20.dp), ambientColor = Color(0x0E171717), spotColor = Color(0x14171717))
      .clip(RoundedCornerShape(20.dp))
      .background(KonnectPrimarySurface)
      .border(1.dp, Color(0x16111111), RoundedCornerShape(20.dp))
      .padding(KonnectSpacing.m)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        KonnectAvatar(
          initials = builder.name,
          size = KonnectAvatarSize.Small,
          isOnline = builder.isOnline
        )
        KonnectPill(
          text = "${builder.reputationScore} rep",
          variant = KonnectPillVariant.Surface
        )
      }

      Spacer(modifier = Modifier.height(KonnectSpacing.xs))

      Text(
        text = builder.name,
        style = Typography.titleSmall,
        color = KonnectPrimaryText
      )
      Text(
        text = builder.role,
        style = Typography.labelSmall,
        color = KonnectSecondaryText,
        maxLines = 1
      )

      Spacer(modifier = Modifier.height(KonnectSpacing.s))

      KonnectSecondaryButton(
        text = "Connect",
        fullWidth = true,
        onClick = onConnect,
        testTag = "compact_connect_${builder.id}"
      )
    }
  }
}

@Composable
private fun CommunityQuestionCard(
  question: Question,
  onClick: () -> Unit
) {
  Konnect3DCard(
    modifier = Modifier.fillMaxWidth(),
    onClick = onClick,
    testTag = "community_question_${question.id}"
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        KonnectPill(
          text = question.category,
          variant = KonnectPillVariant.Primary
        )
        if (question.technologies.isNotEmpty()) {
          KonnectPill(
            text = question.technologies.first(),
            variant = KonnectPillVariant.Secondary
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          tint = KonnectPrimaryText,
          modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(KonnectSpacing.xxs))
        Text(
          text = "${question.matchedBuilderCount} matched",
          style = Typography.labelSmall,
          color = KonnectSecondaryText
        )
      }
    }

    Spacer(modifier = Modifier.height(KonnectSpacing.m))

    Text(
      text = question.title,
      style = Typography.titleMedium,
      color = KonnectPrimaryText
    )

    Spacer(modifier = Modifier.height(KonnectSpacing.xs))

    Text(
      text = question.description,
      style = Typography.bodyMedium,
      color = KonnectSecondaryText,
      maxLines = 2
    )

    Spacer(modifier = Modifier.height(KonnectSpacing.m))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        KonnectAvatar(
          initials = question.authorName,
          size = KonnectAvatarSize.Small
        )
        Spacer(modifier = Modifier.width(KonnectSpacing.xs))
        Text(
          text = "${question.authorName} • ${question.timeAgo}",
          style = Typography.labelSmall,
          color = KonnectSecondaryText
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "${question.replyCount} replies",
          style = Typography.labelSmall,
          color = KonnectPrimaryDarkAction
        )
        Spacer(modifier = Modifier.width(KonnectSpacing.xxs))
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForward,
          contentDescription = null,
          tint = KonnectPrimaryDarkAction,
          modifier = Modifier.size(14.dp)
        )
      }
    }
  }
}
