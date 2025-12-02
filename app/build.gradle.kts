import groovy.json.JsonSlurper

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

val configFile = file("config.json") // 앱 모듈 폴더(app/) 내에 생성됨을 가정

// 기본값 설정 (중요: 값이 비어있으면 빌드 에러가 발생하므로 안전한 기본값 설정)
var myAppId = "" // 기본 패키지명
var myAppName = "" // 기본 앱 이름
var myProjectId = "" // 기본 프로젝트 ID
var myLoadingUrl = "" // 기본 로딩 URL
var myAppVersionName = "1.0.1" // 기본 앱버전 명
var myAppVersionCode = 1 // 기본 앱버전 코드

if (configFile.exists()) {
    try {
        println("⚙️ 로컬 설정 파일 발견: ${configFile.absolutePath}")
        val json = JsonSlurper().parseText(configFile.readText()) as Map<String, Any>

        myAppId = (json["packageName"] as? String) ?: myAppId
        myAppName = (json["appName"] as? String) ?: myAppName
        myProjectId = (json["projectId"] as? String) ?: myProjectId
        myLoadingUrl = (json["loadingUrl"] as? String) ?: myLoadingUrl
        myAppVersionName = (json["appVersionName"] as? String) ?: myAppVersionName
        myAppVersionCode = (json["appVersionCode"] as? Int) ?: myAppVersionCode

        println(
            """
            ✅ [Config] 설정 로드 완료
            - PackageName:     $myAppId
            - AppName:         $myAppName
            - ProjectID:       $myProjectId
            - LoadingUrl:      $myLoadingUrl
            - AppVersionName:  $myAppVersionName
            - AppVersionCode:  $myAppVersionCode
        """.trimIndent()
        )
    } catch (e: Exception) {
        println("⚠️ 설정 파일 파싱 실패: ${e.message}")
    }
} else {
    println("⚠️ 설정 파일이 없습니다. 기본값으로 빌드합니다.")
}

android {
    namespace = "com.appbox.template"
    compileSdk = 36

    defaultConfig {
        applicationId = myAppId
        minSdk = 26
        targetSdk = 36
        versionCode = myAppVersionCode
        versionName = myAppVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 앱 이름 (AndroidManifest에서 @string/dynamic_app_name 사용)
        resValue("string", "dynamic_app_name", myAppName)

        // Kotlin 코드에서 사용할 상수 (BuildConfig 클래스에 생성됨)
        buildConfigField("String", "PROJECT_ID", "\"$myProjectId\"")
        buildConfigField("String", "BUILD_URL", "\"$myLoadingUrl\"")

        // AAB(App Bundle) 파일명 변경을 위한 archivesBaseName 설정
        val safeAppName = myAppName.replace(" ", "_")
        project.setProperty("archivesBaseName", "${safeAppName}_${myAppVersionName}_${myAppVersionCode}")
    }

    signingConfigs {
        create("release") {
            storeFile = file("__KEYSTORE_PATH__")
            storePassword = "__KEYSTORE_STORE_PASSWORD__"
            keyAlias = "__KEY_ALIAS__"
            keyPassword = "__KEY_PASSWORD__"
        }
    }

    // APK 파일명 강제 변경
    applicationVariants.configureEach {
        val variant = this
        variant.outputs.forEach { output ->
            val outputImpl = output as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            val safeAppName = myAppName.replace(" ", "_")
            // 원하는 형식: {앱이름}_{버전명}_{버전코드}.apk
            val newFileName = "${safeAppName}_${myAppVersionName}_${myAppVersionCode}.apk"
            outputImpl.outputFileName = newFileName
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // --------------------------------------------------------------
    // implementation 선언
    // --------------------------------------------------------------
    implementation("com.github.MobilePartnersCo:AppBoxSDKPackage:all-v1.0.36")
    // --------------------------------------------------------------
}