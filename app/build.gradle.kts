plugins {
 id("com.android.application")
 id("org.jetbrains.kotlin.plugin.compose")
}
android {
 namespace = "com.example.gesturedemo"
 compileSdk { version = release(37) }
 defaultConfig {
  applicationId = "com.example.gesturedemo"
  minSdk = 27
  targetSdk = 37
  versionCode = 1
  versionName = "1.0"
  testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
 }
 compileOptions { sourceCompatibility = JavaVersion.VERSION_11; targetCompatibility = JavaVersion.VERSION_11 }
 buildFeatures { compose = true }
}
dependencies {
 implementation(platform("androidx.compose:compose-bom:2026.02.01"))
 implementation("androidx.activity:activity-compose:1.8.0")
 implementation("androidx.compose.material3:material3")
 implementation("androidx.compose.ui:ui-tooling-preview")
 debugImplementation("androidx.compose.ui:ui-tooling")
 androidTestImplementation(platform("androidx.compose:compose-bom:2026.02.01"))
 androidTestImplementation("androidx.compose.ui:ui-test-junit4")
 androidTestImplementation("androidx.test.ext:junit:1.1.5")
 androidTestImplementation("androidx.test:runner:1.5.2")
 debugImplementation("androidx.compose.ui:ui-test-manifest")
}
