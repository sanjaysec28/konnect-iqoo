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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectSecondarySurface

enum class KonnectObjectType {
  AI,           // Abstract intelligence sphere / core node
  Voice,        // Soft 3D microphone / listening orb
  Knowledge,    // Layered floating document / cards
  Community,    // Connected rounded network nodes
  Memory        // Stacked translucent / solid knowledge cards
}

/**
 * KonnectFloatingObject
 *
 * Abstract, tactile 3D product illustrations:
 * - Subtle geometric layering
 * - Realistic studio soft lighting & specular highlights
 * - Matte porcelain and graphite finishes
 * - Gentle floating micro-interaction
 */
@Composable
fun KonnectFloatingObject(
  modifier: Modifier = Modifier,
  size: Dp = 64.dp,
  type: KonnectObjectType = KonnectObjectType.AI,
  isAnimated: Boolean = true,
  testTag: String = "konnect_floating_object"
) {
  val infiniteTransition = rememberInfiniteTransition(label = "floating_transition")
  val floatOffset by infiniteTransition.animateFloat(
    initialValue = if (isAnimated) -2.5f else 0f,
    targetValue = if (isAnimated) 2.5f else 0f,
    animationSpec = infiniteRepeatable(
      animation = tween(2400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "float_offset"
  )

  Box(
    modifier = modifier
      .size(size)
      .offset(y = floatOffset.dp)
      .testTag("${testTag}_${type.name.lowercase()}"),
    contentAlignment = Alignment.Center
  ) {
    // 1. Contact shadow on floor plane
    Box(
      modifier = Modifier
        .size(size * 0.85f)
        .offset(y = (size * 0.12f) - (floatOffset * 0.4f).dp)
        .shadow(
          elevation = 10.dp,
          shape = CircleShape,
          ambientColor = Color(0x22111111),
          spotColor = Color(0x2A111111)
        )
    )

    when (type) {
      KonnectObjectType.AI -> AISphereObject(size = size)
      KonnectObjectType.Voice -> VoiceOrbObject(size = size)
      KonnectObjectType.Knowledge -> KnowledgeCardsObject(size = size)
      KonnectObjectType.Community -> CommunityNodesObject(size = size)
      KonnectObjectType.Memory -> MemoryStackObject(size = size)
    }
  }
}

/**
 * AI Sphere Object: Abstract intelligence sphere with soft graphite inner core and porcelain orbital rim
 */
@Composable
private fun AISphereObject(size: Dp) {
  Box(
    modifier = Modifier
      .size(size)
      .clip(RoundedCornerShape(size * 0.35f))
      .background(
        brush = Brush.radialGradient(
          colors = listOf(
            KonnectHighlight,
            KonnectPrimarySurface,
            KonnectSecondarySurface
          ),
          center = Offset(size.value * 0.35f, size.value * 0.35f),
          radius = size.value * 1.2f
        )
      )
      .drawBehind {
        // Specular top highlight
        drawRect(
          brush = Brush.verticalGradient(
            colors = listOf(KonnectHighlight.copy(alpha = 0.95f), Color.Transparent),
            startY = 0f,
            endY = 4.dp.toPx()
          )
        )
      }
      .border(1.dp, Color(0x14111111), RoundedCornerShape(size * 0.35f)),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .size(size * 0.52f)
        .clip(CircleShape)
        .background(
          brush = Brush.linearGradient(
            colors = listOf(Color(0xFF2C2C2C), KonnectPrimaryDarkAction),
            start = Offset.Zero,
            end = Offset.Infinite
          )
        )
        .border(1.dp, Color(0x35FFFFFF), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.AutoAwesome,
        contentDescription = null,
        tint = KonnectHighlight,
        modifier = Modifier.size(size * 0.26f)
      )
    }
  }
}

/**
 * Voice Orb Object: Soft 3D microphone orb with tactile acoustic pill geometry
 */
@Composable
private fun VoiceOrbObject(size: Dp) {
  Box(
    modifier = Modifier
      .size(size)
      .clip(CircleShape)
      .background(
        brush = Brush.radialGradient(
          colors = listOf(
            KonnectHighlight,
            KonnectPrimarySurface,
            KonnectSecondarySurface
          ),
          center = Offset(size.value * 0.3f, size.value * 0.3f),
          radius = size.value * 1.1f
        )
      )
      .border(1.dp, Color(0x18111111), CircleShape),
    contentAlignment = Alignment.Center
  ) {
    // Subtle acoustic wave ring
    Canvas(modifier = Modifier.size(size * 0.76f)) {
      drawCircle(
        color = Color(0x12111111),
        style = Stroke(width = 1.5.dp.toPx())
      )
    }
    Box(
      modifier = Modifier
        .size(size * 0.48f)
        .clip(CircleShape)
        .background(KonnectPrimaryDarkAction),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Mic,
        contentDescription = null,
        tint = KonnectHighlight,
        modifier = Modifier.size(size * 0.24f)
      )
    }
  }
}

/**
 * Knowledge Cards Object: Layered floating architectural documents
 */
