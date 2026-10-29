package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.Typography

enum class SpatialDockActionType {
  ASK,
  COMMUNITY,
  MEMORY
}

/**
 * KonnectSpatialActionDock
 *
 * Replaces traditional flat rectangular cards with three independent floating 3D objects:
 * - ASK: Dimensional floating question object with embedded intelligence node
 * - COMMUNITY: Interlinked dimensional people network nodes
 * - MEMORY: Dimensional stacked knowledge crystal capsule
 *
 * Full tactile interaction:
 * - Idle: Gentle asynchronous floating
 * - Touch: Compresses, drops shadow, tilts slightly
 * - Release: Snappy spring back
 */
@Composable
fun KonnectSpatialActionDock(
  onAskClick: () -> Unit,
  onCommunityClick: () -> Unit,
  onMemoryClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    SpatialDockItem(
      type = SpatialDockActionType.ASK,
      label = "Ask",
      onClick = onAskClick,
      floatPhaseOffset = 0,
      testTag = "spatial_dock_ask"
    )

    SpatialDockItem(
      type = SpatialDockActionType.COMMUNITY,
      label = "Community",
      onClick = onCommunityClick,
      floatPhaseOffset = 800,
      testTag = "spatial_dock_community"
    )

    SpatialDockItem(
      type = SpatialDockActionType.MEMORY,
      label = "Memory",
      onClick = onMemoryClick,
      floatPhaseOffset = 1600,
      testTag = "spatial_dock_memory"
    )
  }
}

@Composable
private fun SpatialDockItem(
  type: SpatialDockActionType,
  label: String,
  onClick: () -> Unit,
  floatPhaseOffset: Int,
  testTag: String,
  size: Dp = 68.dp
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  // Spring physics for touch feedback
  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.92f else 1f,
    animationSpec = spring(dampingRatio = 0.7f, stiffness = 600f),
    label = "dock_item_scale"
  )
  val rotation by animateFloatAsState(
    targetValue = if (isPressed) -2.5f else 0f,
    animationSpec = spring(dampingRatio = 0.75f, stiffness = 500f),
    label = "dock_item_rotation"
  )

  // Floating breathing oscillation
  val infiniteTransition = rememberInfiniteTransition(label = "dock_item_float")
  val floatY by infiniteTransition.animateFloat(
    initialValue = -2.5f,
    targetValue = 2.5f,
    animationSpec = infiniteRepeatable(
      animation = tween(2600 + floatPhaseOffset / 2, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "dock_float_y"
  )

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .testTag(testTag)
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
  ) {
    Box(
      modifier = Modifier
        .size(size)
        .graphicsLayer {
          scaleX = scale
          scaleY = scale
          rotationZ = rotation
          translationY = (if (isPressed) 4f else floatY) * density
        },
      contentAlignment = Alignment.Center
    ) {
      Canvas(modifier = Modifier.size(size)) {
        val center = Offset(this.size.width / 2f, this.size.height / 2f)

        // Contact shadow that compresses on press
        val shadowRadius = size.toPx() * 0.38f * (if (isPressed) 0.85f else 1f)
        val shadowY = center.y + size.toPx() * 0.32f + (if (isPressed) 2f else -floatY * 0.4f)
        drawOval(
          brush = Brush.radialGradient(
            colors = listOf(Color(0x24111111), Color(0x08111111), Color.Transparent),
            center = Offset(center.x, shadowY),
            radius = shadowRadius
          ),
          topLeft = Offset(center.x - shadowRadius, shadowY - shadowRadius * 0.32f),
          size = Size(shadowRadius * 2f, shadowRadius * 0.64f)
        )

        // Draw 3D Spatial Objects
        when (type) {
          SpatialDockActionType.ASK -> drawSpatialAskObject(center = center, size = size.toPx())
          SpatialDockActionType.COMMUNITY -> drawSpatialCommunityObject(center = center, size = size.toPx())
          SpatialDockActionType.MEMORY -> drawSpatialMemoryObject(center = center, size = size.toPx())
        }
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = label,
      style = Typography.labelMedium,
      color = KonnectPrimaryText
    )
  }
}

/**
 * 3D Question Object (Faceted sphere with tactile question geometry)
 */
private fun DrawScope.drawSpatialAskObject(center: Offset, size: Float) {
  val radius = size * 0.36f

  // Base Porcelain Body
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(Color(0xFFFFFFFF), Color(0xFFF2F2F0), Color(0xFFD8D9D6)),
      center = Offset(center.x - radius * 0.3f, center.y - radius * 0.35f),
      radius = radius * 1.3f
    ),
    radius = radius,
    center = center
  )

  // Rim line
  drawCircle(
    color = Color(0x18111111),
    radius = radius,
    center = center,
    style = Stroke(width = 1.2.dp.toPx())
  )

  // Dark Core Node (Elevated)
  val coreRadius = radius * 0.44f
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(Color(0xFF383838), Color(0xFF171717)),
      center = Offset(center.x - coreRadius * 0.25f, center.y - coreRadius * 0.25f),
      radius = coreRadius * 1.2f
    ),
    radius = coreRadius,
    center = center
  )

  // Inner Plus / Ask Symbol
  val barLen = coreRadius * 0.6f
  val barW = 2.dp.toPx()
  drawLine(
    color = Color(0xFFFFFFFF),
    start = Offset(center.x - barLen / 2f, center.y),
    end = Offset(center.x + barLen / 2f, center.y),
    strokeWidth = barW,
    cap = StrokeCap.Round
  )
  drawLine(
    color = Color(0xFFFFFFFF),
    start = Offset(center.x, center.y - barLen / 2f),
    end = Offset(center.x, center.y + barLen / 2f),
    strokeWidth = barW,
    cap = StrokeCap.Round
  )

  // Orbiting satellite dot
  val satOffset = Offset(center.x + radius * 0.68f, center.y - radius * 0.65f)
  drawCircle(
    color = Color(0x18111111),
    radius = 3.5.dp.toPx(),
    center = Offset(satOffset.x, satOffset.y + 1.dp.toPx())
  )
  drawCircle(
    color = Color(0xFF171717),
    radius = 3.dp.toPx(),
    center = satOffset
  )
}

