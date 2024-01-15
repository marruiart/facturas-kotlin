plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.facturas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.facturas"
        minSdk = 28
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    val core = "1.12.0"
    val appcompat = "1.6.1"
    val material = "1.11.0"
    val constraintlayout = "2.1.4"
    val junit = "4.13.2"
    val gson = "2.10"
    val fragment = "1.6.2"
    val navVersion = "2.7.6"

    implementation("androidx.core:core-ktx:${core}")
    implementation("androidx.appcompat:appcompat:${appcompat}")
    implementation("com.google.android.material:material:${material}")
    implementation("androidx.constraintlayout:constraintlayout:${constraintlayout}")
    testImplementation("junit:junit:${junit}")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Gson
    implementation("com.google.code.gson:gson:${gson}")
    // Fragments
    implementation("androidx.fragment:fragment-ktx:${fragment}")
    // NAVIGATION
    implementation("androidx.navigation:navigation-fragment-ktx:${navVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:${navVersion}")

}