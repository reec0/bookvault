plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.ipt_102_finalproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ipt_102_finalproject"
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
}

dependencies {
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
}

//    dependencies {
//        implementation("com.android.volley:volley:1.2.1")
//        implementation("androidx.recyclerview:recyclerview:1.2.1")
//        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
//        implementation ("com.github.bumptech.glide:glide:4.15.1")
//        annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
//
//    }
