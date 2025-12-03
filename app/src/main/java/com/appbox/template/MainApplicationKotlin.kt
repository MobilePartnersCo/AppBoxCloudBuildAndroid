package com.appbox.template

import android.app.Application
import kr.co.mobpa.waveAppSuiteSdk.AppBox

class MainApplicationKotlin : Application() {
    override fun onCreate() {
        super.onCreate()

        // AppBox 초기화
        AppBox.getInstance().initSDK(
            context = this,
            baseUrl = BuildConfig.BUILD_URL,
            projectId = BuildConfig.PROJECT_ID,
            debugMode = false,
            pushIcon = R.mipmap.ic_launcher
        )
    }
}