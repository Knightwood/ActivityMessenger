plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
        id("maven-publish")
}

android {
    namespace = "com.kiylx.libx.activity_ktx"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.github.knightwood"
                artifactId = "activityExt"
                version = "1.0"
                from(components.getByName("release"))
                //artifact(tasks.getByName("bundleReleaseAar"))
            }
        }
    }
}

dependencies {
    implementation(AndroidX_KTX.core)
    implementation(AndroidX.appCompat)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
        //ktx
    implementation(Lifecycle_KTX.livedata)
    implementation(Lifecycle_KTX.viewmodel)

    implementation(AndroidX_KTX.fragment)
    implementation(AndroidX_KTX.activity)
}
