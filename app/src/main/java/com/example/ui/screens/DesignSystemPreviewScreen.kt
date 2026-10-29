package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.KonnectAIEmptyState
import com.example.ui.components.KonnectAIErrorState
import com.example.ui.components.KonnectAIProcessing
import com.example.ui.components.KonnectAISuccess
import com.example.ui.components.KonnectAvatar
import com.example.ui.components.KonnectAvatarSize
import com.example.ui.components.KonnectBottomNavBar
import com.example.ui.components.KonnectFloatingObject
import com.example.ui.components.KonnectHeroCard
import com.example.ui.components.KonnectObjectType
import com.example.ui.components.KonnectPill
import com.example.ui.components.KonnectPillVariant
import com.example.ui.components.KonnectPrimaryButton
import com.example.ui.components.KonnectSearchField
import com.example.ui.components.KonnectSecondaryButton
import com.example.ui.components.KonnectTextField
import com.example.ui.components.KonnectTopBar
import com.example.ui.components.KonnectVoiceInput
import com.example.ui.components.KonnectVoiceState
import com.example.ui.components.Konnect3DCard
import com.example.ui.navigation.KonnectDestination
import com.example.ui.theme.KonnectBackground
import com.example.ui.theme.KonnectBadgeSuccess
import com.example.ui.theme.KonnectBorder
import com.example.ui.theme.KonnectBorderSubtle
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondarySurface
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectShapes
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography

/**
 * DesignSystemPreviewScreen
 *
 * Dedicated visual QA and development verification page.
 * Showcases all 16 foundational sections of the locked KONNECT design system:
 * 1. Colors
 * 2. Typography
 * 3. Spacing
 * 4. Shapes
 * 5. 3D Cards
 * 6. Hero Cards
 * 7. Pills
 * 8. Buttons
 * 9. Floating Objects
 * 10. Input Fields
 * 11. Avatars
 * 12. Bottom Navigation
 * 13. Top Bars
 * 14. Loading States
 * 15. Empty States
 * 16. AI States
 */
