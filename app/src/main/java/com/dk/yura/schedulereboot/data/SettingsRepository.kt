package com.dk.yura.schedulereboot.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val STORE_NAME = "settings"

class SettingsRepository(
    private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)

    private object Keys {
        val isTaskEnabled = booleanPreferencesKey("task_enabled")
        val taskHour = intPreferencesKey("task_hour")
        val taskMinute = intPreferencesKey("task_minute")
    }

    fun getStatusTask(): Flow<Boolean> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[Keys.isTaskEnabled] ?: false }

    suspend fun setStatusTask(status: Boolean) {
        context.dataStore.edit { it[Keys.isTaskEnabled] = status }
    }

    suspend fun setTaskTime(time: TimeUi) {
        context.dataStore.edit { it[Keys.taskMinute] = time.minute }
        context.dataStore.edit { it[Keys.taskHour] = time.hour }
    }

    suspend fun getSettings(): SettingsModel {
        val isEnabled = context.dataStore.data.map {
            it[Keys.isTaskEnabled] ?: false
        }.first()

        val minutes = context.dataStore.data.map {
            it[Keys.taskMinute] ?: 0
        }.first()

        val hour = context.dataStore.data.map {
            it[Keys.taskHour] ?: 0
        }.first()

        return SettingsModel(TimeUi(hour, minutes), isEnabled)
    }
}
