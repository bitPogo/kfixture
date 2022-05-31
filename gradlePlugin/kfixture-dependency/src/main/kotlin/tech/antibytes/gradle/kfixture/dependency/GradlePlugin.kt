/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.kfixture.dependency

object GradlePlugin {
    const val dependency = "tech.antibytes.gradle-plugins:antibytes-dependency:${Version.Gradle.antibytes}"
    const val publishing = "tech.antibytes.gradle-plugins:antibytes-publishing:${Version.Gradle.antibytes}"
    const val versioning = "tech.antibytes.gradle-plugins:antibytes-versioning:${Version.Gradle.antibytes}"
    const val coverage = "tech.antibytes.gradle-plugins:antibytes-coverage:${Version.Gradle.antibytes}"
    const val projectConfig = "tech.antibytes.gradle-plugins:antibytes-configuration:${Version.Gradle.antibytes}"
    const val runtimeConfig = "tech.antibytes.gradle-plugins:antibytes-runtime-configuration:${Version.Gradle.antibytes}"
    const val spotless = "com.diffplug.spotless:spotless-plugin-gradle:${Version.Gradle.spotless}"
}
