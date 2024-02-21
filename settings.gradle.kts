/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
import tech.antibytes.gradle.dependency.node.nodeToDependencyCatalog
import tech.antibytes.gradle.dependency.settings.fullCache

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        val antibytesPlugins = "^tech\\.antibytes\\.[\\.a-z\\-]+"
        gradlePluginPortal()
        mavenCentral()
        google()
        maven {
            setUrl("https://raw.github.com/bitPogo/maven-snapshots/main/snapshots")
            content {
                includeGroupByRegex(antibytesPlugins)
            }
        }
        maven {
            setUrl("https://raw.github.com/bitPogo/maven-rolling-releases/main/rolling")
            content {
                includeGroupByRegex(antibytesPlugins)
            }
        }
    }
}

plugins {
    id("tech.antibytes.gradle.dependency.settings") version "a9d047f"
}

includeBuild("setup")

dependencyResolutionManagement {
    versionCatalogs {
        create("local") {
            from(files("./gradle/libs.versions.toml"))
            nodeToDependencyCatalog(files("./gradle/package.json"))
        }
    }
}

include(
    ":core",
    ":ktx-datetime",
    ":docs",
)

buildCache {
    fullCache(rootDir)
}

rootProject.name = "kfixture"
