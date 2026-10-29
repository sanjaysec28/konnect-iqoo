package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * KonnectAICoreState
 * 8 physical states representing the AI Core cognitive lifecycle:
 * - IDLE: slow floating motion + gentle breathe
 * - LISTENING: gentle radial acoustic pulse
 * - THINKING: small orbiting satellite nodes
 * - SEARCHING: neural connection filaments traveling outward
 * - FINDING_PEOPLE: avatar nodes emerge and link with connection bridges
 * - FINDING_DISCUSSIONS: knowledge capsules appear and dock with core
 * - SUCCESS: subtle physical expansion & illumination settle
 * - ERROR: subtle physical contraction
 */
enum class KonnectAICoreState {
  IDLE,
  LISTENING,
  THINKING,
  SEARCHING,
  FINDING_PEOPLE,
  FINDING_DISCUSSIONS,
  SUCCESS,
  ERROR
}

/**
 * KonnectAICore
 *
 * The signature 3D AI visual identity of KONNECT:
 * - Smooth pearl-like sphere
 * - Matte white outer porcelain shell
 * - Tiny dark graphite center
 * - Subtle internal specular highlight
 * - Soft ambient contact shadow
 * - Physically grounded floating appearance
 * - Premium product-render quality (Apple/Linear industrial aesthetic)
 */
@Composable
fun KonnectAICore(
  modifier: Modifier = Modifier,
  size: Dp = 80.dp,
  state: KonnectAICoreState = KonnectAICoreState.IDLE,
  testTag: String = "konnect_ai_core"
) {
  val infiniteTransition = rememberInfiniteTransition(label = "ai_core_motion")

  // Floating oscillation (Depth translation)
  val floatOffset by infiniteTransition.animateFloat(
    initialValue = -3.5f,
    targetValue = 3.5f,
    animationSpec = infiniteRepeatable(
      animation = tween(2800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "float_y"
  )

  // Subtle breathing scale
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = if (state == KonnectAICoreState.LISTENING) 0.93f else 0.985f,
    targetValue = if (state == KonnectAICoreState.LISTENING) 1.07f else 1.015f,
    animationSpec = infiniteRepeatable(
      animation = tween(if (state == KonnectAICoreState.LISTENING) 1100 else 2400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_scale"
  )

  // Satellite node orbit rotation angle (in radians)
  val orbitAngle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (2 * Math.PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(4800, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "orbit_angle"
  )

  // Neural beam phase for SEARCHING
  val beamPhase by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(1600, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "beam_phase"
  )

  // Physical expansion/contraction state transition
  val stateScale = remember { Animatable(1f) }
  LaunchedEffect(state) {
    when (state) {
      KonnectAICoreState.SUCCESS -> stateScale.animateTo(1.08f, tween(350, easing = FastOutSlowInEasing))
      KonnectAICoreState.ERROR -> stateScale.animateTo(0.92f, tween(300, easing = FastOutSlowInEasing))
      else -> stateScale.animateTo(1f, tween(400, easing = FastOutSlowInEasing))
    }
  }

  val finalScale = pulseScale * stateScale.value

  Box(
    modifier = modifier
      .size(size)
      .offset(y = floatOffset.dp)
      .testTag(testTag),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.size(size)) {
      val center = Offset(this.size.width / 2f, this.size.height / 2f)
      val coreRadius = (this.size.minDimension / 2f) * 0.65f * finalScale

      // 1. Soft contact shadow on ambient floor plane
      drawAmbientShadow(
        center = center.copy(y = center.y + coreRadius * 0.95f - floatOffset * 0.4f),
        radius = coreRadius * 0.9f
      )

      // 2. State-specific background emissions
      when (state) {
        KonnectAICoreState.LISTENING -> {
          drawAcousticRings(center = center, radius = coreRadius, pulse = pulseScale)
        }
        KonnectAICoreState.SEARCHING -> {
          drawSearchFilaments(center = center, radius = coreRadius, phase = beamPhase)
        }
        KonnectAICoreState.THINKING -> {
          drawOrbitTracks(center = center, radius = coreRadius * 1.35f)
        }
        KonnectAICoreState.FINDING_PEOPLE -> {
          drawPeopleEcosystem(center = center, radius = coreRadius * 1.38f, angle = orbitAngle)
        }
        KonnectAICoreState.FINDING_DISCUSSIONS -> {
          drawDiscussionsEcosystem(center = center, radius = coreRadius * 1.38f, angle = orbitAngle)
        }
        else -> {}
      }

      // 3. Matte Porcelain Shell (3D Studio Lighting)
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(
            Color(0xFFFFFFFF),       // Specular apex
            Color(0xFFF7F7F6),       // Diffuse pearl
            Color(0xFFE5E6E3),       // Terminator
            Color(0xFFD6D7D4)        // Bounce shadow
          ),
          center = Offset(center.x - coreRadius * 0.35f, center.y - coreRadius * 0.4f),
          radius = coreRadius * 1.35f
        ),
        radius = coreRadius,
        center = center
      )

      // 4. Subtle Porcelain Outer Bevel Rim
      drawCircle(
        color = Color(0x18111111),
        radius = coreRadius,
        center = center,
        style = Stroke(width = 1.2.dp.toPx())
      )

      // 5. Top Specular Glaze Arc
      drawArc(
        brush = Brush.radialGradient(
          colors = listOf(Color(0xE6FFFFFF), Color(0x00FFFFFF)),
          center = Offset(center.x - coreRadius * 0.3f, center.y - coreRadius * 0.4f),
          radius = coreRadius * 0.7f
        ),
        startAngle = 180f,
        sweepAngle = 120f,
        useCenter = false,
        topLeft = Offset(center.x - coreRadius * 0.8f, center.y - coreRadius * 0.85f),
        size = androidx.compose.ui.geometry.Size(coreRadius * 1.6f, coreRadius * 1.7f),
        style = Stroke(width = coreRadius * 0.32f, cap = StrokeCap.Round)
      )

      // 6. Tiny Dark Graphite Focal Core (Intelligence center)
      val innerRadius = coreRadius * 0.28f
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(
            Color(0xFF383838),       // Charcoal top
            Color(0xFF171717),       // Deep graphite
            Color(0xFF0D0D0D)        // Shadow pit
          ),
          center = Offset(center.x - innerRadius * 0.25f, center.y - innerRadius * 0.3f),
          radius = innerRadius * 1.2f
        ),
        radius = innerRadius,
        center = center
      )

      // 7. Internal Specular Pin-Point (Living Intelligence spark)
      drawCircle(
        color = Color(0xCCFFFFFF),
        radius = innerRadius * 0.22f,
        center = Offset(center.x - innerRadius * 0.32f, center.y - innerRadius * 0.35f)
      )

      // 8. Orbiting Satellite Nodes for THINKING and SEARCHING states
      if (state == KonnectAICoreState.THINKING || state == KonnectAICoreState.SEARCHING) {
        drawSatelliteNodes(center = center, radius = coreRadius * 1.35f, angle = orbitAngle)
      }
    }
  }
}

