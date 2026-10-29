package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// KONNECT Color Scheme (Locked Visual Identity: White / soft-grey environment)
private val KonnectColorScheme = lightColorScheme(
  primary = KonnectPrimaryDarkAction,
  onPrimary = KonnectHighlight,
  primaryContainer = KonnectSecondarySurface,
  onPrimaryContainer = KonnectPrimaryText,

  secondary = KonnectPrimaryText,
  onSecondary = KonnectHighlight,
  secondaryContainer = KonnectSecondarySurface,
  onSecondaryContainer = KonnectPrimaryText,

  tertiary = KonnectSecondaryText,
  onTertiary = KonnectHighlight,

  background = KonnectBackground,
  onBackground = KonnectPrimaryText,

  surface = KonnectPrimarySurface,
  onSurface = KonnectPrimaryText,
  surfaceVariant = KonnectSecondarySurface,
  onSurfaceVariant = KonnectSecondaryText,

  outline = KonnectBorder,
  outlineVariant = KonnectBorderSubtle,

  inverseSurface = KonnectPrimaryDarkAction,
  inverseOnSurface = KonnectHighlight
)

@Composable
fun KonnectTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = KonnectColorScheme,
    typography = Typography,
    shapes = Shapes,
    content = content
  )
}

// Retain alias for compatibility
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  KonnectTheme(content = content)
}
