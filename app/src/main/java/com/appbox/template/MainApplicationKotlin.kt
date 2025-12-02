package com.appbox.template

import android.app.Application
import kr.co.mobpa.waveAppSuiteSdk.AppBox

class MainApplicationKotlin : Application() {
    override fun onCreate() {
        super.onCreate()

        // --------------------------------------------------------------
        // AppBox AppBoxWebConfig 설정
        // --------------------------------------------------------------
//        val appBoxWebConfig = AppBoxWebConfig().apply {
//            javaScriptEnabled = true
//        }
        // --------------------------------------------------------------

        val projectId = BuildConfig.PROJECT_ID
        val baseUrl = BuildConfig.BUILD_URL

        // --------------------------------------------------------------
        // AppBox 초기화
        // --------------------------------------------------------------
        AppBox.getInstance().initSDK(
            context = this,
            baseUrl = baseUrl,
            projectId = projectId,
//            webConfig = appBoxWebConfig,
            debugMode = true,
            pushIcon = R.mipmap.ic_launcher
        )
        // --------------------------------------------------------------
    }
}