/**
 * Draws the soft ambient floor contact shadow
 */
private fun DrawScope.drawAmbientShadow(center: Offset, radius: Float) {
  drawOval(
    brush = Brush.radialGradient(
      colors = listOf(Color(0x24111111), Color(0x0E111111), Color.Transparent),
      center = center,
      radius = radius
    ),
    topLeft = Offset(center.x - radius, center.y - radius * 0.3f),
    size = androidx.compose.ui.geometry.Size(radius * 2f, radius * 0.6f)
  )
  drawOval(
    brush = Brush.radialGradient(
      colors = listOf(Color(0x10111111), Color.Transparent),
      center = center,
      radius = radius * 1.4f
    ),
    topLeft = Offset(center.x - radius * 1.4f, center.y - radius * 0.42f),
    size = androidx.compose.ui.geometry.Size(radius * 2.8f, radius * 0.84f)
  )
}

/**
 * Draws subtle acoustic listening waves
 */
private fun DrawScope.drawAcousticRings(center: Offset, radius: Float, pulse: Float) {
  val wave1 = radius * 1.25f * pulse
  val wave2 = radius * 1.55f * pulse
  drawCircle(
    color = Color(0x16111111),
    radius = wave1,
    center = center,
    style = Stroke(width = 1.5.dp.toPx())
  )
  drawCircle(
    color = Color(0x0C111111),
    radius = wave2,
    center = center,
    style = Stroke(width = 1.dp.toPx())
  )
}

/**
 * Draws neural search filaments radiating outward
 */
private fun DrawScope.drawSearchFilaments(center: Offset, radius: Float, phase: Float) {
  val beamLength = radius * 1.4f
  for (i in 0 until 4) {
    val angle = (i * Math.PI / 2.0 + phase * Math.PI / 2.0).toFloat()
    val end = Offset(
      center.x + cos(angle) * beamLength,
      center.y + sin(angle) * beamLength
    )
    drawLine(
      brush = Brush.linearGradient(
        colors = listOf(Color(0x40111111), Color.Transparent),
        start = center,
        end = end
      ),
      start = center,
      end = end,
      strokeWidth = 2.dp.toPx(),
      cap = StrokeCap.Round
    )
  }
}

