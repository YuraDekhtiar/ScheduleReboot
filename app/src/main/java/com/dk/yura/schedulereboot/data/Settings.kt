package com.dk.yura.schedulereboot.data

data class TimeUi(
    val hour: Int,
    val minute: Int
) {
    fun toStringTimeFormat(): String {
        return "${getStringFormat(hour)}:${getStringFormat(minute)}"
    }

    private fun getStringFormat(value: Int): String {
        return when(value.toString().length == 1) {
            true -> "0$value"
            false -> value.toString()
        }
    }
}

data class SettingsModel(
    val timeUi: TimeUi,
    val isEnabled: Boolean,
)
