import de.paulwoitaschek.flowpref.configurePublishing

plugins {
  id("com.android.library")
  kotlin("android")
}



android {
  compileSdkVersion(30)
  defaultConfig {
    minSdkVersion(21)
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    testInstrumentationRunnerArguments["clearPackageData"] = "true"
  }

  testOptions {
    execution = "ANDROIDX_TEST_ORCHESTRATOR"
  }

  packagingOptions {
    pickFirst("META-INF/AL2.0")
    pickFirst("META-INF/LGPL2.1")
  }
}

configurePublishing()

dependencies {
  api(project(":core"))
  api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  api("org.jetbrains.kotlinx:kotlinx-coroutines-core")

  implementation("androidx.core:core-ktx:1.10.1")

  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test:core:1.6.1")
  androidTestImplementation("androidx.test:rules:1.3.0")
  androidTestImplementation("androidx.test:runner:1.3.0")
  androidTestImplementation("com.google.truth:truth:1.1.4")
  androidTestImplementation("junit:junit:4.13.2")
  androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")
  androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")

  androidTestUtil("androidx.test:orchestrator:1.4.2")
}

