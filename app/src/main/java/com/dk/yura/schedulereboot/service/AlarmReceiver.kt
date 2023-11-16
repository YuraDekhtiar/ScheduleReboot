package com.dk.yura.schedulereboot.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dk.yura.schedulereboot.AlarmManagerApp
import com.dk.yura.schedulereboot.AlarmManagerApp.Companion.REBOOT_ACTION_INTENT
import com.dk.yura.schedulereboot.RuntimeManagerApp
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


class AlarmReceiver : BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context?, intent: Intent?) {

        val alarmManagerApp: AlarmManagerApp = get()

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
                alarmManagerApp.setAlarm()
            }
            Log.d("AlarmBroadcast", "android.intent.action.BOOT_COMPLETED")
        }
    }
}


