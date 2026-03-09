package com.nawin.habittracker.ui.components

import androidx.compose.runtime.Composable
import com.nawin.habittracker.R
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun CelebrationAnimation() {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.celebration)
    )

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}