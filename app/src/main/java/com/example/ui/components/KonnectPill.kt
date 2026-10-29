package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.KonnectBorder
import com.example.ui.theme.KonnectBorderSubtle
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPillBackground
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondarySurface
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectShapes
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography

enum class KonnectPillVariant {
  Primary,
  Secondary,
  Surface,
  Outline
}

/**
 * KonnectPill
 *
 * Soft, physical 3D pill indicator or tag:
 * - 999dp pill curvature
 * - Tactile soft feel
 * - Supports leading icon/indicator dot
 */
@Composable
fun KonnectPill(
  text: String,
  modifier: Modifier = Modifier,
  variant: KonnectPillVariant = KonnectPillVariant.Secondary,
  isSelected: Boolean = false,
  leadingDotColor: Color? = null,
  onClick: (() -> Unit)? = null,
  testTag: String = "konnect_pill"
) {
  val (bgColor, textColor, borderColor, elevation) = when {
    isSelected || variant == KonnectPillVariant.Primary -> {
      Tuple4(
        KonnectPrimaryDarkAction,
        KonnectHighlight,
        KonnectPrimaryDarkAction,
        2.dp
      )
    }
    variant == KonnectPillVariant.Secondary -> {
      Tuple4(
        KonnectSecondarySurface,
        KonnectPrimaryText,
        KonnectBorderSubtle,
        0.5.dp
      )
    }
    variant == KonnectPillVariant.Surface -> {
      Tuple4(
        KonnectPrimarySurface,
        KonnectPrimaryText,
        KonnectBorder,
        1.5.dp
      )
    }
    else -> {
      Tuple4(
        Color.Transparent,
        KonnectSecondaryText,
        KonnectBorder,
        0.dp
      )
    }
  }

  val interactionSource = remember { MutableInteractionSource() }

  Box(
    modifier = modifier
      .testTag(testTag)
      .then(
        if (elevation > 0.dp) {
          Modifier.shadow(
            elevation = elevation,
            shape = KonnectShapes.Pill,
            ambientColor = Color(0x10000000),
            spotColor = Color(0x14000000)
          )
        } else {
          Modifier
        }
      )
      .clip(KonnectShapes.Pill)
      .background(bgColor)
      .border(
        width = 1.dp,
        color = borderColor,
        shape = KonnectShapes.Pill
      )
      .then(
        if (onClick != null) {
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
        horizontal = KonnectSpacing.m,
        vertical = KonnectSpacing.xs
      ),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (leadingDotColor != null) {
        Box(
          modifier = Modifier
            .size(6.dp)
            .clip(KonnectShapes.Pill)
            .background(leadingDotColor)
        )
        Spacer(modifier = Modifier.width(KonnectSpacing.xs))
      }
      Text(
        text = text,
        style = Typography.labelMedium,
        color = textColor
      )
    }
  }
}

private data class Tuple4<A, B, C, D>(
  val a: A,
  val b: B,
  val c: C,
  val d: D
)
