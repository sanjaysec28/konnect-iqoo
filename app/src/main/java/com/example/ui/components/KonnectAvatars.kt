package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KonnectBadgeSuccess
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondarySurface
import com.example.ui.theme.Typography

enum class KonnectAvatarSize(val dp: Dp, val fontSize: Int) {
  Small(32.dp, 12),
  Medium(44.dp, 15),
  Large(64.dp, 20),
  Profile(80.dp, 26)
}

/**
 * KonnectAvatar
 *
 * Tactile, realistic builder avatar:
 * - High-contrast graphite or porcelain material finish
 * - Optional online indicator
 * - Optional relevance matching indicator score (e.g. 98% match)
 * - Accessible and responsive
 */
@Composable
fun KonnectAvatar(
  initials: String,
  modifier: Modifier = Modifier,
  size: KonnectAvatarSize = KonnectAvatarSize.Medium,
  isOnline: Boolean = false,
  relevanceScore: Int? = null,
  isDarkVariant: Boolean = true,
  testTag: String = "konnect_avatar"
) {
  Box(
    modifier = modifier.testTag(testTag),
    contentAlignment = Alignment.Center
  ) {
    // Main Avatar Surface
    Box(
      modifier = Modifier
        .size(size.dp)
        .shadow(
          elevation = 2.dp,
          shape = CircleShape,
          ambientColor = Color(0x12111111),
          spotColor = Color(0x18111111)
        )
        .clip(CircleShape)
        .background(
          if (isDarkVariant) {
            Brush.linearGradient(
              colors = listOf(Color(0xFF262626), KonnectPrimaryDarkAction)
            )
          } else {
            Brush.linearGradient(
              colors = listOf(KonnectHighlight, KonnectSecondarySurface)
            )
          }
        )
        .border(
          width = 1.dp,
          color = if (isDarkVariant) Color(0x28FFFFFF) else Color(0x16111111),
          shape = CircleShape
        ),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = initials.take(2).uppercase(),
        style = Typography.titleMedium.copy(
          fontSize = size.fontSize.sp,
          color = if (isDarkVariant) KonnectHighlight else KonnectPrimaryText
        )
      )
    }

    // Online Status Indicator Dot
    if (isOnline) {
      val dotSize = (size.dp * 0.24f).coerceAtLeast(8.dp).coerceAtMost(16.dp)
      Box(
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .offset(x = 1.dp, y = 1.dp)
          .size(dotSize)
          .clip(CircleShape)
          .background(KonnectBadgeSuccess)
          .border(1.5.dp, KonnectPrimarySurface, CircleShape)
      )
    }

    // Relevance Match Indicator Badge
    if (relevanceScore != null) {
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .offset(x = 6.dp, y = (-4).dp)
          .shadow(2.dp, RoundedCornerShape(999.dp))
          .clip(RoundedCornerShape(999.dp))
          .background(KonnectPrimaryDarkAction)
          .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(999.dp))
          .padding(horizontal = 5.dp, vertical = 1.5.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "$relevanceScore%",
          style = Typography.labelSmall.copy(
            fontSize = 9.sp,
            color = KonnectHighlight
          )
        )
      }
    }
  }
}