@Composable
fun DesignSystemPreviewScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler(onBack = onBack)

  var textInputDemo by remember { mutableStateOf("Distributed sync consensus protocol") }
  var searchDemo by remember { mutableStateOf("Qualcomm NPU") }
  var voiceStateDemo by remember { mutableStateOf(KonnectVoiceState.Idle) }
  var selectedTabDemo by remember { mutableStateOf(KonnectDestination.Home.route) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(KonnectBackground)
      .testTag("design_system_preview_screen")
  ) {
    KonnectTopBar(
      title = "Design System Showcase",
      subtitle = "Visual QA & Component Laboratory",
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
      verticalArrangement = Arrangement.spacedBy(KonnectSpacing.sectionGap)
    ) {

      // Section 1: Colors
      item {
        SectionTitle(number = "01", title = "Color System")
        Text(
          text = "Neutral, calm white/soft-grey environment. Visual interest is achieved through physical depth, studio lighting, and typography.",
          style = Typography.bodyMedium,
          color = KonnectSecondaryText
        )
        Spacer(modifier = Modifier.height(KonnectSpacing.m))

        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          ColorSwatchRow("Background", "#F5F5F3", KonnectBackground)
          Spacer(modifier = Modifier.height(KonnectSpacing.s))
          ColorSwatchRow("Primary Surface", "#FFFFFF", KonnectPrimarySurface)
          Spacer(modifier = Modifier.height(KonnectSpacing.s))
          ColorSwatchRow("Secondary Surface", "#ECEDEB", KonnectSecondarySurface)
          Spacer(modifier = Modifier.height(KonnectSpacing.s))
          ColorSwatchRow("Primary Text", "#171717", KonnectPrimaryText)
          Spacer(modifier = Modifier.height(KonnectSpacing.s))
          ColorSwatchRow("Secondary Text", "#777777", KonnectSecondaryText)
          Spacer(modifier = Modifier.height(KonnectSpacing.s))
          ColorSwatchRow("Primary Dark Action", "#111111", KonnectPrimaryDarkAction)
        }
      }

      // Section 2: Typography
      item {
        SectionTitle(number = "02", title = "Typography Hierarchy")
        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          Text(text = "Display Large (34sp)", style = Typography.displayLarge)
          Spacer(modifier = Modifier.height(KonnectSpacing.s))
          Text(text = "Headline Large (24sp)", style = Typography.headlineLarge)
          Spacer(modifier = Modifier.height(KonnectSpacing.s))
          Text(text = "Headline Medium (20sp)", style = Typography.headlineMedium)
          Spacer(modifier = Modifier.height(KonnectSpacing.s))
          Text(text = "Title Large (18sp)", style = Typography.titleLarge)
          Spacer(modifier = Modifier.height(KonnectSpacing.s))
          Text(text = "Body Large (15sp) - Clean readable architectural descriptions", style = Typography.bodyLarge)
          Spacer(modifier = Modifier.height(KonnectSpacing.s))
          Text(text = "Body Medium (14sp) - Secondary descriptions and context", style = Typography.bodyMedium)
          Spacer(modifier = Modifier.height(KonnectSpacing.s))
          Text(text = "LABEL SMALL (11sp) - METADATA & TAGS", style = Typography.labelSmall)
        }
      }

      // Section 3: Spacing
      item {
        SectionTitle(number = "03", title = "Spacing Tokens")
        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          val spacings = listOf(
            "xxs" to KonnectSpacing.xxs,
            "xs" to KonnectSpacing.xs,
            "s" to KonnectSpacing.s,
            "m" to KonnectSpacing.m,
            "l" to KonnectSpacing.l,
            "xl" to KonnectSpacing.xl,
            "xxl" to KonnectSpacing.xxl
          )
          spacings.forEach { (name, value) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Spacing.$name (${value.value.toInt()}dp)",
                style = Typography.labelMedium,
                color = KonnectPrimaryText,
                modifier = Modifier.width(130.dp)
              )
              Box(
                modifier = Modifier
                  .height(14.dp)
                  .width(value * 3)
                  .clip(RoundedCornerShape(4.dp))
                  .background(KonnectPrimaryDarkAction)
              )
            }
          }
        }
      }

      // Section 4: Shapes
      item {
        SectionTitle(number = "04", title = "Shape System")
        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          Text(text = "Large, soft rounded corners with physical bevels:", style = Typography.bodyMedium, color = KonnectSecondaryText)
          Spacer(modifier = Modifier.height(KonnectSpacing.m))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.s)
          ) {
            ShapeDemoTile("Small (16dp)", KonnectShapes.SmallCard, Modifier.weight(1f))
            ShapeDemoTile("Standard (20dp)", KonnectShapes.StandardCard, Modifier.weight(1f))
            ShapeDemoTile("Hero (24dp)", KonnectShapes.HeroCard, Modifier.weight(1f))
          }
          Spacer(modifier = Modifier.height(KonnectSpacing.s))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.s)
          ) {
            ShapeDemoTile("Button (16dp)", KonnectShapes.Button, Modifier.weight(1f))
            ShapeDemoTile("Pill (999dp)", KonnectShapes.Pill, Modifier.weight(1f))
          }
        }
      }

      // Section 5: 3D Cards
      item {
        SectionTitle(number = "05", title = "3D Objectified Cards")
        Text(
          text = "Standard cards with studio ambient diffusion, contact shadow, top specular reflection highlight, and tactile press response:",
          style = Typography.bodyMedium,
          color = KonnectSecondaryText
        )
        Spacer(modifier = Modifier.height(KonnectSpacing.m))

        Konnect3DCard(
          modifier = Modifier.fillMaxWidth(),
          onClick = { /* Demo click */ }
        ) {
          Text(text = "Interactive 3D Card (Tap to feel press reaction)", style = Typography.titleMedium)
          Spacer(modifier = Modifier.height(KonnectSpacing.xs))
          Text(
            text = "Notice the soft lower contact shadow, specular rim light at the top edge, and spring scale micro-interaction.",
            style = Typography.bodyMedium,
            color = KonnectSecondaryText
          )
        }
      }

      // Section 6: Hero Cards
      item {
        SectionTitle(number = "06", title = "Hero Cards")
        KonnectHeroCard(
          title = "What are you trying to solve?",
          subtitle = "Inquiries are parsed semantically by AI and routed directly to verified peer architects.",
          badgeText = "PRIMARY PRODUCT ACTION",
          floatingObject = {
            KonnectFloatingObject(type = KonnectObjectType.AI, size = 64.dp)
          },
          actionButton = {
            KonnectPrimaryButton(
              text = "Start Architectural Inquiry",
              leadingIcon = Icons.Default.Add,
              onClick = {}
            )
          },
          secondaryActionButton = {
            KonnectSecondaryButton(
              text = "Browse Memories",
              onClick = {}
            )
          }
        )
      }

      // Section 7: Pills
      item {
        SectionTitle(number = "07", title = "Pill Tags & Status Indicators")
        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.s),
            modifier = Modifier.fillMaxWidth()
          ) {
            KonnectPill(text = "Primary Selected", isSelected = true)
            KonnectPill(text = "Secondary Tag", variant = KonnectPillVariant.Secondary)
            KonnectPill(text = "Surface Pill", variant = KonnectPillVariant.Surface)
          }
          Spacer(modifier = Modifier.height(KonnectSpacing.m))
          Row(
            horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.s),
            verticalAlignment = Alignment.CenterVertically
          ) {
            KonnectPill(
              text = "Active Online",
              variant = KonnectPillVariant.Secondary,
              leadingDotColor = KonnectBadgeSuccess
            )
            KonnectPill(
              text = "3 Architects Matched",
              variant = KonnectPillVariant.Primary
            )
          }
        }
      }

      // Section 8: Buttons
      item {
        SectionTitle(number = "08", title = "Buttons & Micro-interactions")
        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "Primary Action (Dark Charcoal #111111) & Secondary Action (Porcelain #FFFFFF):",
            style = Typography.bodyMedium,
            color = KonnectSecondaryText
          )
          Spacer(modifier = Modifier.height(KonnectSpacing.m))

          KonnectPrimaryButton(
            text = "Connect with Match (Primary)",
            leadingIcon = Icons.Default.Hub,
            trailingIcon = Icons.Default.ArrowForward,
            onClick = {}
          )

          Spacer(modifier = Modifier.height(KonnectSpacing.m))

          KonnectSecondaryButton(
            text = "View Discussion History (Secondary)",
            leadingIcon = Icons.Default.Search,
            onClick = {}
          )

          Spacer(modifier = Modifier.height(KonnectSpacing.m))

          KonnectPrimaryButton(
            text = "Disabled Button State",
            enabled = false,
            onClick = {}
          )
        }
      }

      // Section 9: Floating Objects
      item {
        SectionTitle(number = "09", title = "Floating 3D Objects")
        Text(
          text = "Abstract tactile product illustrations across the 5 core domains:",
          style = Typography.bodyMedium,
          color = KonnectSecondaryText
        )
        Spacer(modifier = Modifier.height(KonnectSpacing.m))

        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              KonnectFloatingObject(type = KonnectObjectType.AI, size = 52.dp)
              Spacer(modifier = Modifier.height(KonnectSpacing.xs))
              Text("AI Orb", style = Typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              KonnectFloatingObject(type = KonnectObjectType.Voice, size = 52.dp)
              Spacer(modifier = Modifier.height(KonnectSpacing.xs))
              Text("Voice", style = Typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              KonnectFloatingObject(type = KonnectObjectType.Knowledge, size = 52.dp)
              Spacer(modifier = Modifier.height(KonnectSpacing.xs))
              Text("Knowledge", style = Typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              KonnectFloatingObject(type = KonnectObjectType.Community, size = 52.dp)
              Spacer(modifier = Modifier.height(KonnectSpacing.xs))
              Text("Community", style = Typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              KonnectFloatingObject(type = KonnectObjectType.Memory, size = 52.dp)
              Spacer(modifier = Modifier.height(KonnectSpacing.xs))
              Text("Memory", style = Typography.labelSmall)
            }
          }
        }
      }

      // Section 10: Input Fields
      item {
        SectionTitle(number = "10", title = "Input System & Voice")
        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          KonnectTextField(
            value = textInputDemo,
            onValueChange = { textInputDemo = it },
            label = "INQUIRY TITLE",
            placeholder = "Enter question..."
          )

          Spacer(modifier = Modifier.height(KonnectSpacing.m))

          KonnectSearchField(
            query = searchDemo,
            onQueryChange = { searchDemo = it }
          )

          Spacer(modifier = Modifier.height(KonnectSpacing.l))

          Text(
            text = "Voice Input Interaction (Tap orb to cycle states):",
            style = Typography.labelSmall,
            color = KonnectSecondaryText
          )
          Spacer(modifier = Modifier.height(KonnectSpacing.m))

          KonnectVoiceInput(
            state = voiceStateDemo,
            onMicClick = {
              voiceStateDemo = when (voiceStateDemo) {
                KonnectVoiceState.Idle -> KonnectVoiceState.Listening
                KonnectVoiceState.Listening -> KonnectVoiceState.Processing
                KonnectVoiceState.Processing -> KonnectVoiceState.Complete
                KonnectVoiceState.Complete -> KonnectVoiceState.Idle
              }
            },
            transcriptText = if (voiceStateDemo == KonnectVoiceState.Complete) "Benchmarking SQLite WAL2 mode vs memory-mapped files" else ""
          )
        }
      }

      // Section 11: Avatars
      item {
        SectionTitle(number = "11", title = "Avatars & Relevance Badges")
        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              KonnectAvatar(initials = "AC", size = KonnectAvatarSize.Small, isOnline = true)
              Spacer(modifier = Modifier.height(KonnectSpacing.xs))
              Text("Small (32dp)", style = Typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              KonnectAvatar(initials = "DT", size = KonnectAvatarSize.Medium, isOnline = true, relevanceScore = 98)
              Spacer(modifier = Modifier.height(KonnectSpacing.xs))
              Text("Medium 98%", style = Typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              KonnectAvatar(initials = "ER", size = KonnectAvatarSize.Large, isOnline = false, isDarkVariant = false)
              Spacer(modifier = Modifier.height(KonnectSpacing.xs))
              Text("Large Porcelain", style = Typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              KonnectAvatar(initials = "JV", size = KonnectAvatarSize.Profile, isOnline = true)
              Spacer(modifier = Modifier.height(KonnectSpacing.xs))
              Text("Profile (80dp)", style = Typography.labelSmall)
            }
          }
        }
      }

      // Section 12: Bottom Navigation Dock
      item {
        SectionTitle(number = "12", title = "Bottom Navigation Dock")
        Text(
          text = "Floating white 3D dock with prominent tactile center 'Ask' action button:",
          style = Typography.bodyMedium,
          color = KonnectSecondaryText
        )
        Spacer(modifier = Modifier.height(KonnectSpacing.m))

        KonnectBottomNavBar(
          currentRoute = selectedTabDemo,
          onNavigate = { selectedTabDemo = it.route }
        )
      }

      // Section 13: Top Bars
      item {
        SectionTitle(number = "13", title = "Top Bar Variations")
        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          KonnectTopBar(
            title = "KONNECT",
            subtitle = "Questions find the right people.",
            avatarInitials = "JV",
            onNotificationsClick = {},
            onSettingsClick = {}
          )
        }
      }

      // Section 14: Loading States
      item {
        SectionTitle(number = "14", title = "Loading States")
        Konnect3DCard(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            CircularProgressIndicator(
              modifier = Modifier.size(28.dp),
              color = KonnectPrimaryDarkAction,
              strokeWidth = 2.5.dp
            )
            Spacer(modifier = Modifier.width(KonnectSpacing.m))
            Text(
              text = "Loading knowledge models...",
              style = Typography.bodyMedium,
              color = KonnectSecondaryText
            )
          }
        }
      }

      // Section 15: Empty States
      item {
        SectionTitle(number = "15", title = "Empty State")
        KonnectAIEmptyState(
          title = "No Inquiries Archived Yet",
          description = "Inquiries resolved by peer architects are permanently preserved here for collective memory.",
          actionText = "Pose First Inquiry",
          onActionClick = {}
        )
      }

      // Section 16: AI States
      item {
        SectionTitle(number = "16", title = "AI Processing, Success & Error")
        KonnectAIProcessing(
          statusText = "Gemini Cognitive Pipeline Active",
          subtext = "Matching inquiries against verified domain nodes"
        )

        Spacer(modifier = Modifier.height(KonnectSpacing.m))

        KonnectAISuccess(
          title = "Architecture Consensus Formed",
          summary = "Verified solution: Zero-copy direct ByteBuffer allocation reduces NPU inference overhead by 22% on target silicon.",
          matchedCount = 3
        )

        Spacer(modifier = Modifier.height(KonnectSpacing.m))

        KonnectAIErrorState(
          errorMessage = "Knowledge store connection timed out. Local cache intact.",
          onRetry = {}
        )

        Spacer(modifier = Modifier.height(KonnectSpacing.xxxl))
      }
    }
  }
}

