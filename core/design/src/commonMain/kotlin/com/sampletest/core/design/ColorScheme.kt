package com.sampletest.core.design

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
expect fun platformColorScheme(
    darkTheme: Boolean,
    disableDynamicTheming: Boolean
): ColorScheme