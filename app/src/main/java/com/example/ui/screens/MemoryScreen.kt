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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.data.model.MemoryItem
import com.example.ui.components.KonnectAIEmptyState
import com.example.ui.components.KonnectAvatar
import com.example.ui.components.KonnectAvatarSize
import com.example.ui.components.KonnectFloatingObject
import com.example.ui.components.KonnectObjectType
import com.example.ui.components.KonnectPill
import com.example.ui.components.KonnectPillVariant
import com.example.ui.components.KonnectPrimaryButton
import com.example.ui.components.KonnectSearchField
import com.example.ui.components.KonnectSecondaryButton
import com.example.ui.theme.KonnectBackground
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectShapes
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography
import com.example.viewmodel.MemoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryScreen(
  viewModel: MemoryViewModel,
  onAskAboutThis: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val sheetState = rememberModalBottomSheetState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(KonnectBackground)
      .testTag("memory_screen")
      .padding(horizontal = KonnectSpacing.screenHorizontal),
    verticalArrangement = Arrangement.spacedBy(KonnectSpacing.l)
  ) {

    // 1. Spatial Knowledge Archive Header
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
              text = "KNOWLEDGE ARCHIVE",
              variant = KonnectPillVariant.Primary
            )
            Spacer(modifier = Modifier.height(KonnectSpacing.s))
            Text(
              text = "Community Memory",
              style = Typography.headlineLarge.copy(fontSize = 24.sp),
              color = KonnectPrimaryText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "What the community has already solved. Permanent, verified architectural resolutions.",
              style = Typography.bodyMedium,
              color = KonnectSecondaryText
            )
          }

          Spacer(modifier = Modifier.width(KonnectSpacing.m))

          KonnectFloatingObject(
            type = KonnectObjectType.Memory,
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
          .shadow(4.dp, RoundedCornerShape(20.dp), ambientColor = Color(0x0E171717), spotColor = Color(0x14171717))
          .clip(RoundedCornerShape(20.dp))
          .background(KonnectPrimarySurface)
          .border(1.dp, Color(0x14111111), RoundedCornerShape(20.dp))
          .padding(KonnectSpacing.l)
      ) {
        KonnectSearchField(
          query = uiState.searchQuery,
          onQueryChange = { viewModel.onSearchQueryChanged(it) },
          placeholder = "Search solved architectures, problems, technologies...",
          testTag = "memory_search_input"
        )
      }
    }

    // 3. Section Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Memory Capsules",
          style = Typography.titleLarge,
          color = KonnectPrimaryText
        )
        Text(
          text = "${uiState.filteredMemories.size} preserved in orbit",
          style = Typography.labelSmall,
          color = KonnectSecondaryText
        )
      }
    }

    // 4. Memory Capsule Cards
    if (uiState.filteredMemories.isEmpty()) {
      item {
        KonnectAIEmptyState(
          title = "No Memory Capsules",
          description = "Your solved knowledge will live here.",
          objectType = KonnectObjectType.Memory,
          actionText = "Clear Search",
          onActionClick = { viewModel.onSearchQueryChanged("") }
        )
      }
    } else {
      items(uiState.filteredMemories, key = { it.id }) { memory ->
        MemoryCapsuleCard(
          memory = memory,
          onClick = { viewModel.selectMemory(memory) }
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(KonnectSpacing.xxxl))
    }
  }

  // 5. Memory Detail Modal Bottom Sheet
  if (uiState.selectedMemory != null) {
    val memory = uiState.selectedMemory!!
    ModalBottomSheet(
      onDismissRequest = { viewModel.clearSelectedMemory() },
      sheetState = sheetState,
      containerColor = KonnectPrimarySurface,
      shape = KonnectShapes.BottomSheet
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(KonnectSpacing.cardPaddingHero)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            memory.technologies.forEach { tech ->
              KonnectPill(text = tech, variant = KonnectPillVariant.Primary)
            }
          }
          KonnectPill(
            text = "Verified Capsule",
            variant = KonnectPillVariant.Secondary
          )
        }

        Spacer(modifier = Modifier.height(KonnectSpacing.m))

        Text(
          text = memory.title,
          style = Typography.headlineMedium.copy(fontSize = 20.sp),
          color = KonnectPrimaryText
        )

        Spacer(modifier = Modifier.height(KonnectSpacing.s))

        Text(
          text = "PROBLEM DEFINITION",
          style = Typography.labelSmall.copy(fontSize = 11.sp),
          color = KonnectSecondaryText
        )
        Text(
          text = memory.problem,
          style = Typography.bodyMedium,
          color = KonnectPrimaryText
        )

        Spacer(modifier = Modifier.height(KonnectSpacing.m))

        Text(
          text = "SYNTHESIZED ARCHITECTURAL RESOLUTION",
          style = Typography.labelSmall.copy(fontSize = 11.sp),
          color = KonnectSecondaryText
        )
        Spacer(modifier = Modifier.height(KonnectSpacing.xxs))
        Text(
          text = memory.solution,
          style = Typography.bodyMedium,
          color = KonnectPrimaryText
        )

        Spacer(modifier = Modifier.height(KonnectSpacing.m))

        Text(
          text = "CONTRIBUTING ARCHITECTS",
          style = Typography.labelSmall.copy(fontSize = 11.sp),
          color = KonnectSecondaryText
        )
        Spacer(modifier = Modifier.height(KonnectSpacing.xs))
        Row(
          horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.s),
          verticalAlignment = Alignment.CenterVertically
        ) {
          memory.contributors.forEach { name ->
            Row(verticalAlignment = Alignment.CenterVertically) {
              KonnectAvatar(initials = name, size = KonnectAvatarSize.Small)
              Spacer(modifier = Modifier.width(KonnectSpacing.xxs))
              Text(text = name, style = Typography.labelSmall, color = KonnectPrimaryText)
            }
          }
        }

        Spacer(modifier = Modifier.height(KonnectSpacing.l))

        // REMEMBER -> ASK AGAIN LOOP
        KonnectPrimaryButton(
          text = "Ask about this",
          leadingIcon = Icons.Default.HelpOutline,
          onClick = {
            val title = memory.title
            viewModel.clearSelectedMemory()
            onAskAboutThis(title)
          },
          testTag = "memory_ask_about_this_button"
        )
        Spacer(modifier = Modifier.height(KonnectSpacing.xl))
      }
    }
  }
}

