plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}


android {
    namespace = "com.snapbizz.snapbillingv2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.snapbizz.snapbillingv2"
        minSdk = 23
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
    buildFeatures {
        compose = true
    }
    flavorDimensions += "version"
    productFlavors{
        create("axis"){
            dimension = "version"
            applicationIdSuffix = ".axis"
            versionNameSuffix = "-axis"
            versionCode = 1
            versionName = "1.0"
        }
        create("axisworldline"){
            dimension = "version"
            applicationIdSuffix = ".axisworldline"
            versionNameSuffix = "-axisworldline"
            versionCode = 1
            versionName = "1.0"
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":onboarding"))
    implementation(project(":ui"))
    implementation(project(":common"))
    implementation(project(":inventory"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.foundation.layout.android)
    debugImplementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.navigation.compose)


    implementation(libs.ui)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")


}