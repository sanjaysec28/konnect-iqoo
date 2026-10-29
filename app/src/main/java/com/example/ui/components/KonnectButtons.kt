package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.KonnectBorder
import com.example.ui.theme.KonnectBorderSubtle
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectShapes
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography

/**
 * KonnectPrimaryButton
 *
 * Dark charcoal action button (#111111):
 * - Physical soft 3D presence with ambient studio lighting
 * - Top edge specular highlight
 * - Large 16dp rounded corners
 * - Accessible minimum 48dp height
 * - Tactile press scale micro-interaction
 */
@Composable
fun KonnectPrimaryButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  leadingIcon: ImageVector? = null,
  trailingIcon: ImageVector? = null,
  fullWidth: Boolean = true,
  testTag: String = "konnect_primary_button"
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed && enabled) 0.97f else 1f,
    animationSpec = spring(dampingRatio = 0.75f, stiffness = 500f),
    label = "primary_btn_scale"
  )

  val currentElevation = if (isPressed) 1.dp else 4.dp

  Box(
    modifier = modifier
      .testTag(testTag)
      .minimumInteractiveComponentSize()
      .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
      .defaultMinSize(minHeight = 52.dp)
      .graphicsLayer {
        scaleX = scale
        scaleY = scale
      }
      .then(
        if (enabled) {
          Modifier
            .drawBehind {
              drawRoundRect(
                color = Color(0x18111111),
                topLeft = Offset(0f, 3.dp.toPx()),
                size = Size(size.width, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx())
              )
            }
            .shadow(
              elevation = currentElevation,
              shape = KonnectShapes.Button,
              ambientColor = Color(0x22111111),
              spotColor = Color(0x30111111)
            )
        } else {
          Modifier
        }
      )
      .clip(KonnectShapes.Button)
      .background(
        if (enabled) KonnectPrimaryDarkAction else Color(0xFFDCDDD9)
      )
      .then(
        if (enabled) {
          Modifier.drawBehind {
            drawRect(
              brush = Brush.verticalGradient(
                colors = listOf(
                  KonnectHighlight.copy(alpha = 0.28f),
                  Color.Transparent
                ),
                startY = 0f,
                endY = 3.dp.toPx()
              )
            )
          }
        } else {
          Modifier
        }
      )
      .then(
        if (enabled) {
          Modifier.clickable(
            interactionSource = interactionSource,
            indication = ripple(bounded = true, color = KonnectHighlight),
            onClick = onClick
          )
        } else {
          Modifier
        }
      )
      .padding(
        horizontal = KonnectSpacing.xl,
        vertical = KonnectSpacing.m
      ),
    contentAlignment = Alignment.Center
  ) {
    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (leadingIcon != null) {
        Icon(
          imageVector = leadingIcon,
          contentDescription = null,
          tint = if (enabled) KonnectHighlight else KonnectSecondaryText,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(KonnectSpacing.xs))
      }
      Text(
        text = text,
        style = Typography.labelLarge,
        color = if (enabled) KonnectHighlight else KonnectSecondaryText
      )
      if (trailingIcon != null) {
        Spacer(modifier = Modifier.width(KonnectSpacing.xs))
        Icon(
          imageVector = trailingIcon,
          contentDescription = null,
          tint = if (enabled) KonnectHighlight else KonnectSecondaryText,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

/**
 * KonnectSecondaryButton
 *
 * Soft physical surface button (#FFFFFF):
 * - Soft ambient shadow and perimeter border
 * - Contrast dark text (#171717)
 * - 16dp rounded corners
 * - Accessible 48dp+ touch target
 * - Tactile press scale micro-interaction
 */
@Composable
fun KonnectSecondaryButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  leadingIcon: ImageVector? = null,
  trailingIcon: ImageVector? = null,
  fullWidth: Boolean = true,
  testTag: String = "konnect_secondary_button"
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed && enabled) 0.97f else 1f,
    animationSpec = spring(dampingRatio = 0.75f, stiffness = 500f),
    label = "secondary_btn_scale"
  )

  val currentElevation = if (isPressed) 0.5.dp else 2.dp

  Box(
    modifier = modifier
      .testTag(testTag)
      .minimumInteractiveComponentSize()
      .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
      .defaultMinSize(minHeight = 52.dp)
      .graphicsLayer {
        scaleX = scale
        scaleY = scale
      }
      .then(
        if (enabled) {
          Modifier
            .drawBehind {
              drawRoundRect(
                color = Color(0x0A111111),
                topLeft = Offset(0f, 2.dp.toPx()),
                size = Size(size.width, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx())
              )
            }
            .shadow(
              elevation = currentElevation,
              shape = KonnectShapes.Button,
              ambientColor = Color(0x10111111),
              spotColor = Color(0x14111111)
            )
        } else {
          Modifier
        }
      )
      .clip(KonnectShapes.Button)
      .background(KonnectPrimarySurface)
      .border(
        width = 1.dp,
        color = KonnectBorder,
        shape = KonnectShapes.Button
      )
      .then(
        if (enabled) {
          Modifier.clickable(
            interactionSource = interactionSource,
            indication = ripple(bounded = true),
            onClick = onClick
          )
        } else {
          Modifier
        }
      )
      .padding(
        horizontal = KonnectSpacing.xl,
        vertical = KonnectSpacing.m
      ),
    contentAlignment = Alignment.Center
  ) {
    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (leadingIcon != null) {
        Icon(
          imageVector = leadingIcon,
          contentDescription = null,
          tint = if (enabled) KonnectPrimaryText else KonnectSecondaryText,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(KonnectSpacing.xs))
      }
      Text(
        text = text,
        style = Typography.labelLarge,
        color = if (enabled) KonnectPrimaryText else KonnectSecondaryText
      )
      if (trailingIcon != null) {
        Spacer(modifier = Modifier.width(KonnectSpacing.xs))
        Icon(
          imageVector = trailingIcon,
          contentDescription = null,
          tint = if (enabled) KonnectPrimaryText else KonnectSecondaryText,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}
