package de.paulwoitaschek.flowpref

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import java.util.*

fun Project.configurePublishing() {
  project.pluginManager.apply("maven-publish")
  project.pluginManager.withPlugin("maven-publish") {
    project.afterEvaluate {
      project.extensions.configure<PublishingExtension> {
        publications {
          create<MavenPublication>("maven") {
            groupId = "com.github.PaulWoitaschek"
            version = "1.0.1-rc1"
            this.artifactId = project.name.toArtifactName()
            val componentName = if (project.pluginManager.hasPlugin("com.android.library")) {
              "release"
            } else {
              "java"
            }
            from(components[componentName])
            pom {
              licenses {
                license {
                  name.set("The Apache License, Version 2.0")
                  url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
              }
            }
          }
        }
      }
    }
  }
}

private fun String.toArtifactName(): String {
  return "(?<=[a-zA-Z])[A-Z]".toRegex().replace(this) {
    "-${it.value}"
  }.toLowerCase(Locale.US)
}