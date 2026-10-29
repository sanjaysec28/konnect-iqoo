package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.di.KonnectContainer
import com.example.ui.components.KonnectAICore
import com.example.ui.components.KonnectAICoreState
import com.example.ui.components.KonnectBottomNavBar
import com.example.ui.components.KonnectTopBar
import com.example.ui.screens.AIProcessingScreen
import com.example.ui.screens.AIResultsScreen
import com.example.ui.screens.AISummaryScreen
import com.example.ui.screens.AskScreen
import com.example.ui.screens.CommunityScreen
import com.example.ui.screens.DesignSystemPreviewScreen
import com.example.ui.screens.DiscussionScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MemoryScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.KonnectBackground
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography
import com.example.viewmodel.AIWorkflowViewModel
import com.example.viewmodel.AskViewModel
import com.example.viewmodel.CommunityViewModel
import com.example.viewmodel.HomeViewModel
import com.example.viewmodel.KonnectViewModelFactory
import com.example.viewmodel.MemoryViewModel
import com.example.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KonnectApp(
  container: KonnectContainer,
  initialSharedText: String? = null,
  initialSharedImageUri: String? = null,
  navController: NavHostController = rememberNavController()
) {
  val factory = KonnectViewModelFactory(container)
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  val isPrimaryTab = KonnectDestination.primaryDestinations.any { it.route == currentRoute }
  val isOnline by container.networkMonitor.isOnline.collectAsState(initial = true)

  val askViewModel: AskViewModel = viewModel(factory = factory)
  val aiWorkflowViewModel: AIWorkflowViewModel = viewModel(factory = factory)

  // Signature KONNECT Opening Transition (800-1000ms awakening)
  var isLaunching by remember { mutableStateOf(initialSharedText == null && initialSharedImageUri == null) }

  LaunchedEffect(Unit) {
    if (isLaunching) {
      delay(950)
      isLaunching = false
    }
  }

  if (isLaunching) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(KonnectBackground),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        KonnectAICore(
          size = 92.dp,
          state = KonnectAICoreState.IDLE,
          testTag = "launch_ai_core"
        )
        Spacer(modifier = Modifier.height(KonnectSpacing.l))
        Text(
          text = "KONNECT",
          style = Typography.headlineLarge.copy(fontSize = 30.sp, letterSpacing = 2.sp),
          color = KonnectPrimaryText
        )
        Spacer(modifier = Modifier.height(KonnectSpacing.xs))
        Text(
          text = "Questions find the right people.",
          style = Typography.bodyMedium,
          color = KonnectSecondaryText
        )
      }
    }
    return
  }

  // Handle inbound Android Share intent
  LaunchedEffect(initialSharedText, initialSharedImageUri) {
    if (initialSharedText != null || initialSharedImageUri != null) {
      askViewModel.setInitialContent(
        title = initialSharedText ?: "",
        description = if (initialSharedImageUri != null) "Shared screenshot attached for architectural analysis." else "",
        imageUri = initialSharedImageUri
      )
      navController.navigate(KonnectDestination.Ask.route) {
        popUpTo(navController.graph.findStartDestination().id) {
          saveState = true
        }
        launchSingleTop = true
      }
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = KonnectBackground,
    topBar = {
      Column {
        if (!isOnline) {
          // Subtle spatial status strip
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .statusBarsPadding()
              .padding(horizontal = KonnectSpacing.m, vertical = 4.dp),
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE8E9E6))
                .border(1.dp, Color(0x18111111), RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.CloudOff,
                  contentDescription = null,
                  tint = KonnectSecondaryText,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "You're offline. Showing saved community knowledge.",
                  style = Typography.labelSmall.copy(fontSize = 11.sp),
                  color = KonnectPrimaryText
                )
              }
            }
          }
        }

        if (isPrimaryTab) {
          KonnectTopBar(
            avatarInitials = "Alex",
            onAvatarClick = {
              navController.navigate(KonnectDestination.Profile.route)
            },
            onNotificationsClick = {
              navController.navigate(KonnectDestination.Notifications.route)
            },
            onSettingsClick = {
              navController.navigate(KonnectDestination.Settings.route)
            }
          )
        }
      }
    },
    bottomBar = {
      if (isPrimaryTab) {
        KonnectBottomNavBar(
          currentRoute = currentRoute,
          onNavigate = { destination ->
            navController.navigate(destination.route) {
              popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
              }
              launchSingleTop = true
              restoreState = true
            }
          }
        )
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(KonnectBackground)
        .padding(paddingValues)
    ) {
      NavHost(
        navController = navController,
        startDestination = KonnectDestination.Home.route,
        modifier = Modifier.fillMaxSize()
      ) {
        // 1. Home
        composable(KonnectDestination.Home.route) {
          val homeViewModel: HomeViewModel = viewModel(factory = factory)
          HomeScreen(
            viewModel = homeViewModel,
            onNavigateToAsk = {
              navController.navigate(KonnectDestination.Ask.route)
            },
            onNavigateToAskWithVoice = {
              askViewModel.activateVoiceDirectly()
              navController.navigate(KonnectDestination.Ask.route)
            },
            onNavigateToDiscussion = { questionId ->
              navController.navigate("${KonnectDestination.Discussion.route}/$questionId")
            },
            onNavigateToCommunity = {
              navController.navigate(KonnectDestination.Community.route)
            },
            onNavigateToMemory = {
              navController.navigate(KonnectDestination.Memory.route)
            },
            onNavigateToNotifications = {
              navController.navigate(KonnectDestination.Notifications.route)
            }
          )
        }

        // 2. Ask
        composable(KonnectDestination.Ask.route) {
          AskScreen(
            viewModel = askViewModel,
            onQuestionSubmitted = { questionId ->
              navController.navigate("${KonnectDestination.AIProcessing.route}/$questionId")
            }
          )
        }

        // 3. Community
        composable(KonnectDestination.Community.route) {
          val communityViewModel: CommunityViewModel = viewModel(factory = factory)
          CommunityScreen(
            viewModel = communityViewModel,
            onQuestionClick = { questionId ->
              navController.navigate("${KonnectDestination.Discussion.route}/$questionId")
            },
            onConnectClick = { builder ->
              aiWorkflowViewModel.openConnectSheet(builder)
              navController.navigate("${KonnectDestination.AIResults.route}/q-001")
            }
          )
        }

        // 4. Memory
        composable(KonnectDestination.Memory.route) {
          val memoryViewModel: MemoryViewModel = viewModel(factory = factory)
          MemoryScreen(
            viewModel = memoryViewModel,
            onAskAboutThis = { inquiryTitle ->
              askViewModel.setInitialContent(
                title = "Follow-up on $inquiryTitle",
                description = "Referencing architectural insights and verified solutions from Community Memory."
              )
              navController.navigate(KonnectDestination.Ask.route)
            }
          )
        }

        // 5. Profile
        composable(KonnectDestination.Profile.route) {
          val profileViewModel: ProfileViewModel = viewModel(factory = factory)
          ProfileScreen(
            viewModel = profileViewModel,
            onNavigateToSettings = {
              navController.navigate(KonnectDestination.Settings.route)
            }
          )
        }

        // 6. AI Processing
        composable(
          route = "${KonnectDestination.AIProcessing.route}/{questionId}",
          arguments = listOf(navArgument("questionId") { type = NavType.StringType; defaultValue = "q-001" })
        ) { backStackEntry ->
          val questionId = backStackEntry.arguments?.getString("questionId") ?: "q-001"
          AIProcessingScreen(
            viewModel = aiWorkflowViewModel,
            questionId = questionId,
            onBack = { navController.popBackStack() },
            onNavigateToResults = {
              navController.navigate("${KonnectDestination.AIResults.route}/$questionId")
            }
          )
        }

        composable(KonnectDestination.AIProcessing.route) {
          AIProcessingScreen(
            viewModel = aiWorkflowViewModel,
            questionId = "q-001",
            onBack = { navController.popBackStack() },
            onNavigateToResults = {
              navController.navigate("${KonnectDestination.AIResults.route}/q-001")
            }
          )
        }

        // 7. AI Results
        composable(
          route = "${KonnectDestination.AIResults.route}/{questionId}",
          arguments = listOf(navArgument("questionId") { type = NavType.StringType; defaultValue = "q-001" })
        ) { backStackEntry ->
          val questionId = backStackEntry.arguments?.getString("questionId") ?: "q-001"
          AIResultsScreen(
            viewModel = aiWorkflowViewModel,
            onBack = { navController.popBackStack() },
            onNavigateToDiscussion = { discId ->
              navController.navigate("${KonnectDestination.Discussion.route}/$discId")
            },
            onNavigateToMemoryDetail = {
              navController.navigate(KonnectDestination.Memory.route)
            }
          )
        }

        composable(KonnectDestination.AIResults.route) {
          AIResultsScreen(
            viewModel = aiWorkflowViewModel,
            onBack = { navController.popBackStack() },
            onNavigateToDiscussion = { discId ->
              navController.navigate("${KonnectDestination.Discussion.route}/$discId")
            },
            onNavigateToMemoryDetail = {
              navController.navigate(KonnectDestination.Memory.route)
            }
          )
        }

        // 8. Discussion Room
        composable(
          route = "${KonnectDestination.Discussion.route}/{questionId}",
          arguments = listOf(navArgument("questionId") { type = NavType.StringType })
        ) { backStackEntry ->
          val questionId = backStackEntry.arguments?.getString("questionId") ?: "q-001"
          DiscussionScreen(
            viewModel = aiWorkflowViewModel,
            questionId = questionId,
            onBack = { navController.popBackStack() },
            onNavigateToSummary = {
              navController.navigate(KonnectDestination.AISummary.route)
            }
          )
        }

        // 9. AI Summary
        composable(KonnectDestination.AISummary.route) {
          AISummaryScreen(
            viewModel = aiWorkflowViewModel,
            onBack = { navController.popBackStack() },
            onSaveToMemory = {
              navController.navigate(KonnectDestination.Memory.route) {
                popUpTo(KonnectDestination.Home.route)
              }
            }
          )
        }

        // 10. Notifications
        composable(KonnectDestination.Notifications.route) {
          val communityViewModel: CommunityViewModel = viewModel(factory = factory)
          NotificationsScreen(
            viewModel = communityViewModel,
            onBack = { navController.popBackStack() },
            onNavigateToDiscussion = { questionId ->
              navController.navigate("${KonnectDestination.Discussion.route}/$questionId")
            },
            onNavigateToMemory = {
              navController.navigate(KonnectDestination.Memory.route)
            }
          )
        }

        // 11. Settings
        composable(KonnectDestination.Settings.route) {
          SettingsScreen(
            onBack = { navController.popBackStack() },
            onNavigateToDesignSystem = {
              navController.navigate(KonnectDestination.DesignSystemPreview.route)
            }
          )
        }

        // 12. Design System Showcase
        composable(KonnectDestination.DesignSystemPreview.route) {
          DesignSystemPreviewScreen(
            onBack = { navController.popBackStack() }
          )
        }
      }
    }
  }
}
