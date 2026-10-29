package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
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
 * KonnectAIProcessing
 *
 * Calm, 3D animated indicator:
 * Three soft tactile nodes gently oscillating and pulsing with studio shadows.
 * Avoids flashing neon animations.
 */
@Composable
fun KonnectAIProcessing(
  modifier: Modifier = Modifier,
  statusText: String = "Analyzing technical inquiry...",
  subtext: String = "Evaluating domain relevance across builder knowledge graph",
  testTag: String = "konnect_ai_processing"
) {
  val transition = rememberInfiniteTransition(label = "ai_nodes_oscillation")

  val offset1 by transition.animateFloat(
    initialValue = -4f,
    targetValue = 4f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "node1_offset"
  )

  val offset2 by transition.animateFloat(
    initialValue = 4f,
    targetValue = -4f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "node2_offset"
  )

  val offset3 by transition.animateFloat(
    initialValue = -3f,
    targetValue = 3f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "node3_offset"
  )

  Konnect3DCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = KonnectSpacing.m),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Animated 3-node tactile constellation
      Box(
        modifier = Modifier.size(width = 140.dp, height = 70.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.size(width = 100.dp, height = 50.dp)) {
          val yMid = size.height / 2f
          val p1 = Offset(15.dp.toPx(), yMid + offset1.dp.toPx())
          val p2 = Offset(50.dp.toPx(), yMid + offset2.dp.toPx())
          val p3 = Offset(85.dp.toPx(), yMid + offset3.dp.toPx())

          // Subtle connection bridges
          drawLine(color = Color(0x35111111), start = p1, end = p2, strokeWidth = 2.dp.toPx())
          drawLine(color = Color(0x35111111), start = p2, end = p3, strokeWidth = 2.dp.toPx())
        }

        // Node 1
        SoftNode(
          offsetY = offset1,
          size = 28.dp,
          isDark = false,
          modifier = Modifier.align(Alignment.CenterStart)
        )

        // Node 2 (Center Core Node)
        SoftNode(
          offsetY = offset2,
          size = 36.dp,
          isDark = true,
          modifier = Modifier.align(Alignment.Center)
        )

        // Node 3
        SoftNode(
          offsetY = offset3,
          size = 28.dp,
          isDark = false,
          modifier = Modifier.align(Alignment.CenterEnd)
        )
      }

      Spacer(modifier = Modifier.height(KonnectSpacing.m))

      Text(
        text = statusText,
        style = Typography.titleMedium,
        color = KonnectPrimaryText
      )

      Spacer(modifier = Modifier.height(KonnectSpacing.xs))

      Text(
        text = subtext,
        style = Typography.bodySmall,
        color = KonnectSecondaryText
      )
    }
  }
}

@Composable
private fun SoftNode(
  offsetY: Float,
  size: androidx.compose.ui.unit.Dp,
  isDark: Boolean,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .offset(y = offsetY.dp)
      .size(size)
      .shadow(
        elevation = 4.dp,
        shape = CircleShape,
        ambientColor = Color(0x18111111),
        spotColor = Color(0x22111111)
      )
      .clip(CircleShape)
      .background(
        if (isDark) {
          Brush.radialGradient(
            colors = listOf(Color(0xFF2E2E2E), KonnectPrimaryDarkAction)
          )
        } else {
          Brush.radialGradient(
            colors = listOf(KonnectHighlight, KonnectSecondarySurface)
          )
        }
      )
      .border(
        width = 1.dp,
        color = if (isDark) Color(0x35FFFFFF) else Color(0x14111111),
        shape = CircleShape
      )
  )
}

/**
 * KonnectAISuccess
 *
 * Verified resolution confirmation card
 */
@Composable
fun KonnectAISuccess(
  title: String,
  summary: String,
  modifier: Modifier = Modifier,
  matchedCount: Int = 3,
  testTag: String = "konnect_ai_success"
) {
  Konnect3DCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(KonnectPrimaryDarkAction),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = KonnectHighlight,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.size(KonnectSpacing.m))
        Column {
          Text(
            text = title,
            style = Typography.titleMedium,
            color = KonnectPrimaryText
          )
          Text(
            text = "$matchedCount verified architects matched",
            style = Typography.labelSmall,
            color = KonnectSecondaryText
          )
        }
      }
      KonnectPill(text = "Resolved", variant = KonnectPillVariant.Primary)
    }

    Spacer(modifier = Modifier.height(KonnectSpacing.m))

    Text(
      text = summary,
      style = Typography.bodyMedium,
      color = KonnectPrimaryText
    )
  }
}

/**
 * KonnectAIEmptyState
 *
 * Calm empty state with soft 3D graphic illustration and suggestion chips
 */
@Composable
fun KonnectAIEmptyState(
  title: String,
  description: String,
  modifier: Modifier = Modifier,
  objectType: KonnectObjectType = KonnectObjectType.Knowledge,
  useAICore: Boolean = false,
  actionText: String? = null,
  onActionClick: (() -> Unit)? = null,
  testTag: String = "konnect_ai_empty_state"
) {
  Konnect3DCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = KonnectSpacing.l),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      if (useAICore) {
        KonnectAICore(
          size = 64.dp,
          state = KonnectAICoreState.SEARCHING
        )
      } else {
        KonnectFloatingObject(
          type = objectType,
          size = 64.dp
        )
      }

      Spacer(modifier = Modifier.height(KonnectSpacing.l))

      Text(
        text = title,
        style = Typography.titleLarge,
        color = KonnectPrimaryText
      )

      Spacer(modifier = Modifier.height(KonnectSpacing.xs))

      Text(
        text = description,
        style = Typography.bodyMedium,
        color = KonnectSecondaryText,
        modifier = Modifier.padding(horizontal = KonnectSpacing.m)
      )

      if (actionText != null && onActionClick != null) {
        Spacer(modifier = Modifier.height(KonnectSpacing.l))
        KonnectSecondaryButton(
          text = actionText,
          fullWidth = false,
          onClick = onActionClick
        )
      }
    }
  }
}

/**
 * KonnectAIErrorState
 *
 * Subtle, non-jarring error feedback with retry affordance
 */
@Composable
fun KonnectAIErrorState(
  errorMessage: String,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = "konnect_ai_error_state"
) {
  Konnect3DCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFFE5E6E3)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.WarningAmber,
            contentDescription = null,
            tint = KonnectPrimaryText,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.size(KonnectSpacing.m))
        Column {
          Text(
            text = "Connection Interrupted",
            style = Typography.titleSmall,
            color = KonnectPrimaryText
          )
          Text(
            text = errorMessage,
            style = Typography.bodySmall,
            color = KonnectSecondaryText
          )
        }
      }

      KonnectSecondaryButton(
        text = "Retry",
        fullWidth = false,
        leadingIcon = Icons.Default.Refresh,
        onClick = onRetry
      )
    }
  }
}
