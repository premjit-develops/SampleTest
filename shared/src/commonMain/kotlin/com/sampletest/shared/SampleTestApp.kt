package com.sampletest.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SampleTestApp() {

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme
    ) {

        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                Text("Just Test")
            }
        }

    }

}