@Composable
private fun SectionTitle(number: String, title: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(6.dp))
        .background(KonnectPrimaryDarkAction)
        .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
      Text(
        text = number,
        style = Typography.labelSmall,
        color = KonnectHighlight
      )
    }
    Spacer(modifier = Modifier.width(KonnectSpacing.s))
    Text(
      text = title,
      style = Typography.titleLarge,
      color = KonnectPrimaryText
    )
  }
}

@Composable
private fun ColorSwatchRow(name: String, hex: String, color: Color) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .shadow(2.dp, RoundedCornerShape(8.dp))
          .clip(RoundedCornerShape(8.dp))
          .background(color)
          .border(1.dp, KonnectBorder, RoundedCornerShape(8.dp))
      )
      Spacer(modifier = Modifier.width(KonnectSpacing.m))
      Text(
        text = name,
        style = Typography.titleSmall,
        color = KonnectPrimaryText
      )
    }
    Text(
      text = hex,
      style = Typography.labelMedium,
      color = KonnectSecondaryText
    )
  }
}

@Composable
private fun ShapeDemoTile(label: String, shape: androidx.compose.ui.graphics.Shape, modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .height(60.dp)
      .shadow(2.dp, shape)
      .clip(shape)
      .background(KonnectPrimarySurface)
      .border(1.dp, KonnectBorderSubtle, shape),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = label,
      style = Typography.labelSmall,
      color = KonnectPrimaryText
    )
  }
}
