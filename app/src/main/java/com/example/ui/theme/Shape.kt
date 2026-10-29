package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// KONNECT Shape System
// Reusable dimensions for small cards, standard cards, hero cards, bottom sheets, buttons, input fields, pills
object KonnectShapes {
  val SmallCard = RoundedCornerShape(16.dp)
  val StandardCard = RoundedCornerShape(20.dp)
  val HeroCard = RoundedCornerShape(24.dp)
  val BottomSheet = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
  val Button = RoundedCornerShape(16.dp)
  val ButtonPill = RoundedCornerShape(999.dp)
  val InputField = RoundedCornerShape(16.dp)
  val Pill = RoundedCornerShape(999.dp)
  val FloatingObject = RoundedCornerShape(20.dp)
}

val Shapes = Shapes(
  extraSmall = RoundedCornerShape(8.dp),
  small = KonnectShapes.SmallCard,
  medium = KonnectShapes.StandardCard,
  large = KonnectShapes.HeroCard,
  extraLarge = RoundedCornerShape(28.dp)
)
