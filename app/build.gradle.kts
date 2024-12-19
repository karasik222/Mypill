plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")


}

android {
    namespace = "com.example.mypill"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mypill"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.compose.ui:ui:1.7.6")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.6")
    implementation ("androidx.compose.runtime:runtime-livedata:<c1.5.1>")
    implementation ("androidx.compose.runtime:runtime:<1.5.1>")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.work:work-runtime-ktx:2.10.0")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.7.6")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.4.3")
    implementation ("androidx.compose.foundation:foundation:1.4.0")
    implementation ("androidx.compose.material:material-icons-extended:1.7.6")
    implementation ("com.google.code.gson:gson:2.8.8")
    implementation ("androidx.room:room-runtime:2.5.2")
    implementation ("androidx.work:work-runtime-ktx:2.10.0")
    implementation ("androidx.core:core-ktx:1.15.0")
    implementation ("androidx.compose.material3:material3:1.3.1")
    implementation ("androidx.activity:activity-ktx:1.9.3")
    implementation ("androidx.core:core-ktx:1.15.0")
    implementation ("androidx.activity:activity-ktx:1.9.3-beta01")
}