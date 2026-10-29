package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
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
 * KonnectTextField
 *
 * Minimal, tactile input field:
 * - White surface on soft grey background
 * - 16dp rounded corners
 * - Calm focus outline state (dark charcoal highlight rather than harsh primary colors)
 * - 48dp+ min height touch target
 */
@Composable
fun KonnectTextField(
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String,
  modifier: Modifier = Modifier,
  label: String? = null,
  singleLine: Boolean = true,
  minHeight: androidx.compose.ui.unit.Dp = 52.dp,
  leadingIcon: (@Composable () -> Unit)? = null,
  trailingIcon: (@Composable () -> Unit)? = null,
  testTag: String = "konnect_text_field"
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isFocused by interactionSource.collectIsFocusedAsState()

  val borderColor by animateColorAsState(
    targetValue = if (isFocused) KonnectPrimaryDarkAction else KonnectBorderSubtle,
    animationSpec = tween(180),
    label = "input_border_color"
  )

  Column(modifier = modifier.fillMaxWidth()) {
    if (label != null) {
      Text(
        text = label,
        style = Typography.labelSmall,
        color = if (isFocused) KonnectPrimaryText else KonnectSecondaryText
      )
      Spacer(modifier = Modifier.height(KonnectSpacing.xs))
    }

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = minHeight)
        .minimumInteractiveComponentSize()
        .shadow(
          elevation = if (isFocused) 3.dp else 1.dp,
          shape = KonnectShapes.InputField,
          ambientColor = Color(0x10111111),
          spotColor = Color(0x14111111)
        )
        .clip(KonnectShapes.InputField)
        .background(KonnectPrimarySurface)
        // Top specular highlight
        .drawBehind {
          drawRect(
            brush = Brush.verticalGradient(
              colors = listOf(KonnectHighlight.copy(alpha = 0.8f), Color.Transparent),
              startY = 0f,
              endY = 3.dp.toPx()
            )
          )
        }
        .border(
          width = if (isFocused) 1.5.dp else 1.dp,
          color = borderColor,
          shape = KonnectShapes.InputField
        )
        .padding(horizontal = KonnectSpacing.m, vertical = KonnectSpacing.s),
      contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top
      ) {
        if (leadingIcon != null) {
          leadingIcon()
          Spacer(modifier = Modifier.width(KonnectSpacing.s))
        }

        Box(modifier = Modifier.weight(1f)) {
          if (value.isEmpty()) {
            Text(
              text = placeholder,
              style = Typography.bodyLarge,
              color = KonnectSecondaryText
            )
          }
          BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = Typography.bodyLarge.copy(color = KonnectPrimaryText),
            cursorBrush = SolidColor(KonnectPrimaryDarkAction),
            interactionSource = interactionSource,
            modifier = Modifier
              .fillMaxWidth()
              .testTag(testTag)
          )
        }

        if (trailingIcon != null) {
          Spacer(modifier = Modifier.width(KonnectSpacing.s))
          trailingIcon()
        }
      }
    }
  }
}

/**
 * KonnectSearchField
 *
 * Dedicated search field with quick clear affordance and search icon
 */
@Composable
fun KonnectSearchField(
  query: String,
  onQueryChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  placeholder: String = "Search architectural topics, builders, tags...",
  onClear: (() -> Unit)? = null,
  testTag: String = "konnect_search_field"
) {
  KonnectTextField(
    value = query,
    onValueChange = onQueryChange,
    placeholder = placeholder,
    modifier = modifier,
    leadingIcon = {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "Search",
        tint = KonnectSecondaryText,
        modifier = Modifier.size(20.dp)
      )
    },
    trailingIcon = {
      if (query.isNotEmpty()) {
        IconButton(
          onClick = {
            onQueryChange("")
            onClear?.invoke()
          },
          modifier = Modifier.size(28.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Clear search",
            tint = KonnectSecondaryText,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    },
    testTag = testTag
  )
}

enum class KonnectVoiceState {
  Idle,
  Listening,
  Processing,
  Complete
}

/**
 * KonnectVoiceInput
 *
 * Tactile, calm physical voice input communicating:
 * Idle -> Listening -> Processing -> Complete
 * without loud neon or flashing aesthetics.
 */
@Composable
fun KonnectVoiceInput(
  state: KonnectVoiceState,
  onMicClick: () -> Unit,
  modifier: Modifier = Modifier,
  transcriptText: String = "",
  testTag: String = "konnect_voice_input"
) {
  val infiniteTransition = rememberInfiniteTransition(label = "voice_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = if (state == KonnectVoiceState.Listening) 1.15f else 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "voice_pulse_scale"
  )

  val interactionSource = remember { MutableInteractionSource() }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // 3D tactile voice pill button
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier.size(80.dp)
    ) {
      // Soft breathing aura when listening
      if (state == KonnectVoiceState.Listening) {
        Box(
          modifier = Modifier
            .size(76.dp)
            .scale(pulseScale)
            .clip(CircleShape)
            .background(Color(0x18111111))
        )
      }

      // Main physical orb
      Box(
        modifier = Modifier
          .size(60.dp)
          .shadow(
            elevation = 6.dp,
            shape = CircleShape,
            ambientColor = Color(0x18111111),
            spotColor = Color(0x24111111)
          )
          .clip(CircleShape)
          .background(
            when (state) {
              KonnectVoiceState.Listening -> KonnectPrimaryDarkAction
              KonnectVoiceState.Processing -> Color(0xFF2C2C2C)
              KonnectVoiceState.Complete -> KonnectPrimaryDarkAction
              KonnectVoiceState.Idle -> KonnectPrimarySurface
            }
          )
          .border(
            width = 1.dp,
            color = if (state == KonnectVoiceState.Idle) KonnectBorder else Color(0x30FFFFFF),
            shape = CircleShape
          )
          .clickable(
            interactionSource = interactionSource,
            indication = ripple(bounded = true),
            onClick = onMicClick
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = when (state) {
            KonnectVoiceState.Complete -> Icons.Default.Check
            KonnectVoiceState.Processing -> Icons.Default.Sync
            else -> Icons.Default.Mic
          },
          contentDescription = "Voice input",
          tint = if (state == KonnectVoiceState.Idle) KonnectPrimaryText else KonnectHighlight,
          modifier = Modifier.size(26.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(KonnectSpacing.s))

    // State Label
    Text(
      text = when (state) {
        KonnectVoiceState.Idle -> "Tap to speak inquiry"
        KonnectVoiceState.Listening -> "Listening to architectural query..."
        KonnectVoiceState.Processing -> "Transcribing and extracting keywords..."
        KonnectVoiceState.Complete -> "Inquiry transcribed"
      },
      style = Typography.labelMedium,
      color = if (state == KonnectVoiceState.Listening) KonnectPrimaryText else KonnectSecondaryText
    )

    if (transcriptText.isNotEmpty()) {
      Spacer(modifier = Modifier.height(KonnectSpacing.xs))
      Text(
        text = "\"$transcriptText\"",
        style = Typography.bodyMedium,
        color = KonnectPrimaryText,
        modifier = Modifier.padding(horizontal = KonnectSpacing.m)
      )
    }
  }
}
