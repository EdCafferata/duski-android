plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "info.cafferata.duski"
    compileSdk = 36

    defaultConfig {
        applicationId = "info.cafferata.duski"
        minSdk = 26
        targetSdk = 36
        versionCode = 2
        versionName = "1.0.1"
    }

    signingConfigs {
        create("release") {
            storeFile = file(project.findProperty("DUSKI_STORE_FILE") as String)
            storePassword = project.findProperty("DUSKI_STORE_PASSWORD") as String
            keyAlias = project.findProperty("DUSKI_KEY_ALIAS") as String
            keyPassword = project.findProperty("DUSKI_KEY_PASSWORD") as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2026.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation("androidx.core:core-ktx:1.17.0")

    // Play Billing — subscription equivalent of StoreKit 2 on iOS.
    implementation("com.android.billingclient:billing-ktx:9.1.0")

    // Override the ancient androidx.fragment (1.0.0/1.1.0) that Play Billing pulls in
    // transitively via com.google.android.gms:play-services-base. Gradle resolves to the
    // highest version, so the bundled APK ships fragment 1.9.0 instead of 1.1.0, which
    // pre-empts the Play Console "SDK version is outdated" warning. This app is pure Compose
    // and doesn't use fragments itself, so the bump is behaviour-neutral.
    implementation("androidx.fragment:fragment:1.9.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