/**
 * Draws subtle orbital tracks
 */
private fun DrawScope.drawOrbitTracks(center: Offset, radius: Float) {
  drawCircle(
    color = Color(0x10111111),
    radius = radius,
    center = center,
    style = Stroke(width = 1.dp.toPx(), pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(6f, 6f)))
  )
}

/**
 * Draws satellite nodes orbiting the AI Core
 */
private fun DrawScope.drawSatelliteNodes(center: Offset, radius: Float, angle: Float) {
  val nodeCount = 3
  for (i in 0 until nodeCount) {
    val nodeAngle = angle + (i * 2 * Math.PI / nodeCount).toFloat()
    val nodeCenter = Offset(
      center.x + cos(nodeAngle) * radius,
      center.y + sin(nodeAngle) * radius * 0.75f
    )

    drawCircle(
      color = Color(0x14111111),
      radius = 4.5.dp.toPx(),
      center = Offset(nodeCenter.x, nodeCenter.y + 2.dp.toPx())
    )

    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color.White, Color(0xFFD6D7D4)),
        center = Offset(nodeCenter.x - 1.dp.toPx(), nodeCenter.y - 1.dp.toPx()),
        radius = 4.dp.toPx()
      ),
      radius = 3.5.dp.toPx(),
      center = nodeCenter
    )

    drawCircle(
      color = Color(0xFF171717),
      radius = 1.2.dp.toPx(),
      center = nodeCenter
    )

    drawLine(
      color = Color(0x12111111),
      start = center,
      end = nodeCenter,
      strokeWidth = 1.dp.toPx()
    )
  }
}

/**
 * Draws people/avatar nodes emerging and linking with connection bridges (FINDING_PEOPLE)
 */
private fun DrawScope.drawPeopleEcosystem(center: Offset, radius: Float, angle: Float) {
  val avatarCount = 3
  for (i in 0 until avatarCount) {
    val nodeAngle = angle + (i * 2 * Math.PI / avatarCount).toFloat()
    val nodeCenter = Offset(
      center.x + cos(nodeAngle) * radius,
      center.y + sin(nodeAngle) * radius * 0.75f
    )

    // Pulsing bridge
    drawLine(
      color = Color(0xFF171717).copy(alpha = 0.35f),
      start = center,
      end = nodeCenter,
      strokeWidth = 2.dp.toPx(),
      cap = StrokeCap.Round
    )

    // Avatar pearl
    drawCircle(
      color = Color(0x20111111),
      radius = 6.dp.toPx(),
      center = Offset(nodeCenter.x, nodeCenter.y + 2.dp.toPx())
    )
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color.White, Color(0xFFE2E3E0)),
        center = Offset(nodeCenter.x - 2.dp.toPx(), nodeCenter.y - 2.dp.toPx()),
        radius = 5.dp.toPx()
      ),
      radius = 5.5.dp.toPx(),
      center = nodeCenter
    )
    // Dark architect node
    drawCircle(
      color = Color(0xFF171717),
      radius = 2.2.dp.toPx(),
      center = nodeCenter
    )
  }
}

/**
 * Draws discussion / knowledge capsules emerging and docking with core (FINDING_DISCUSSIONS)
 */
private fun DrawScope.drawDiscussionsEcosystem(center: Offset, radius: Float, angle: Float) {
  val discCount = 3
  for (i in 0 until discCount) {
    val nodeAngle = -angle + (i * 2 * Math.PI / discCount).toFloat()
    val nodeCenter = Offset(
      center.x + cos(nodeAngle) * radius,
      center.y + sin(nodeAngle) * radius * 0.75f
    )

    drawLine(
      color = Color(0x25111111),
      start = center,
      end = nodeCenter,
      strokeWidth = 1.5.dp.toPx(),
      cap = StrokeCap.Round
    )

    // Mini capsule rectangle
    val w = 12.dp.toPx()
    val h = 7.dp.toPx()
    drawRoundRect(
      brush = Brush.verticalGradient(
        colors = listOf(Color.White, Color(0xFFDCDDD9)),
        startY = nodeCenter.y - h / 2f,
        endY = nodeCenter.y + h / 2f
      ),
      topLeft = Offset(nodeCenter.x - w / 2f, nodeCenter.y - h / 2f),
      size = androidx.compose.ui.geometry.Size(w, h),
      cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
    )
    drawRoundRect(
      color = Color(0x20111111),
      topLeft = Offset(nodeCenter.x - w / 2f, nodeCenter.y - h / 2f),
      size = androidx.compose.ui.geometry.Size(w, h),
      cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
      style = Stroke(width = 0.8.dp.toPx())
    )
  }
}
