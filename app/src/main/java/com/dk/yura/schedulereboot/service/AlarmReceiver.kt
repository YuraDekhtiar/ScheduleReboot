package com.dk.yura.schedulereboot.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dk.yura.schedulereboot.AlarmManagerApp
import com.dk.yura.schedulereboot.AlarmManagerApp.Companion.REBOOT_ACTION_INTENT
import com.dk.yura.schedulereboot.RuntimeManagerApp
import com.dk.yura.schedulereboot.data.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("onReceive", "onReceive")

        if (intent?.action == REBOOT_ACTION_INTENT) {
            try {
                RuntimeManagerApp.exec(RuntimeManagerApp.Action.REBOOT)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            Log.d("AlarmBroadcast", REBOOT_ACTION_INTENT)
        }

        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            if (context != null) {
                runBlocking {
                    // Waiting startup system services and time synchronization
                    delay(PauseTimeMillis)
                    AlarmManagerApp(context, SettingsRepository(context)).setAlarm()
                }
            }
            Log.d("AlarmBroadcast", "android.intent.action.BOOT_COMPLETED")
        }
    }
}

const val PauseTimeMillis: Long = 30000

