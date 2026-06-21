package com.example.composedemo

import android.app.Application
import com.example.composedemo.compose.stabilityAnalyzer.DataProvider
import com.skydoves.compose.stability.runtime.ComposeStabilityAnalyzer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

//import com.example.composedemo.BuildConfig

class ComposeDemo : Application() {

    init {
//        ComposeStabilityAnalyzer.setEnabled(BuildConfig.DEBUG)
        ComposeStabilityAnalyzer.setEnabled(true)

        startKoin {
            androidLogger()
            androidContext(this@ComposeDemo)
            modules(appModule)
        }
    }
}

val appModule = module {
    viewModelOf(::DataProvider)
}
