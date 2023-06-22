plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
        id("maven-publish")
}

android {
    namespace = "com.kiylx.libx"
    compileSdk = 32

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.github.knightwood"
                artifactId = "activitymessenger"
                version = "1.0"
                from(components.getByName("release"))
                //artifact(tasks.getByName("bundleReleaseAar"))
            }
        }
    }
}

dependencies {
    implementation(AndroidX.Core.core)
    implementation(AndroidX.appCompat)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

        //导航
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.1")

        //ktx
    implementation(AndroidX.Lifecycle.viewmodel)
    implementation(AndroidX.Lifecycle.fragment)
    implementation(AndroidX.Lifecycle.activity)
        //lifecycle
//    implementation(AndroidX.Lifecycle.runtime)
    implementation(AndroidX.Lifecycle.livedata)
}