@Composable
private fun KnowledgeCardsObject(size: Dp) {
  Box(
    modifier = Modifier.size(size),
    contentAlignment = Alignment.Center
  ) {
    // Background tilted document
    Box(
      modifier = Modifier
        .size(width = size * 0.68f, height = size * 0.72f)
        .offset(x = 4.dp, y = (-3).dp)
        .clip(RoundedCornerShape(8.dp))
        .background(KonnectSecondarySurface)
        .border(1.dp, Color(0x12111111), RoundedCornerShape(8.dp))
    )
    // Foreground primary document
    Box(
      modifier = Modifier
        .size(width = size * 0.72f, height = size * 0.76f)
        .shadow(4.dp, RoundedCornerShape(10.dp), ambientColor = Color(0x14111111), spotColor = Color(0x18111111))
        .clip(RoundedCornerShape(10.dp))
        .background(KonnectPrimarySurface)
        .border(1.dp, Color(0x16111111), RoundedCornerShape(10.dp))
        .padding(size * 0.12f),
      contentAlignment = Alignment.Center
    ) {
      Canvas(modifier = Modifier.size(size * 0.42f)) {
        // Subtle document lines
        drawRoundRect(
          color = Color(0xFF171717),
          topLeft = Offset(0f, 0f),
          size = Size(size.toPx() * 0.32f, 3.dp.toPx()),
          cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
        drawRoundRect(
          color = Color(0xFF888888),
          topLeft = Offset(0f, 6.dp.toPx()),
          size = Size(size.toPx() * 0.25f, 2.5.dp.toPx()),
          cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
        drawRoundRect(
          color = Color(0xFF888888),
          topLeft = Offset(0f, 11.dp.toPx()),
          size = Size(size.toPx() * 0.20f, 2.5.dp.toPx()),
          cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
      }
    }
  }
}

/**
 * Community Nodes Object: Connected rounded nodes with physical bridge
 */
@Composable
private fun CommunityNodesObject(size: Dp) {
  Box(
    modifier = Modifier.size(size),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.size(size)) {
      val center = Offset(size.toPx() / 2f, size.toPx() / 2f)
      val node1 = Offset(center.x - size.toPx() * 0.24f, center.y + size.toPx() * 0.12f)
      val node2 = Offset(center.x + size.toPx() * 0.24f, center.y - size.toPx() * 0.12f)

      // Bridge connection bar
      drawLine(
        color = Color(0xFF2C2C2C),
        start = node1,
        end = node2,
        strokeWidth = 3.dp.toPx(),
        cap = androidx.compose.ui.graphics.StrokeCap.Round
      )
      drawLine(
        color = Color(0x30FFFFFF),
        start = node1,
        end = node2,
        strokeWidth = 1.dp.toPx(),
        cap = androidx.compose.ui.graphics.StrokeCap.Round
      )

      // Node 1 (Graphite)
      drawCircle(
        color = Color(0x22111111),
        radius = size.toPx() * 0.18f + 2.dp.toPx(),
        center = Offset(node1.x, node1.y + 2.dp.toPx())
      )
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color(0xFF333333), Color(0xFF111111)),
          center = Offset(node1.x - 2.dp.toPx(), node1.y - 2.dp.toPx()),
          radius = size.toPx() * 0.25f
        ),
        radius = size.toPx() * 0.18f,
        center = node1
      )

      // Node 2 (Porcelain with graphite center)
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color.White, Color(0xFFECEDEB)),
          center = Offset(node2.x - 2.dp.toPx(), node2.y - 2.dp.toPx()),
          radius = size.toPx() * 0.22f
        ),
        radius = size.toPx() * 0.17f,
        center = node2
      )
      drawCircle(
        color = Color(0xFF111111),
        radius = size.toPx() * 0.08f,
        center = node2
      )
    }
  }
}

/**
 * Memory Stack Object: Stacked translucent/solid knowledge stones
 */
@Composable
private fun MemoryStackObject(size: Dp) {
  Box(
    modifier = Modifier.size(size),
    contentAlignment = Alignment.Center
  ) {
    // Bottom stone
    Box(
      modifier = Modifier
        .size(width = size * 0.72f, height = size * 0.24f)
        .offset(y = (size * 0.20f))
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFE2E3E0))
        .border(1.dp, Color(0x18111111), RoundedCornerShape(8.dp))
    )
    // Middle stone
    Box(
      modifier = Modifier
        .size(width = size * 0.62f, height = size * 0.22f)
        .offset(y = (size * 0.02f))
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFFFFFFF))
        .border(1.dp, Color(0x14111111), RoundedCornerShape(8.dp))
    )
    // Top stone (Dark focal stone)
    Box(
      modifier = Modifier
        .size(width = size * 0.48f, height = size * 0.20f)
        .offset(y = (-size * 0.16f))
        .clip(RoundedCornerShape(7.dp))
        .background(KonnectPrimaryDarkAction)
        .border(1.dp, Color(0x35FFFFFF), RoundedCornerShape(7.dp)),
      contentAlignment = Alignment.Center
    ) {
      Canvas(modifier = Modifier.size(size * 0.16f)) {
        drawCircle(color = KonnectHighlight, radius = 2.dp.toPx())
      }
    }
  }
}
