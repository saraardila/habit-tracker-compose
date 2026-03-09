package com.nawin.habittracker.data.preferences

import androidx.annotation.RawRes
import com.nawin.habittracker.R

data class PetConfig(
    val index: Int,
    @RawRes val idleRes: Int,        // animación cuando progress == 0
    @RawRes val sadRes: Int,         // progress < 0.3
    @RawRes val normalRes: Int,      // progress < 0.6
    @RawRes val happyRes: Int,       // progress < 1
    @RawRes val celebrateRes: Int,   // progress == 1
    val name: String,
    val emoji: String,
    val requiredStreak: Int          // streak necesario para desbloquear
)

val ALL_PETS = listOf(
    PetConfig(
        index = 0,
        idleRes = R.raw.kawaii_cry,
        sadRes = R.raw.kawaii_cry,
        normalRes = R.raw.kawaii_hi,
        happyRes = R.raw.kawaii_star,
        celebrateRes = R.raw.kawaii_love,
        name = "Kawaii",
        emoji = "🌸",
        requiredStreak = 0  // gratis desde el inicio
    ),
    PetConfig(
        index = 1,
        idleRes = R.raw.angry_dog,
        sadRes = R.raw.angry_dog,
        normalRes = R.raw.happy_dog,
        happyRes = R.raw.happyunicorn_dog,
        celebrateRes = R.raw.astro_dog,
        name = "Doggo",
        emoji = "🐶",
        requiredStreak = 7  // desbloquea con 7 días de racha
    )
)

fun PetConfig.getAnimationRes(progress: Float): Int = when {
    progress == 0f  -> idleRes
    progress < 0.3f -> sadRes
    progress < 0.6f -> normalRes
    progress < 1f   -> happyRes
    else            -> celebrateRes
}