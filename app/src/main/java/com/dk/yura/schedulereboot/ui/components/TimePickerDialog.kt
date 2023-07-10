package com.dk.yura.schedulereboot.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.dk.yura.schedulereboot.data.TimeUi
import java.util.Calendar

@Composable
fun TimePickerDialog(onSetTime: (time: TimeUi) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    android.app.TimePickerDialog(
        LocalContext.current,
        { _, mHour: Int, mMinute: Int ->
            onSetTime(TimeUi(hour = mHour, minute = mMinute))
        },
        hour, minute, true,
    ).show()
}