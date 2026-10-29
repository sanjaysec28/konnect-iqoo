package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.components.KonnectAICore
import com.example.ui.components.KonnectAICoreState
import com.example.ui.components.KonnectPill
import com.example.ui.components.KonnectPillVariant
import com.example.ui.components.KonnectPrimaryButton
import com.example.ui.components.KonnectSecondaryButton
import com.example.ui.components.KonnectTextField
import com.example.ui.components.KonnectVoiceInput
import com.example.ui.components.KonnectVoiceState
import com.example.ui.theme.KonnectBackground
import com.example.ui.theme.KonnectHighlight
import com.example.ui.theme.KonnectPrimaryDarkAction
import com.example.ui.theme.KonnectPrimarySurface
import com.example.ui.theme.KonnectPrimaryText
import com.example.ui.theme.KonnectSecondarySurface
import com.example.ui.theme.KonnectSecondaryText
import com.example.ui.theme.KonnectShapes
import com.example.ui.theme.KonnectSpacing
import com.example.ui.theme.Typography
import com.example.viewmodel.AskViewModel
import java.io.ByteArrayOutputStream

@Composable
fun AskScreen(
  viewModel: AskViewModel,
  onQuestionSubmitted: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val context = LocalContext.current

  // 1. Android Photo Picker (Zero-permission)
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      viewModel.onImageSelected(uri.toString())
    }
  }

  // 2. Camera Capture Launcher
  val cameraCaptureLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicturePreview()
  ) { bitmap: Bitmap? ->
    if (bitmap != null) {
      val uri = saveBitmapToCache(context, bitmap)
      viewModel.onImageSelected(uri)
    }
  }

  // 3. Camera Permission Launcher
  val cameraPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted: Boolean ->
    if (isGranted) {
      cameraCaptureLauncher.launch(null)
    }
  }

  // 4. Audio Record Permission Launcher
  val audioPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted: Boolean ->
    viewModel.onVoiceClick(hasRecordAudioPermission = isGranted)
  }

  // Determine current AI Core State
  val coreState = when {
    uiState.isSubmitting -> KonnectAICoreState.SEARCHING
    uiState.voiceState == KonnectVoiceState.Listening -> KonnectAICoreState.LISTENING
    uiState.voiceState == KonnectVoiceState.Processing -> KonnectAICoreState.THINKING
    uiState.title.isNotBlank() -> KonnectAICoreState.THINKING
    else -> KonnectAICoreState.IDLE
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(KonnectBackground)
      .testTag("ask_screen")
      .padding(horizontal = KonnectSpacing.screenHorizontal),
    verticalArrangement = Arrangement.spacedBy(KonnectSpacing.l)
  ) {

    // 1. Spatial Engine Header: Prominent AI Core Environment
    item {
      Spacer(modifier = Modifier.height(KonnectSpacing.xs))
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .drawBehind {
            drawRoundRect(
              color = Color(0x0C111111),
              topLeft = Offset(0f, 6.dp.toPx()),
              size = Size(size.width, size.height),
              cornerRadius = androidx.compose.ui.geometry.CornerRadius(26.dp.toPx(), 26.dp.toPx())
            )
          }
          .shadow(
            elevation = 6.dp,
            shape = RoundedCornerShape(26.dp),
            ambientColor = Color(0x10171717),
            spotColor = Color(0x18171717)
          )
          .clip(RoundedCornerShape(26.dp))
          .background(KonnectPrimarySurface)
          .drawBehind {
            drawRect(
              brush = Brush.verticalGradient(
                colors = listOf(KonnectHighlight.copy(alpha = 0.95f), Color.Transparent),
                startY = 0f,
                endY = 5.dp.toPx()
              )
            )
          }
          .border(1.dp, Color(0x16111111), RoundedCornerShape(26.dp))
          .padding(KonnectSpacing.cardPaddingHero)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            KonnectPill(
              text = "INTELLIGENCE ENGINE",
              variant = KonnectPillVariant.Primary
            )
            Spacer(modifier = Modifier.height(KonnectSpacing.s))
            Text(
              text = "Pose an Inquiry",
              style = Typography.headlineLarge.copy(fontSize = 24.sp),
              color = KonnectPrimaryText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Describe architectural boundaries, stack constraints, or attach logs.",
              style = Typography.bodyMedium,
              color = KonnectSecondaryText
            )
          }

          Spacer(modifier = Modifier.width(KonnectSpacing.m))

          KonnectAICore(
            size = 78.dp,
            state = coreState,
            testTag = "ask_ai_core"
          )
        }
      }
    }

    // Attached Context Banner (REMEMBER → ASK AGAIN loop)
    if (uiState.infoMessage != null) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
              drawRoundRect(
                color = Color(0x0C111111),
                topLeft = Offset(0f, 4.dp.toPx()),
                size = Size(size.width, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(18.dp.toPx(), 18.dp.toPx())
              )
            }
            .shadow(3.dp, RoundedCornerShape(18.dp), ambientColor = Color(0x0E171717), spotColor = Color(0x14171717))
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFE8E9E6))
            .border(1.dp, Color(0x20111111), RoundedCornerShape(18.dp))
            .padding(horizontal = KonnectSpacing.m, vertical = KonnectSpacing.s)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(KonnectPrimaryDarkAction),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Info,
                  contentDescription = null,
                  tint = KonnectHighlight,
                  modifier = Modifier.size(15.dp)
                )
              }
              Spacer(modifier = Modifier.width(KonnectSpacing.m))
              Column {
                Text(
                  text = "ATTACHED MEMORY CONTEXT",
                  style = Typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 0.5.sp),
                  color = KonnectPrimaryDarkAction
                )
                Text(
                  text = uiState.infoMessage ?: "",
                  style = Typography.bodySmall,
                  color = KonnectPrimaryText
                )
              }
            }

            KonnectPill(
              text = "REMEMBER → ASK AGAIN",
              variant = KonnectPillVariant.Primary
            )
          }
        }
      }
    }

    // 2. Physical Input Workspace
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("ask_input_card")
          .drawBehind {
            drawRoundRect(
              color = Color(0x0C111111),
              topLeft = Offset(0f, 6.dp.toPx()),
              size = Size(size.width, size.height),
              cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
            )
          }
          .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(24.dp),
            ambientColor = Color(0x10171717),
            spotColor = Color(0x16171717)
          )
          .clip(RoundedCornerShape(24.dp))
          .background(KonnectPrimarySurface)
          .drawBehind {
            drawRect(
              brush = Brush.verticalGradient(
                colors = listOf(KonnectHighlight.copy(alpha = 0.95f), Color.Transparent),
                startY = 0f,
                endY = 5.dp.toPx()
              )
            )
          }
          .border(1.dp, Color(0x14111111), RoundedCornerShape(24.dp))
          .padding(KonnectSpacing.cardPaddingHero)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          // Title Input
          KonnectTextField(
            value = uiState.title,
            onValueChange = { viewModel.onTitleChanged(it) },
            label = "INQUIRY TITLE",
            placeholder = "e.g., My Jetpack Compose screen becomes slow when displaying a large list from Firebase",
            testTag = "ask_title_input"
          )

          Spacer(modifier = Modifier.height(KonnectSpacing.m))

          // Detailed Description Input
          KonnectTextField(
            value = uiState.description,
            onValueChange = { viewModel.onDescriptionChanged(it) },
            label = "CONTEXT & SYSTEM CONSTRAINTS",
            placeholder = "Detail the lifecycle transitions, observed frame times, memory allocations, or error traces...",
            singleLine = false,
            minHeight = 110.dp,
            testTag = "ask_description_input"
          )

          Spacer(modifier = Modifier.height(KonnectSpacing.m))

          // Category Pills
          Text(
            text = "ARCHITECTURAL DOMAIN",
            style = Typography.labelSmall.copy(fontSize = 11.sp),
            color = KonnectSecondaryText
          )
          Spacer(modifier = Modifier.height(KonnectSpacing.xs))

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.xs),
            modifier = Modifier.fillMaxWidth()
          ) {
            items(uiState.availableCategories) { category ->
              val isSelected = category == uiState.selectedCategory
              KonnectPill(
                text = category,
                isSelected = isSelected,
                onClick = { viewModel.onCategorySelected(category) },
                testTag = "ask_category_$category"
              )
            }
          }

          Spacer(modifier = Modifier.height(KonnectSpacing.l))

          // Elevated Dimensional Image Tile
          Text(
            text = "ATTACHED SCREENSHOT / TELEMETRY",
            style = Typography.labelSmall.copy(fontSize = 11.sp),
            color = KonnectSecondaryText
          )
          Spacer(modifier = Modifier.height(KonnectSpacing.xs))

          if (uiState.imageUri != null) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .shadow(6.dp, RoundedCornerShape(16.dp), ambientColor = Color(0x18111111), spotColor = Color(0x22111111))
                .clip(RoundedCornerShape(16.dp))
                .background(KonnectSecondarySurface)
                .border(1.dp, Color(0x20111111), RoundedCornerShape(16.dp))
            ) {
              AsyncImage(
                model = uiState.imageUri,
                contentDescription = "Attached screenshot",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )

              Row(
                modifier = Modifier
                  .align(Alignment.TopEnd)
                  .padding(KonnectSpacing.s),
                horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.xs)
              ) {
                KonnectSecondaryButton(
                  text = "Replace",
                  fullWidth = false,
                  onClick = {
                    photoPickerLauncher.launch(
                      PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                  },
                  testTag = "replace_screenshot_button"
                )

                IconButton(
                  onClick = { viewModel.removeImage() },
                  modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(KonnectPrimaryDarkAction)
                    .testTag("remove_screenshot_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove screenshot",
                    tint = KonnectHighlight,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          } else {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(KonnectSpacing.s)
            ) {
              KonnectSecondaryButton(
                text = "Take photo",
                leadingIcon = Icons.Default.CameraAlt,
                modifier = Modifier.weight(1f),
                onClick = {
                  cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                },
                testTag = "take_photo_button"
              )

              KonnectSecondaryButton(
                text = "Gallery",
                leadingIcon = Icons.Default.AddPhotoAlternate,
                modifier = Modifier.weight(1f),
                onClick = {
                  photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                  )
                },
                testTag = "add_screenshot_button"
              )
            }
          }

          Spacer(modifier = Modifier.height(KonnectSpacing.l))

          // Voice Input Section
          Text(
            text = "VOICE INPUT CHANNELS",
            style = Typography.labelSmall.copy(fontSize = 11.sp),
            color = KonnectSecondaryText
          )
          Spacer(modifier = Modifier.height(KonnectSpacing.xs))

          KonnectVoiceInput(
            state = uiState.voiceState,
            onMicClick = {
              audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            },
            transcriptText = uiState.voiceTranscript,
            testTag = "ask_voice_input"
          )

          if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(KonnectSpacing.m))
            Text(
              text = uiState.errorMessage ?: "",
              style = Typography.bodySmall,
              color = KonnectPrimaryDarkAction
            )
          }

          Spacer(modifier = Modifier.height(KonnectSpacing.xl))

          // Primary Submit Button
          KonnectPrimaryButton(
            text = if (uiState.isSubmitting) "Dissecting with Gemini..." else "Find help",
            leadingIcon = Icons.Default.AutoAwesome,
            enabled = !uiState.isSubmitting,
            onClick = {
              viewModel.submitQuestion { questionId ->
                onQuestionSubmitted(questionId)
              }
            },
            testTag = "ask_find_help_button"
          )
        }
      }

      Spacer(modifier = Modifier.height(KonnectSpacing.xxxl))
    }
  }
}

private fun saveBitmapToCache(context: Context, bitmap: Bitmap): String {
  return try {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "konnect_capture_${System.currentTimeMillis()}", null)
    path ?: ""
  } catch (e: Exception) {
    ""
  }
}