/**
 * 3D Connected People Network Object (Interlinked dimensional nodes)
 */
private fun DrawScope.drawSpatialCommunityObject(center: Offset, size: Float) {
  val nodeRadius = size * 0.16f
  val node1 = Offset(center.x - size * 0.17f, center.y + size * 0.08f)
  val node2 = Offset(center.x + size * 0.17f, center.y - size * 0.08f)

  // Bridge connector bar
  drawLine(
    color = Color(0xFF2C2C2C),
    start = node1,
    end = node2,
    strokeWidth = 3.dp.toPx(),
    cap = StrokeCap.Round
  )

  // Node 1 (Graphite porcelain)
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(Color(0xFF383838), Color(0xFF171717)),
      center = Offset(node1.x - 2.dp.toPx(), node1.y - 2.dp.toPx()),
      radius = nodeRadius * 1.2f
    ),
    radius = nodeRadius,
    center = node1
  )
  drawCircle(
    color = Color(0x35FFFFFF),
    radius = nodeRadius,
    center = node1,
    style = Stroke(width = 1.dp.toPx())
  )

  // Node 2 (Porcelain with graphite eye)
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(Color(0xFFFFFFFF), Color(0xFFE2E3E0)),
      center = Offset(node2.x - 2.dp.toPx(), node2.y - 2.dp.toPx()),
      radius = nodeRadius * 1.2f
    ),
    radius = nodeRadius,
    center = node2
  )
  drawCircle(
    color = Color(0x18111111),
    radius = nodeRadius,
    center = node2,
    style = Stroke(width = 1.dp.toPx())
  )
  drawCircle(
    color = Color(0xFF171717),
    radius = nodeRadius * 0.38f,
    center = node2
  )
}

/**
 * 3D Knowledge Archive Object (Stacked spatial crystal layers)
 */
private fun DrawScope.drawSpatialMemoryObject(center: Offset, size: Float) {
  val layerW = size * 0.54f
  val layerH = size * 0.16f
  val r = 6.dp.toPx()

  // Layer 3 (Bottom)
  drawRoundRect(
    brush = Brush.verticalGradient(
      colors = listOf(Color(0xFFD6D7D4), Color(0xFFC7C8C5)),
      startY = center.y + size * 0.12f,
      endY = center.y + size * 0.12f + layerH
    ),
    topLeft = Offset(center.x - layerW * 0.44f, center.y + size * 0.12f),
    size = Size(layerW * 0.88f, layerH),
    cornerRadius = CornerRadius(r, r)
  )

  // Layer 2 (Middle porcelain)
  drawRoundRect(
    brush = Brush.verticalGradient(
      colors = listOf(Color(0xFFFFFFFF), Color(0xFFE9EAE7)),
      startY = center.y - size * 0.02f,
      endY = center.y - size * 0.02f + layerH
    ),
    topLeft = Offset(center.x - layerW * 0.5f, center.y - size * 0.02f),
    size = Size(layerW, layerH),
    cornerRadius = CornerRadius(r, r)
  )
  drawRoundRect(
    color = Color(0x18111111),
    topLeft = Offset(center.x - layerW * 0.5f, center.y - size * 0.02f),
    size = Size(layerW, layerH),
    cornerRadius = CornerRadius(r, r),
    style = Stroke(width = 1.dp.toPx())
  )

  // Layer 1 (Top Graphite crystal)
  val topW = layerW * 0.65f
  val topH = layerH * 0.95f
  drawRoundRect(
    brush = Brush.verticalGradient(
      colors = listOf(Color(0xFF383838), Color(0xFF171717)),
      startY = center.y - size * 0.17f,
      endY = center.y - size * 0.17f + topH
    ),
    topLeft = Offset(center.x - topW * 0.5f, center.y - size * 0.17f),
    size = Size(topW, topH),
    cornerRadius = CornerRadius(r * 0.85f, r * 0.85f)
  )
  drawRoundRect(
    color = Color(0x35FFFFFF),
    topLeft = Offset(center.x - topW * 0.5f, center.y - size * 0.17f),
    size = Size(topW, topH),
    cornerRadius = CornerRadius(r * 0.85f, r * 0.85f),
    style = Stroke(width = 1.dp.toPx())
  )
}
