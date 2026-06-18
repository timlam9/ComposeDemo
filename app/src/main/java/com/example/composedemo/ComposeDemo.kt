package com.example.composedemo

import android.app.Application
import com.skydoves.compose.stability.runtime.ComposeStabilityAnalyzer
//import com.example.composedemo.BuildConfig

class ComposeDemo : Application() {

    init {
//        ComposeStabilityAnalyzer.setEnabled(BuildConfig.DEBUG)
        ComposeStabilityAnalyzer.setEnabled(true)
    }
}