plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id ("kotlin-kapt")

}

android {
    namespace = "com.example.readquran"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.readquran"
        minSdk = 26
        //noinspection OldTargetApi
        targetSdk = 34
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
    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Room runtime
    //noinspection UseTomlInstead
    implementation ("androidx.room:room-runtime:2.6.1")

// Annotation processor for Room (use KAPT for Kotlin)
    //noinspection KaptUsageInsteadOfKsp
    kapt (libs.androidx.room.compiler)

// Room Kotlin Extensions and Coroutines support
    implementation (libs.androidx.room.ktx)
    // Coroutines Core library
    implementation (libs.kotlinx.coroutines.core)

// Coroutines support for Android
    implementation (libs.kotlinx.coroutines.android)

// Optional: Coroutines support for testing
    testImplementation (libs.kotlinx.coroutines.test)
    implementation (libs.gson)
    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    //exoPlayer
    implementation ("androidx.media3:media3-exoplayer:1.5.0")
    implementation ("androidx.media3:media3-ui:1.5.0")
    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    //view pager
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    //material design
    implementation ("com.google.android.material:material:1.9.0")
    //flow layout
    implementation (libs.androidx.constraintlayout.v204)
    //material design
    implementation ("com.google.android.material:material:1.9.0")
    //picasso
    implementation ("com.squareup.picasso:picasso:2.8")



}