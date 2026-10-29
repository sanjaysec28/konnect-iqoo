package com.example.ui.navigation

/**
 * Konnect Destinations
 * Centralized sealed class defining all primary and secondary routes in KONNECT.
 */
sealed class KonnectDestination(val route: String) {
  // Primary Core Navigation Tabs
  data object Home : KonnectDestination("home")
  data object Ask : KonnectDestination("ask")
  data object Community : KonnectDestination("community")
  data object Memory : KonnectDestination("memory")
  data object Profile : KonnectDestination("profile")

  // Secondary & Future Core Experience Routes
  data object AIProcessing : KonnectDestination("ai_processing")
  data object AIResults : KonnectDestination("ai_results")
  data object Discussion : KonnectDestination("discussion")
  data object AISummary : KonnectDestination("ai_summary")
  data object Notifications : KonnectDestination("notifications")
  data object Settings : KonnectDestination("settings")
  data object DesignSystemPreview : KonnectDestination("design_system_preview")

  companion object {
    val primaryDestinations = listOf(Home, Community, Ask, Memory, Profile)
    val secondaryDestinations = listOf(
      AIProcessing,
      AIResults,
      Discussion,
      AISummary,
      Notifications,
      Settings,
      DesignSystemPreview
    )

    fun fromRoute(route: String?): KonnectDestination {
      return when (route) {
        Home.route -> Home
        Ask.route -> Ask
        Community.route -> Community
        Memory.route -> Memory
        Profile.route -> Profile
        AIProcessing.route -> AIProcessing
        AIResults.route -> AIResults
        Discussion.route -> Discussion
        AISummary.route -> AISummary
        Notifications.route -> Notifications
        Settings.route -> Settings
        DesignSystemPreview.route -> DesignSystemPreview
        else -> Home
      }
    }
  }
}
