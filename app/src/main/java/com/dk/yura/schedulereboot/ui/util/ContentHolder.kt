package com.dk.yura.schedulereboot.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.schedulereboot.ui.widgets.ErrorScreen
import com.example.schedulereboot.ui.widgets.Loader
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <T> ContentHolder(
    stateFlow: StateFlow<UiState<T>>,
    Content: @Composable (T) -> Unit
) {
    val state by stateFlow.collectAsState()

    when (val safeState = state) {
        is UiState.Data<T> -> {
            Content(safeState.data)
        }

        is UiState.Error -> {
            ErrorScreen(safeState.reason)
        }

        UiState.Loading -> {
            Loader()
        }
    }
}

sealed class UiState<out T> {
    @Immutable
    object Loading : UiState<Nothing>()

    @Immutable
    data class Error(
        val reason: String,
    ) : UiState<Nothing>()

    @Immutable
    data class Data<T>(val data: T) : UiState<T>()
}