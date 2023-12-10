plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 34
    namespace = "com.kiylx.activitymessage"

    defaultConfig {
        applicationId = "com.kiylx.activitymessage"
        minSdk = 24
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"))
        }
    }
    buildFeatures {
        viewBinding = true
        //dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(AndroidX_KTX.core)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.material)
    implementation(AndroidX.constraintLayout)
    implementation(Lifecycle_KTX.viewmodel)
    implementation(AndroidX_KTX.fragment)
    implementation(AndroidX_KTX.activity)
    implementation(project(mapOf("path" to ":activitymessenger")))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

}
