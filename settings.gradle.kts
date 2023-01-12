/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
import tech.antibytes.gradle.dependency.node.nodeToDependencyCatalog
import tech.antibytes.gradle.dependency.settings.gitHubHomeDir
import tech.antibytes.gradle.dependency.settings.isGitHub

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
    id("tech.antibytes.gradle.dependency.settings") version "b1373c3"
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
    ":docs"
)

buildCache {
    local {
        isEnabled = isGitHub()
        directory = File(
            "${gitHubHomeDir()}.gradle${File.separator}caches",
            "build-cache"
        )
        removeUnusedEntriesAfterDays = 3
    }
}

rootProject.name = "kfixture"
