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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Question
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography

/**
 * KonnectLayeredDiscussions
 *
 * Distinctive layered horizontal discussion environment:
 * - Depth hierarchy: First card is stepped forward with elevated presence
 * - Subsequent cards subtly recede in scale and shadow
 * - Tactile spring touch interaction
 * - Dimensional topic objects and avatar clusters
 */
@Composable
fun KonnectLayeredDiscussions(
  questions: List<Question>,
  onDiscussionClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyRow(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.m),
    contentPadding = PaddingValues(horizontal = KonnectSpacing.xs)
  ) {
    itemsIndexed(questions, key = { _, q -> q.id }) { index, question ->
      LayeredDiscussionItem(
        question = question,
        isFirst = index == 0,
        onClick = { onDiscussionClick(question.id) },
        testTag = "discussion_layered_${question.id}"
      )
    }
  }
}

@Composable
private fun LayeredDiscussionItem(
  question: Question,
  isFirst: Boolean,
  onClick: () -> Unit,
  testTag: String
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val baseScale = if (isFirst) 1.0f else 0.97f
  val scale by animateFloatAsState(
    targetValue = if (isPressed) baseScale * 0.97f else baseScale,
    animationSpec = spring(dampingRatio = 0.75f, stiffness = 500f),
    label = "item_scale"
  )

  val cardElevation = if (isFirst) 7.dp else 3.dp

  Box(
    modifier = Modifier
      .width(280.dp)
      .testTag(testTag)
      .graphicsLayer {
        scaleX = scale
        scaleY = scale
      }
      .drawBehind {
        // Soft contact shadow
        drawRoundRect(
          color = if (isFirst) Color(0x10111111) else Color(0x08111111),
          topLeft = Offset(0f, 6.dp.toPx()),
          size = Size(size.width, size.height),
          cornerRadius = androidx.compose.ui.geometry.CornerRadius(22.dp.toPx(), 22.dp.toPx())
        )
      }
      .shadow(
        elevation = cardElevation,
        shape = RoundedCornerShape(22.dp),
        ambientColor = Color(0x10171717),
        spotColor = Color(0x18171717)
      )
      .clip(RoundedCornerShape(22.dp))
      .background(KonnectPrimarySurface)
      .drawBehind {
        // Specular glaze reflection
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
        color = if (isFirst) Color(0x22111111) else Color(0x14111111),
        shape = RoundedCornerShape(22.dp)
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .padding(KonnectSpacing.l)
  ) {
    Column {
      // Top Row: Category Pill & Activity time
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          KonnectPill(
            text = question.category,
            variant = if (isFirst) KonnectPillVariant.Primary else KonnectPillVariant.Secondary
          )
          if (question.technologies.isNotEmpty()) {
            KonnectPill(
              text = question.technologies.first(),
              variant = KonnectPillVariant.Surface
            )
          }
        }

        Text(
          text = question.timeAgo,
          style = Typography.labelSmall.copy(fontSize = 11.sp),
          color = KonnectSecondaryText
        )
      }

      Spacer(modifier = Modifier.height(KonnectSpacing.m))

      // Title
      Text(
        text = question.title,
        style = Typography.titleMedium.copy(fontSize = 16.sp, lineHeight = 21.sp),
        color = KonnectPrimaryText,
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(KonnectSpacing.xs))

      // Short excerpt
      Text(
        text = question.description,
        style = Typography.bodySmall,
        color = KonnectSecondaryText,
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(KonnectSpacing.l))

      // Bottom: Avatar + replies + arrow affordance
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          KonnectAvatar(
            initials = question.authorName,
            size = KonnectAvatarSize.Small,
            testTag = "layered_avatar_${question.id}"
          )
          Spacer(modifier = Modifier.width(KonnectSpacing.xs))
          Text(
            text = question.authorName,
            style = Typography.labelSmall,
            color = KonnectSecondaryText
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "${question.replyCount} replies",
            style = Typography.labelSmall,
            color = KonnectPrimaryDarkAction
          )
          Spacer(modifier = Modifier.width(3.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = KonnectPrimaryDarkAction,
            modifier = Modifier.size(13.dp)
          )
        }
      }
    }
  }
}
