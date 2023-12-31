plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("bcu-plugin")
    id("dokit-compat")
}

bcu {
    config { variant ->
        loggerLevel = 2
        modifiers = arrayOf(
            com.ysj.lib.dokit.plugin.compat.DoKitTransformCompat::class.java,
        )
    }
    filterNot { variant, entryName ->
        !entryName.startsWith("com/ysj/")
            && !entryName.startsWith("com/didichuxing/doraemonkit")
            && !entryName.startsWith("okhttp3")
            && !entryName.startsWith("com/bumptech/glide")
    }
}

// =========== 下面是 dokit 相关配置 ===============================
// 详见：https://xingyun.xiaojukeji.com/docs/dokit/#/androidGuide
ext["DOKIT_PLUGIN_SWITCH"] = true
ext["DOKIT_LOG_SWITCH"] = true
ext["DOKIT_METHOD_SWITCH"] = true
ext["DOKIT_METHOD_STRATEGY"] = com.didichuxing.doraemonkit.plugin.extension.SlowMethodExt.STRATEGY_NORMAL
ext["DOKIT_METHOD_STACK_LEVEL"] = 5
ext["DOKIT_WEBVIEW_CLASS_NAME"] = ""

dokitExt {
    //通用设置
    comm {
        gpsSwitch = true
        networkSwitch = true
        bigImgSwitch = true
        webViewSwitch = true
    }
    slowMethod {
        // 调用栈模式配置 对应 DOKIT_METHOD_STRATEGY=0
        stackMethod.thresholdTime = 10
        stackMethod.enterMethods = mutableSetOf()
        stackMethod.methodBlacklist = mutableSetOf()
        // 普通模式配置 对应 DOKIT_METHOD_STRATEGY=1
        normalMethod.thresholdTime = 500
        normalMethod.packageNames = mutableSetOf("com.ysj.demo")
        normalMethod.methodBlacklist = mutableSetOf()
    }
}
// =============================================================

android {
    namespace = "com.ysj.demo.dokit"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ysj.demo.dokit"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    debugImplementation(dokit)
    releaseImplementation(dokit_no_op)
}