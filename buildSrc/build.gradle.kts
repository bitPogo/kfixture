/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.kfixture.dependency.Dependency
import tech.antibytes.gradle.kfixture.dependency.addCustomRepositories
import tech.antibytes.gradle.kfixture.dependency.ensureKotlinVersion

plugins {
    `kotlin-dsl`

    id("tech.antibytes.gradle.kfixture.dependency")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
    addCustomRepositories()
}

dependencies {
    implementation(Dependency.gradle.dependency) {
        exclude(
            group = "org.jetbrains.dokka",
            module = "dokka-gradle-plugin"
        )
    }
    implementation(Dependency.gradle.projectConfig)
    implementation(Dependency.gradle.dokka)
}
