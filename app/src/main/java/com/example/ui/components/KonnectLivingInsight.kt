package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectShapes
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography
import kotlin.math.cos
import kotlin.math.sin

/**
 * KonnectLivingInsight
 *
 * Distinctive living data object:
 * - Dimensional circular knowledge structure
 * - Revolving connected satellite intelligence nodes
 * - Center numeral: 142
 * - Living community intelligence visualization
 */
@Composable
fun KonnectLivingInsight(
  solvedCount: Int = 142,
  modifier: Modifier = Modifier,
  testTag: String = "konnect_living_insight"
) {
  val infiniteTransition = rememberInfiniteTransition(label = "living_insight_motion")
  val rotationAngle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (2 * Math.PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(12000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "insight_orbit"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag)
      // Soft lower contact shadow
      .drawBehind {
        drawRoundRect(
          color = Color(0x0A111111),
          topLeft = Offset(0f, 6.dp.toPx()),
          size = Size(size.width, size.height),
          cornerRadius = androidx.compose.ui.geometry.CornerRadius(22.dp.toPx(), 22.dp.toPx())
        )
      }
      .shadow(
        elevation = 4.dp,
        shape = RoundedCornerShape(22.dp),
        ambientColor = Color(0x0E171717),
        spotColor = Color(0x14171717)
      )
      .clip(RoundedCornerShape(22.dp))
      .background(KonnectPrimarySurface)
      .drawBehind {
        // Top specular reflection glaze
        drawRect(
          brush = Brush.verticalGradient(
            colors = listOf(KonnectHighlight.copy(alpha = 0.95f), Color.Transparent),
            startY = 0f,
            endY = 5.dp.toPx()
          )
        )
      }
      .border(
        width = 1.dp,
        color = Color(0x15111111),
        shape = RoundedCornerShape(22.dp)
      )
      .padding(horizontal = KonnectSpacing.l, vertical = KonnectSpacing.m)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 1. Living Knowledge Wheel (3D Object)
      Box(
        modifier = Modifier.size(76.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.size(76.dp)) {
          val center = Offset(size.width / 2f, size.height / 2f)
          val outerRadius = size.minDimension * 0.44f

          // Outer faint orbital track
          drawCircle(
            color = Color(0x15111111),
            radius = outerRadius,
            center = center,
            style = Stroke(
              width = 1.2.dp.toPx(),
              pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 6f))
            )
          )

          // 3 Orbiting Connected Nodes
          val nodeCount = 4
          for (i in 0 until nodeCount) {
            val angle = rotationAngle + (i * 2 * Math.PI / nodeCount).toFloat()
            val nodePos = Offset(
              center.x + cos(angle) * outerRadius,
              center.y + sin(angle) * outerRadius
            )

            // Connection line to center
            drawLine(
              color = Color(0x12111111),
              start = center,
              end = nodePos,
              strokeWidth = 1.dp.toPx()
            )

            // Connection chord between adjacent nodes
            val nextAngle = rotationAngle + ((i + 1) * 2 * Math.PI / nodeCount).toFloat()
            val nextNodePos = Offset(
              center.x + cos(nextAngle) * outerRadius,
              center.y + sin(nextAngle) * outerRadius
            )
            drawLine(
              color = Color(0x0C111111),
              start = nodePos,
              end = nextNodePos,
              strokeWidth = 1.dp.toPx()
            )

            // Node contact shadow
            drawCircle(
              color = Color(0x18111111),
              radius = 4.dp.toPx(),
              center = Offset(nodePos.x, nodePos.y + 1.5.dp.toPx())
            )

            // Pearl satellite node
            drawCircle(
              brush = Brush.radialGradient(
                colors = listOf(Color.White, Color(0xFFD6D7D4)),
                center = Offset(nodePos.x - 1.dp.toPx(), nodePos.y - 1.dp.toPx()),
                radius = 3.5.dp.toPx()
              ),
              radius = 3.5.dp.toPx(),
              center = nodePos
            )
          }

          // Core Porcelain Hub
          val hubRadius = outerRadius * 0.62f
          drawCircle(
            brush = Brush.radialGradient(
              colors = listOf(Color(0xFFFFFFFF), Color(0xFFF0F1EE), Color(0xFFDCDDD9)),
              center = Offset(center.x - hubRadius * 0.3f, center.y - hubRadius * 0.35f),
              radius = hubRadius * 1.3f
            ),
            radius = hubRadius,
            center = center
          )
          drawCircle(
            color = Color(0x16111111),
            radius = hubRadius,
            center = center,
            style = Stroke(width = 1.dp.toPx())
          )

          // Inner dark focal eye
          drawCircle(
            color = KonnectPrimaryDarkAction,
            radius = hubRadius * 0.32f,
            center = center
          )
          drawCircle(
            color = Color(0xCCFFFFFF),
            radius = hubRadius * 0.1f,
            center = Offset(center.x - hubRadius * 0.08f, center.y - hubRadius * 0.1f)
          )
        }
      }

      Spacer(modifier = Modifier.width(KonnectSpacing.l))

      // 2. Intelligence Metric Text
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(7.dp)
              .clip(CircleShape)
              .background(KonnectPrimaryDarkAction)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "LIVING INTELLIGENCE",
            style = Typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 0.8.sp),
            color = KonnectSecondaryText
          )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "$solvedCount",
            style = Typography.headlineLarge.copy(fontSize = 28.sp),
            color = KonnectPrimaryText
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "problems solved this week",
            style = Typography.titleSmall,
            color = KonnectPrimaryText
          )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = "Community knowledge continuously compounding across 6 domains.",
          style = Typography.bodySmall,
          color = KonnectSecondaryText
        )
      }
    }
  }
}
