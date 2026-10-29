package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.KonnectBorderSubtle
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectShapes
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography

/**
 * KonnectHeroCard
 *
 * Premium physical hero container for major focal points and inquiries:
 * - 24dp rounded geometry
 * - Soft layered studio lighting & contact shadow
 * - Optional subtle embossed decorative contour background geometry
 * - Supports status badge pill, prominent headline, secondary description,
 *   primary CTA, secondary CTA, and floating 3D object
 */
@Composable
fun KonnectHeroCard(
  title: String,
  subtitle: String,
  modifier: Modifier = Modifier,
  badgeText: String? = null,
  showDecorativeGeometry: Boolean = true,
  floatingObject: (@Composable () -> Unit)? = null,
  actionButton: (@Composable () -> Unit)? = null,
  secondaryActionButton: (@Composable () -> Unit)? = null,
  testTag: String = "konnect_hero_card"
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag)
      // Soft lower contact shadow + ambient aura
      .drawBehind {
        drawRoundRect(
          color = Color(0x0C111111),
          topLeft = Offset(0f, 6.dp.toPx()),
          size = Size(size.width, size.height),
          cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
        )
      }
      .shadow(
        elevation = 6.dp,
        shape = KonnectShapes.HeroCard,
        ambientColor = Color(0x10171717),
        spotColor = Color(0x18171717)
      )
      .clip(KonnectShapes.HeroCard)
      .background(KonnectPrimarySurface)
      // Top inner physical reflection highlight
      .drawBehind {
        drawRect(
          brush = Brush.verticalGradient(
            colors = listOf(
              KonnectHighlight.copy(alpha = 0.95f),
              KonnectHighlight.copy(alpha = 0.0f)
            ),
            startY = 0f,
            endY = 6.dp.toPx()
          )
        )

        // Subtle restrained decorative physical embossed contour lines in background
        if (showDecorativeGeometry) {
          drawCircle(
            color = Color(0x06111111),
            radius = size.width * 0.45f,
            center = Offset(size.width * 0.95f, size.height * 0.15f),
            style = Stroke(width = 1.dp.toPx())
          )
          drawCircle(
            color = Color(0x04111111),
            radius = size.width * 0.65f,
            center = Offset(size.width * 0.95f, size.height * 0.15f),
            style = Stroke(width = 1.dp.toPx())
          )
        }
      }
      .border(
        width = 1.dp,
        color = KonnectBorderSubtle,
        shape = KonnectShapes.HeroCard
      )
      .padding(KonnectSpacing.cardPaddingHero)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth()
    ) {
      if (badgeText != null) {
        KonnectPill(
          text = badgeText,
          variant = KonnectPillVariant.Secondary
        )
        Spacer(modifier = Modifier.height(KonnectSpacing.m))
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = title,
            style = Typography.headlineLarge,
            color = KonnectPrimaryText
          )
          Spacer(modifier = Modifier.height(KonnectSpacing.xs))
          Text(
            text = subtitle,
            style = Typography.bodyMedium,
            color = KonnectSecondaryText
          )
        }

        if (floatingObject != null) {
          Box(
            modifier = Modifier.padding(start = KonnectSpacing.m),
            contentAlignment = Alignment.Center
          ) {
            floatingObject()
          }
        }
      }

      if (actionButton != null || secondaryActionButton != null) {
        Spacer(modifier = Modifier.height(KonnectSpacing.l))
        if (actionButton != null && secondaryActionButton != null) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.m),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(modifier = Modifier.weight(1f)) { actionButton() }
            Box(modifier = Modifier.weight(1f)) { secondaryActionButton() }
          }
        } else if (actionButton != null) {
          actionButton()
        } else {
          secondaryActionButton?.invoke()
        }
      }
    }
  }
}
