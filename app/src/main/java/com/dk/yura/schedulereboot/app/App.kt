package com.dk.yura.schedulereboot.app

import android.app.Application
import com.dk.yura.schedulereboot.AlarmManagerApp
import com.dk.yura.schedulereboot.ui.start.SettingsRepository
import com.dk.yura.schedulereboot.ui.start.StartScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val appModule = module {
            single { SettingsRepository(androidContext()) }
            single { AlarmManagerApp(androidContext(), get()) }

            viewModel { StartScreenViewModel(get(), get()) }
        }

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(appModule)
        }
    }
}
