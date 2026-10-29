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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FolderSpecial
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.navigation.KonnectDestination
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectShapes
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography

data class KonnectNavTab(
  val destination: KonnectDestination,
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val isProminentAction: Boolean = false
)

val MainNavTabs = listOf(
  KonnectNavTab(
    destination = KonnectDestination.Home,
    label = "Home",
    selectedIcon = Icons.Filled.Home,
    unselectedIcon = Icons.Outlined.Home
  ),
  KonnectNavTab(
    destination = KonnectDestination.Community,
    label = "Community",
    selectedIcon = Icons.Filled.People,
    unselectedIcon = Icons.Outlined.People
  ),
  KonnectNavTab(
    destination = KonnectDestination.Ask,
    label = "Ask",
    selectedIcon = Icons.Filled.Add,
    unselectedIcon = Icons.Filled.Add,
    isProminentAction = true
  ),
  KonnectNavTab(
    destination = KonnectDestination.Memory,
    label = "Memory",
    selectedIcon = Icons.Filled.FolderSpecial,
    unselectedIcon = Icons.Outlined.FolderSpecial
  ),
  KonnectNavTab(
    destination = KonnectDestination.Profile,
    label = "Profile",
    selectedIcon = Icons.Filled.Person,
    unselectedIcon = Icons.Outlined.Person
  )
)

/**
 * KonnectTopBar
 * Minimal, clean top bar with KONNECT identity, avatar, and notification actions
 */
@Composable
fun KonnectTopBar(
  modifier: Modifier = Modifier,
  title: String = "KONNECT",
  subtitle: String? = "Questions find the right people.",
  avatarInitials: String? = null,
  onAvatarClick: (() -> Unit)? = null,
  onNotificationsClick: (() -> Unit)? = null,
  onSettingsClick: (() -> Unit)? = null,
  navigationIcon: (@Composable () -> Unit)? = null
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .padding(
        horizontal = KonnectSpacing.screenHorizontal,
        vertical = KonnectSpacing.s
      ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f)
    ) {
      if (navigationIcon != null) {
        navigationIcon()
        Spacer(modifier = Modifier.size(KonnectSpacing.xs))
      }
      if (avatarInitials != null) {
        KonnectAvatar(
          initials = avatarInitials,
          size = KonnectAvatarSize.Small,
          modifier = Modifier
            .padding(end = KonnectSpacing.s)
            .then(
              if (onAvatarClick != null) Modifier.clickable(onClick = onAvatarClick) else Modifier
            )
        )
      }
      Column {
        Text(
          text = title,
          style = Typography.titleLarge.copy(fontSize = 18.sp, letterSpacing = 0.5.sp),
          color = KonnectPrimaryText
        )
        if (subtitle != null) {
          Text(
            text = subtitle,
            style = Typography.labelSmall,
            color = KonnectSecondaryText
          )
        }
      }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
      if (onNotificationsClick != null) {
        IconButton(
          onClick = onNotificationsClick,
          modifier = Modifier.testTag("top_bar_notifications_button")
        ) {
          Icon(
            imageVector = Icons.Default.NotificationsNone,
            contentDescription = "Notifications",
            tint = KonnectPrimaryText
          )
        }
      }
      if (onSettingsClick != null) {
        IconButton(
          onClick = onSettingsClick,
          modifier = Modifier.testTag("top_bar_settings_button")
        ) {
          Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = KonnectPrimaryText
          )
        }
      }
    }
  }
}

/**
 * KonnectBottomNavBar
 *
 * Distinctive Floating Spatial Navigation Dock:
 * - Elevated floating dock container with ambient diffusion shadow
 * - Central ASK button physically floats higher above the dock
 * - Selected items feature tactile elevation, soft shadow, and scale
 */
