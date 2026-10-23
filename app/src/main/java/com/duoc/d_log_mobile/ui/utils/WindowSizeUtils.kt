package com.duoc.d_log_mobile.ui.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun esPantallaAncha(): Boolean {
    val actividad = LocalActivity.current ?: return false
    return calculateWindowSizeClass(actividad).widthSizeClass != WindowWidthSizeClass.Compact
}
