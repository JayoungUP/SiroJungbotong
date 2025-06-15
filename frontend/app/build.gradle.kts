import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.tukorea.sirojungbotong"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.tukorea.sirojungbotong"
        minSdk = 21
        targetSdk = 35      // 타겟 SDK
        versionCode = 1
        versionName = "1.0"
    }

    viewBinding {
        enable=true
    }

    defaultConfig {
        applicationId = "com.tukorea.sirojungbotong"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    viewBinding{
        enable = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation(libs.junit)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    androidTestImplementation(libs.androidx.junit)
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    androidTestImplementation(libs.androidx.espresso.core)
}