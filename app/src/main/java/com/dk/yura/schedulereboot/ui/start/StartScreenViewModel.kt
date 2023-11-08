package com.dk.yura.schedulereboot.ui.start

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dk.yura.schedulereboot.AlarmManagerApp
import com.dk.yura.schedulereboot.RuntimeManagerApp
import com.dk.yura.schedulereboot.data.SettingsModel
import com.dk.yura.schedulereboot.data.SettingsRepository
import com.dk.yura.schedulereboot.data.TimeUi
import com.dk.yura.schedulereboot.ui.util.UiState
import com.dk.yura.schedulereboot.ui.widgets.undomain.stateInWhileSubscribe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class StartScreenViewModel(
    private val settingsRepository: SettingsRepository,
    private val alarmManagerApp: AlarmManagerApp
) : ViewModel() {
    private val _update = MutableStateFlow(false)

    var state: StateFlow<UiState<ModelUi>> = flow {
        emit(ModelUi(settingsRepository.getSettings(), false))
    }
        .combine(_update) { _, _ ->
            UiState.Data(
                ModelUi(settingsRepository.getSettings(), false)
            )
        }
        .flowOn(Dispatchers.Default)
        .stateInWhileSubscribe()

    fun changeStatusTask(status: Boolean) {
        viewModelScope.launch {
            settingsRepository.setStatusTask(status)
            update()
        }
    }

    fun setTaskTime(timeUi: TimeUi) {
        viewModelScope.launch {
            settingsRepository.setTaskTime(timeUi)
            update()
        }
    }

    fun checkRoot() {
        Log.d("root", RuntimeManagerApp.exec(RuntimeManagerApp.Action.CHECK_ROOT).toString())
    }

    fun rebootAction() {
        Log.d("root", RuntimeManagerApp.exec(RuntimeManagerApp.Action.REBOOT).toString())
    }

    fun shutdownAction() {
        Log.d("root", RuntimeManagerApp.exec(RuntimeManagerApp.Action.SHUTDOWN).toString())
    }

    private fun update() {
        _update.value = !_update.value
        alarmManagerApp.setAlarm()
    }
}

data class ModelUi(
    val settings: SettingsModel,
    var isRoot: Boolean
)

