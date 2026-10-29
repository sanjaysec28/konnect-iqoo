package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * konnectTactilePress
 *
 * Physical touch response modifier:
 * 1. Scale down (0.96f - 0.98f)
 * 2. Positional depth shift (translationY)
 * 3. Tiny rotation (-0.8f to -1.5f degrees)
 * 4. Crisp spring release physics
 */
fun Modifier.konnectTactilePress(
  pressedScale: Float = 0.965f,
  pressedTranslationY: Float = 3f,
  pressedRotation: Float = -0.8f,
  enabled: Boolean = true,
  onClick: (() -> Unit)? = null
): Modifier = composed {
  if (!enabled) return@composed this

  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) pressedScale else 1f,
    animationSpec = spring(dampingRatio = 0.72f, stiffness = 600f),
    label = "tactile_scale"
  )

  val translationY by animateFloatAsState(
    targetValue = if (isPressed) pressedTranslationY else 0f,
    animationSpec = spring(dampingRatio = 0.72f, stiffness = 600f),
    label = "tactile_trans_y"
  )

  val rotationZ by animateFloatAsState(
    targetValue = if (isPressed) pressedRotation else 0f,
    animationSpec = spring(dampingRatio = 0.75f, stiffness = 550f),
    label = "tactile_rot_z"
  )

  this
    .graphicsLayer {
      scaleX = scale
      scaleY = scale
      this.translationY = translationY * density
      this.rotationZ = rotationZ
    }
    .then(
      if (onClick != null) {
        Modifier.clickable(
          interactionSource = interactionSource,
          indication = null,
          onClick = onClick
        )
      } else {
        Modifier
      }
    )
}
