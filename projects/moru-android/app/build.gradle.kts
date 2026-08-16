import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

// Firebase is optional in this portfolio snapshot. The plugin is applied only
// when a developer supplies their own, git-ignored configuration file.
if (rootProject.file("app/google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
}

val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

val baseUrl: String = (
    providers.gradleProperty("MORU_BASE_URL").orNull
        ?: providers.environmentVariable("MORU_BASE_URL").orNull
        ?: localProps.getProperty("base.url")
        ?: "https://example.invalid/"
    ).trim().let {
    require(it.startsWith("https://")) {
        "MORU_BASE_URL (or base.url) must use HTTPS"
    }
    if (it.endsWith("/")) it else "$it/"
}

android {
    namespace = "com.konkuk.moru"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.konkuk.moru"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
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
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.datastore.preferences)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // navigation
    implementation(libs.androidx.navigation.compose)

    // serialization
    implementation(libs.kotlinx.serialization.json)

    // coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Network
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization.converter)
    // Gson for local data serialization
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    //page를 좌우로 넘기는 Indicator를 위한 의존성
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    // Material Icons 확장 라이브러리 (Outlined, Rounded, Sharp 아이콘 포함)
    implementation(libs.androidx.compose.material.icons.extended)

    // FlowRow를 위한 확장
    implementation(libs.accompanist.flowlayout)

    // 스와이프 기능
    implementation(libs.androidx.compose.material)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.messaging)

}

configurations.all {
    exclude(group = "com.intellij", module = "annotations")
}
