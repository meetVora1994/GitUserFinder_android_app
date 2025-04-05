plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    //noinspection GradleDependency - because there is a Kotlin version dependency
    id("com.google.devtools.ksp") version "2.0.21-1.0.28" // Match Kotlin version from: https://github.com/google/ksp/releases
}

android {
    namespace = "com.meetvora.gituserfinder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.meetvora.gituserfinder"
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
    buildFeatures {
        compose = true
        buildConfig = true
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

    // Coil for image loading
    // https://coil-kt.github.io/coil/compose/
    implementation(libs.coil.compose)
    // To load network images
    implementation(libs.coil.network.okhttp)

    // Making API calls using Retrofit
    // https://github.com/square/retrofit/tree/trunk
    implementation(libs.retrofit)
    // Log Network calls
    implementation(libs.logging.interceptor)

    // A Converter which uses Moshi for serialization to and from JSON.
    // https://github.com/square/retrofit/blob/trunk/retrofit-converters/moshi/README.md
    implementation(libs.converter.moshi)

    // Parse JSON into Java and Kotlin classes
    // https://github.com/square/moshi
    implementation(libs.moshi.kotlin)
    // Moshi KSP codegen
    ksp(libs.moshi.kotlin.codegen)

    // The Navigation component provides support for Jetpack Compose applications. You can navigate between composables while taking advantage of the Navigation component's infrastructure and features.
    // https://developer.android.com/develop/ui/compose/navigation#kts
    implementation(libs.androidx.navigation.compose)

}