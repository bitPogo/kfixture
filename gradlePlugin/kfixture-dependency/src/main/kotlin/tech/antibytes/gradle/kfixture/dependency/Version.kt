/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.kfixture.dependency

object Version {

    val gradle = Gradle
    const val kotlin = "1.7.10"

    object Gradle {
        /**
         * [Kotlin](https://github.com/JetBrains/kotlin)
         */
        const val kotlin = Version.kotlin

        /**
         * [AnitBytes GradlePlugins](https://github.com/bitPogo/gradle-plugins)
         */
        const val antibytes = "d40a207"

        /**
         * [Spotless](https://plugins.gradle.org/plugin/com.diffplug.gradle.spotless)
         */
        const val spotless = "6.11.0"
    }

    val npm = NPM

    object NPM {
        /**
         * [Joda](https://github.com/JodaOrg/joda-time/releases)
         */
        const val joda = "~2.11.1"
    }
}
