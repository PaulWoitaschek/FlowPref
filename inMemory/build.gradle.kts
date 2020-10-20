import de.paulwoitaschek.flowpref.configurePublishing

plugins {
  kotlin("jvm")
}

configurePublishing()

dependencies {
  api(project(":core"))
  api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  api("org.jetbrains.kotlinx:kotlinx-coroutines-core")
}
