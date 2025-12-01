package kr.co.mobpa.sample.waveAppSuiteSdk

import android.app.Application
import kr.co.mobpa.waveAppSuiteSdk.AppBox
import kr.co.mobpa.waveAppSuiteSdk.data.AppBoxWebConfig

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

        // --------------------------------------------------------------
        // AppBox 초기화
        // --------------------------------------------------------------
        AppBox.getInstance().initSDK(
            context = this,
            baseUrl = "https://www.appboxapp.com",
            projectId = "AAA-000000",
//            webConfig = appBoxWebConfig,
            debugMode = true,
            pushIcon = R.drawable.ic_launcher_background
        )
        // --------------------------------------------------------------
    }
}