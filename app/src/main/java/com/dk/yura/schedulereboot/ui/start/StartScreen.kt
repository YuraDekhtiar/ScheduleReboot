package com.dk.yura.schedulereboot.ui.start

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.dk.yura.schedulereboot.R
import com.dk.yura.schedulereboot.ui.components.TimePickerDialog
import com.dk.yura.schedulereboot.ui.util.ContentHolder
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartScreen(modifier: Modifier, startScreenViewModel: StartScreenViewModel = koinViewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        ContentHolder(
            stateFlow = startScreenViewModel.state
        ) { data ->
            StartScreenContent(modifier, startScreenViewModel, data)
        }
    }
}

@Composable
fun StartScreenContent(modifier: Modifier, viewModel: StartScreenViewModel, data: ModelUi) {
    Column(modifier = modifier.padding(5.dp)) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Керування")
                Spacer(modifier = modifier.padding(top = 15.dp))
                ButtonComponent(text = "Check Root", onClick = { viewModel.checkRoot() })
                ButtonComponent(text = "Reboot now", onClick = { })
                ButtonComponent(text = "Shutdown now", onClick = { })
            }
        }
        TimeComponent(modifier, viewModel, data)
    }
}

@Composable
fun TimeComponent(modifier: Modifier, viewModel: StartScreenViewModel, data: ModelUi) {
    val context = LocalContext.current
    var isVisibleTimePicker by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Автоматичне перезавантаження")
        SwitchComponent(viewModel, data.settings.isEnabled, context)
    }
    Spacer(modifier = modifier.padding(top = 10.dp))
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Час перезавантаження", modifier = modifier)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = data.settings.timeUi.toStringTimeFormat(), modifier = modifier)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { isVisibleTimePicker = !isVisibleTimePicker }) {
            Text(text = stringResource(R.string.change_button))
        }
    }
    if (isVisibleTimePicker) {
        TimePickerDialog(onSetTime = {
            viewModel.setTaskTime(it)
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
        })
        isVisibleTimePicker = false
    }
}

@Composable
fun SwitchComponent(viewModel: StartScreenViewModel, status: Boolean, context: Context) {
    Switch(
        modifier = Modifier.semantics { contentDescription = "Switch component" },
        checked = status,
        onCheckedChange = {
            viewModel.changeStatusTask(it)
            Toast.makeText(
                context,
                if (it) R.string.enabled else R.string.disabled,
                Toast.LENGTH_SHORT
            ).show()
        }
    )
}

@Composable
fun ButtonComponent(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = text)
    }
}