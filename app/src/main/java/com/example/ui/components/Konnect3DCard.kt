package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.KonnectBorderSubtle
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectShapes
import com.example.ui.theme.KonnectSpacing

/**
 * Konnect3DCard
 *
 * A standardized, realistic soft 3D objectified card:
 * - Large rounded geometry (configurable, default 20dp)
 * - Soft ambient diffusion shadow (calm studio lighting, no harsh black drop)
 * - Lower contact shadow for grounding
 * - Optional top inner highlight for specular edge bevel reflection
 * - Spacious internal padding
 * - Subtle tactile press response when interactionEnabled is true
 */
@Composable
fun Konnect3DCard(
  modifier: Modifier = Modifier,
  shape: Shape = KonnectShapes.StandardCard,
  backgroundColor: Color = KonnectPrimarySurface,
  elevation: Dp = 3.dp,
  cornerRadius: Dp = 20.dp,
  contentPadding: PaddingValues = PaddingValues(KonnectSpacing.cardPaddingStandard),
  showHighlight: Boolean = true,
  showShadow: Boolean = true,
  interactionEnabled: Boolean = true,
  onClick: (() -> Unit)? = null,
  testTag: String = "konnect_3d_card",
  floatingObject: (@Composable () -> Unit)? = null,
  content: @Composable ColumnScope.() -> Unit
) {
  val effectiveShape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed && interactionEnabled && onClick != null) 0.985f else 1f,
    animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
    label = "card_scale"
  )

  val currentElevation = if (isPressed && onClick != null) (elevation * 0.4f) else elevation

  Box(
    modifier = modifier
      .testTag(testTag)
      .graphicsLayer {
        scaleX = scale
        scaleY = scale
      }
      .then(
        if (showShadow) {
          // Soft multi-layered studio lighting shadow
          Modifier
            .drawBehind {
              // 1. Soft lower contact shadow directly under object
              drawRoundRect(
                color = Color(0x0A111111),
                topLeft = Offset(0f, 4.dp.toPx()),
                size = Size(size.width, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
              )
              // 2. Ambient studio diffusion aura
              drawRoundRect(
                color = Color(0x06111111),
                topLeft = Offset(-1.dp.toPx(), 2.dp.toPx()),
                size = Size(size.width + 2.dp.toPx(), size.height + 3.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius((cornerRadius + 1.dp).toPx(), (cornerRadius + 1.dp).toPx())
              )
            }
            .shadow(
              elevation = currentElevation,
              shape = effectiveShape,
              ambientColor = Color(0x0E171717),
              spotColor = Color(0x12171717)
            )
        } else {
          Modifier
        }
      )
      .clip(effectiveShape)
      .background(backgroundColor)
      .then(
        if (showHighlight) {
          // Top inner specular rim reflection
          Modifier.drawBehind {
            drawRect(
              brush = Brush.verticalGradient(
                colors = listOf(
                  KonnectHighlight.copy(alpha = 0.90f),
                  KonnectHighlight.copy(alpha = 0.0f)
                ),
                startY = 0f,
                endY = 4.dp.toPx()
              )
            )
          }
        } else {
          Modifier
        }
      )
      .border(
        width = 1.dp,
        color = KonnectBorderSubtle,
        shape = effectiveShape
      )
      .then(
        if (onClick != null && interactionEnabled) {
          Modifier.clickable(
            interactionSource = interactionSource,
            indication = ripple(bounded = true),
            onClick = onClick
          )
        } else {
          Modifier
        }
      )
  ) {
    if (floatingObject != null) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(
          modifier = Modifier.weight(1f),
          content = content
        )
        Box(
          modifier = Modifier.padding(start = KonnectSpacing.m),
          contentAlignment = Alignment.Center
        ) {
          floatingObject()
        }
      }
    } else {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(contentPadding),
        content = content
      )
    }
  }
}
