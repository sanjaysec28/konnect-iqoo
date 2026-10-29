package com.example

import androidx.compose.ui.graphics.toArgb
import com.example.ui.navigation.KonnectDestination
import com.example.ui.theme.KonnectBackground
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectSecondarySurface
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class KonnectDesignSystemTest {

  @Test
  fun testColorTokensConsistency() {
    // Background: #F5F5F3
    assertEquals(0xFFF5F5F3.toInt(), KonnectBackground.toArgb())
    // Primary Surface: #FFFFFF
    assertEquals(0xFFFFFFFF.toInt(), KonnectPrimarySurface.toArgb())
    // Secondary Surface: #ECEDEB
    assertEquals(0xFFECEDEB.toInt(), KonnectSecondarySurface.toArgb())
    // Primary Action: #111111
    assertEquals(0xFF111111.toInt(), KonnectPrimaryDarkAction.toArgb())
  }

  @Test
  fun testDesignSystemPreviewDestinationResolved() {
    val destination = KonnectDestination.fromRoute("design_system_preview")
    assertEquals(KonnectDestination.DesignSystemPreview, destination)
  }

  @Test
  fun testPrimaryNavigationTabsCount() {
    // 5 core navigation tabs
    assertEquals(5, KonnectDestination.primaryDestinations.size)
    assertTrue(KonnectDestination.primaryDestinations.contains(KonnectDestination.Home))
    assertTrue(KonnectDestination.primaryDestinations.contains(KonnectDestination.Ask))
    assertTrue(KonnectDestination.primaryDestinations.contains(KonnectDestination.Community))
    assertTrue(KonnectDestination.primaryDestinations.contains(KonnectDestination.Memory))
    assertTrue(KonnectDestination.primaryDestinations.contains(KonnectDestination.Profile))
  }
}
