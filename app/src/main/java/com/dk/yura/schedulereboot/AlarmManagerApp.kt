package com.dk.yura.schedulereboot

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dk.yura.schedulereboot.data.SettingsModel
import com.dk.yura.schedulereboot.data.SettingsRepository
import com.dk.yura.schedulereboot.data.TimeUi
import com.dk.yura.schedulereboot.service.AlarmReceiver
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class AlarmManagerApp(
    private val context: Context,
    private val settingsRepository: SettingsRepository
) {
    fun setAlarm(delay: Long = 30_000) {
        runBlocking {
            // Waiting startup system services and time synchronization
            delay(delay)
            setAlarm()
        }
    }
    private fun setAlarm() {
        val settingsModel: SettingsModel = runBlocking {
            settingsRepository.getSettings()
        }
        val alarmMgr: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent =
            Intent(context, AlarmReceiver::class.java).setAction(REBOOT_ACTION_INTENT)
                .let { intent ->
                    PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                }
        when (settingsModel.isEnabled) {
            true -> {
                alarmMgr.cancel(alarmIntent)
                alarmMgr.set(
                    AlarmManager.RTC_WAKEUP,
                    getCalendarInstance(settingsModel.timeUi).timeInMillis,
                    alarmIntent
                )
                Log.d("AlarmManagerApp", "setAlarm() -> successfully")
            }

            false -> {
                alarmMgr.cancel(alarmIntent)
            }
        }
    }

    private fun getCalendarInstance(timeUi: TimeUi): Calendar {
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, timeUi.hour)
            set(Calendar.MINUTE, timeUi.minute)
            set(Calendar.SECOND, 0)
        }

        // When the time is up, move alarm to the next day
        if (System.currentTimeMillis() > calendar.timeInMillis) {
            calendar.add(Calendar.DATE, 1)
        }
        return calendar
    }

    companion object {
        const val REBOOT_ACTION_INTENT = "com.schedule.reboot.intent.action.REBOOT"
    }
}
