package com.dk.yura.schedulereboot

import java.lang.Exception

object RuntimeManagerApp {
    fun exec(action: Action): Boolean {
        return try {
            when (action) {
                Action.REBOOT -> {
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot now")).waitFor()
                    true
                }

                Action.SHUTDOWN -> {
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot now")).waitFor()
                    true
                }

                Action.CHECK_ROOT -> {
                    Runtime.getRuntime().exec(arrayOf("su"))
                    true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    enum class Action {
        REBOOT, SHUTDOWN, CHECK_ROOT
    }
}