@Composable
private fun MemoryCapsuleCard(
  memory: MemoryItem,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .drawBehind {
        drawRoundRect(
          color = Color(0x0C111111),
          topLeft = Offset(0f, 5.dp.toPx()),
          size = Size(size.width, size.height),
          cornerRadius = androidx.compose.ui.geometry.CornerRadius(22.dp.toPx(), 22.dp.toPx())
        )
      }
      .shadow(4.dp, RoundedCornerShape(22.dp), ambientColor = Color(0x0E171717), spotColor = Color(0x16171717))
      .clip(RoundedCornerShape(22.dp))
      .background(KonnectPrimarySurface)
      .border(1.dp, Color(0x16111111), RoundedCornerShape(22.dp))
      .padding(KonnectSpacing.cardPaddingStandard)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          memory.technologies.take(2).forEach { tech ->
            KonnectPill(
              text = tech,
              variant = KonnectPillVariant.Secondary
            )
          }
        }
        Text(
          text = "${memory.discussionCount} discussions",
          style = Typography.labelSmall,
          color = KonnectSecondaryText
        )
      }

      Spacer(modifier = Modifier.height(KonnectSpacing.m))

      Text(
        text = memory.title,
        style = Typography.titleMedium.copy(fontSize = 16.sp),
        color = KonnectPrimaryText
      )

      Spacer(modifier = Modifier.height(KonnectSpacing.xs))

      Text(
        text = memory.summary,
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
            initials = memory.solverName,
            size = KonnectAvatarSize.Small
          )
          Spacer(modifier = Modifier.width(KonnectSpacing.xs))
          Text(
            text = "Solved by ${memory.solverName}",
            style = Typography.labelSmall,
            color = KonnectSecondaryText
          )
        }

        KonnectSecondaryButton(
          text = "Read capsule",
          fullWidth = false,
          onClick = onClick,
          testTag = "read_solution_${memory.id}"
        )
      }
    }
  }
}
