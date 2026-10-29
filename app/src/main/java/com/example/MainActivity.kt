package com.example

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.di.KonnectContainer
import com.example.ui.navigation.KonnectApp
import com.example.ui.theme.KonnectTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val container = KonnectContainer.initialize(this)

    // Handle Android Share Target (ACTION_SEND)
    val sharedText: String? = if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
      intent.getStringExtra(Intent.EXTRA_TEXT)
    } else null

    val sharedImageUri: String? = if (intent?.action == Intent.ACTION_SEND && intent.type?.startsWith("image/") == true) {
      @Suppress("DEPRECATION")
      val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
      } else {
        intent.getParcelableExtra(Intent.EXTRA_STREAM)
      }
      uri?.toString() ?: intent.clipData?.getItemAt(0)?.uri?.toString()
    } else null

    setContent {
      KonnectTheme {
        KonnectApp(
          container = container,
          initialSharedText = sharedText,
          initialSharedImageUri = sharedImageUri
        )
      }
    }
  }
}
