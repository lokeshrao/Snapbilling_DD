plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")}

android {
    namespace = "com.snapbizz.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 23

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // ========== 📌 MODULE DEPENDENCIES ==========
    implementation(project(":common"))

    // ========== ⚡ CORE LIBRARIES ==========
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.datastore.preferences)

    // ========== 🔥 FIREBASE ==========
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config.ktx)

    // ========== 🎨 UI & GRAPHICS ==========
    implementation(libs.androidx.ui.graphics.android)

    // ========== 🏗️ ROOM (DATABASE) ==========
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // ========== 🌐 NETWORKING (RETROFIT, OKHTTP, GSON) ==========
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // ========== 🏎️ COROUTINES ==========
    implementation(libs.kotlinx.coroutines.android)

    // ========== 🏗️ DEPENDENCY INJECTION (HILT) ==========
    api(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // ========== 🪵 LOGGING ==========
    implementation(libs.timber)

    // ========== 🛠️ TESTING ==========
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    api(libs.androidx.hilt.navigation.compose)

}
