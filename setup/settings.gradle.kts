/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
pluginManagement {
    repositories {
        val antibytesPlugins = "^tech\\.antibytes\\.[\\.a-z\\-]+"
        gradlePluginPortal()
        google()
        mavenCentral()
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
    id("tech.antibytes.gradle.dependency.settings") version "601dc8e"
}

rootProject.name = "fixture-setup"
