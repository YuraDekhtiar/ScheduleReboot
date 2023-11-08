package com.dk.yura.schedulereboot.ui.widgets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorScreen(textError: String) {
    Text(text = textError)
}