@Composable
fun KonnectBottomNavBar(
  currentRoute: String?,
  onNavigate: (KonnectDestination) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .padding(horizontal = KonnectSpacing.m, vertical = KonnectSpacing.xs)
  ) {
    // 1. Floating Spatial Dock Container
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .drawBehind {
          // Soft lower contact shadow
          drawRoundRect(
            color = Color(0x0E111111),
            topLeft = Offset(0f, 6.dp.toPx()),
            size = Size(size.width, size.height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(26.dp.toPx(), 26.dp.toPx())
          )
        }
        .shadow(
          elevation = 10.dp,
          shape = RoundedCornerShape(26.dp),
          ambientColor = Color(0x14171717),
          spotColor = Color(0x1E171717)
        )
        .clip(RoundedCornerShape(26.dp))
        .background(KonnectPrimarySurface)
        // Top specular reflection line
        .drawBehind {
          drawRect(
            brush = Brush.verticalGradient(
              colors = listOf(KonnectHighlight.copy(alpha = 0.95f), Color.Transparent),
              startY = 0f,
              endY = 4.dp.toPx()
            )
          )
        }
        .border(
          width = 1.dp,
          color = Color(0x18111111),
          shape = RoundedCornerShape(26.dp)
        )
        .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        MainNavTabs.forEach { tab ->
          val isSelected = currentRoute == tab.destination.route

          if (tab.isProminentAction) {
            // Prominent physical ASK orb floating above the dock
            FloatingSpatialAskItem(
              isSelected = isSelected,
              onClick = { onNavigate(tab.destination) }
            )
          } else {
            // Standard Navigation Item with tactile elevation
            SpatialNavItem(
              tab = tab,
              isSelected = isSelected,
              onClick = { onNavigate(tab.destination) }
            )
          }
        }
      }
    }
  }
}

/**
 * The primary action ASK orb, physically floating above the dock
 */
@Composable
private fun FloatingSpatialAskItem(
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.90f else if (isSelected) 1.05f else 1.0f,
    animationSpec = spring(dampingRatio = 0.7f, stiffness = 600f),
    label = "ask_scale"
  )

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .testTag("bottom_nav_tab_ask")
      .offset(y = (-14).dp) // Physically floating above the dock
      .graphicsLayer {
        scaleX = scale
        scaleY = scale
      }
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
  ) {
    Box(
      modifier = Modifier
        .size(54.dp)
        .drawBehind {
          // Contact shadow under the floating orb
          drawCircle(
            color = Color(0x35111111),
            radius = 24.dp.toPx(),
            center = Offset(size.width / 2f, size.height / 2f + 5.dp.toPx())
          )
        }
        .shadow(
          elevation = if (isSelected) 8.dp else 5.dp,
          shape = CircleShape,
          ambientColor = Color(0x28111111),
          spotColor = Color(0x38111111)
        )
        .clip(CircleShape)
        .background(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFF2E2E2E), KonnectPrimaryDarkAction),
            center = Offset(22f, 22f),
            radius = 65f
          )
        )
        .border(1.5.dp, Color(0x40FFFFFF), CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "Ask",
        tint = KonnectHighlight,
        modifier = Modifier.size(26.dp)
      )
    }

    Spacer(modifier = Modifier.height(2.dp))

    Text(
      text = "Ask",
      style = Typography.labelSmall.copy(fontSize = 11.sp),
      color = KonnectPrimaryText
    )
  }
}

@Composable
private fun SpatialNavItem(
  tab: KonnectNavTab,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.92f else if (isSelected) 1.04f else 1.0f,
    animationSpec = spring(dampingRatio = 0.75f, stiffness = 500f),
    label = "tab_scale"
  )

  Column(
    modifier = Modifier
      .testTag("bottom_nav_tab_${tab.destination.route}")
      .graphicsLayer {
        scaleX = scale
        scaleY = scale
      }
      .clip(RoundedCornerShape(14.dp))
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 6.dp, vertical = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(width = 46.dp, height = 30.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(
          if (isSelected) Color(0xFFE8E9E6) else Color.Transparent
        )
        .then(
          if (isSelected) {
            Modifier.border(1.dp, Color(0x14111111), RoundedCornerShape(12.dp))
          } else {
            Modifier
          }
        ),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
        contentDescription = tab.label,
        tint = if (isSelected) KonnectPrimaryDarkAction else KonnectSecondaryText,
        modifier = Modifier.size(20.dp)
      )
    }

    Spacer(modifier = Modifier.height(2.dp))

    Text(
      text = tab.label,
      style = Typography.labelSmall.copy(fontSize = 11.sp),
      color = if (isSelected) KonnectPrimaryText else KonnectSecondaryText
    )
  }
}
