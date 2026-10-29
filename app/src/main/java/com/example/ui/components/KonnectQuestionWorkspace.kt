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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography

/**
 * KonnectQuestionWorkspace
 *
 * Dominant spatial hero environment:
 * - Floating Question Workspace with layered depth
 * - Prominent 3D KONNECT AI Core at the focal point
 * - Floating connected spatial intelligence nodes
 * - Subtle ambient shadow & specular highlights
 * - Communicates AI + COMMUNITY + PROBLEM SOLVING instantly
 */
@Composable
fun KonnectQuestionWorkspace(
  onAskClick: () -> Unit,
  onVoiceClick: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = "konnect_question_workspace"
) {
  val infiniteTransition = rememberInfiniteTransition(label = "workspace_ambient_float")
  val floatY by infiniteTransition.animateFloat(
    initialValue = -2.5f,
    targetValue = 2.5f,
    animationSpec = infiniteRepeatable(
      animation = tween(3200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "hero_float"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag)
  ) {
    // 1. Layered Substrate Plate (Depth 1: Foundation depth layer)
    Box(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .height(290.dp)
        .align(Alignment.Center)
        .offset(y = 12.dp)
        .clip(RoundedCornerShape(26.dp))
        .background(Color(0xFFE8E9E6))
        .border(1.dp, Color(0x10111111), RoundedCornerShape(26.dp))
    )

    // 2. Primary Floating Workspace (Depth 2: Elevated Surface)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .offset(y = floatY.dp)
        .drawBehind {
          // Soft ambient contact shadow under workspace
          drawRoundRect(
            color = Color(0x0E111111),
            topLeft = Offset(0f, 8.dp.toPx()),
            size = Size(size.width, size.height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx(), 28.dp.toPx())
          )
          drawRoundRect(
            color = Color(0x06111111),
            topLeft = Offset(-2.dp.toPx(), 3.dp.toPx()),
            size = Size(size.width + 4.dp.toPx(), size.height + 6.dp.toPx()),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(30.dp.toPx(), 30.dp.toPx())
          )
        }
        .shadow(
          elevation = 8.dp,
          shape = RoundedCornerShape(28.dp),
          ambientColor = Color(0x12171717),
          spotColor = Color(0x1A171717)
        )
        .clip(RoundedCornerShape(28.dp))
        .background(KonnectPrimarySurface)
        // Top specular reflection line
        .drawBehind {
          drawRect(
            brush = Brush.verticalGradient(
              colors = listOf(KonnectHighlight.copy(alpha = 0.98f), Color.Transparent),
              startY = 0f,
              endY = 6.dp.toPx()
            )
          )
        }
        .border(
          width = 1.dp,
          color = Color(0x18111111),
          shape = RoundedCornerShape(28.dp)
        )
        .padding(horizontal = KonnectSpacing.cardPaddingHero, vertical = KonnectSpacing.l)
    ) {
      // Background Neural Connection Filaments (Drawn subtly inside workspace)
      Canvas(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp)
      ) {
        val w = size.width
        val h = size.height

        // Subtle architectural depth rings
        drawCircle(
          color = Color(0x08111111),
          radius = w * 0.42f,
          center = Offset(w * 0.88f, h * 0.28f),
          style = Stroke(width = 1.dp.toPx())
        )
        drawCircle(
          color = Color(0x05111111),
          radius = w * 0.60f,
          center = Offset(w * 0.88f, h * 0.28f),
          style = Stroke(
            width = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 8f))
          )
        )
      }

      Column(modifier = Modifier.fillMaxWidth()) {
        // Top Category Pill & Micro State
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          KonnectPill(
            text = "SPATIAL WORKSPACE",
            variant = KonnectPillVariant.Secondary
          )

          Text(
            text = "AI • COMMUNITY • MEMORY",
            style = Typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 0.8.sp),
            color = KonnectSecondaryText
          )
        }

        Spacer(modifier = Modifier.height(KonnectSpacing.m))

        // Center: Headline + Signature AI Core floating at focal position
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "What are you\ntrying to solve?",
              style = Typography.headlineLarge.copy(fontSize = 28.sp, lineHeight = 34.sp),
              color = KonnectPrimaryText
            )
            Spacer(modifier = Modifier.height(KonnectSpacing.xs))
            Text(
              text = "Ask the community. Let KONNECT find the right people and knowledge.",
              style = Typography.bodyMedium,
              color = KonnectSecondaryText
            )
          }

          Spacer(modifier = Modifier.width(KonnectSpacing.m))

          // Signature 3D AI Core
          KonnectAICore(
            size = 86.dp,
            state = KonnectAICoreState.IDLE,
            testTag = "workspace_ai_core"
          )
        }

        Spacer(modifier = Modifier.height(KonnectSpacing.xl))

        // Physical Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.m),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(modifier = Modifier.weight(1.3f)) {
            KonnectPrimaryButton(
              text = "Ask a question",
              leadingIcon = Icons.Default.Add,
              onClick = onAskClick,
              testTag = "workspace_ask_primary_button"
            )
          }

          Box(modifier = Modifier.weight(1f)) {
            KonnectSecondaryButton(
              text = "Use voice",
              leadingIcon = Icons.Default.Mic,
              onClick = onVoiceClick,
              testTag = "workspace_ask_voice_button"
            )
          }
        }
      }
    }
  }
}
