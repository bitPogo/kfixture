/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.kfixture.config

import tech.antibytes.gradle.publishing.api.PackageConfiguration
import tech.antibytes.gradle.publishing.api.PomConfiguration

object FixtureConfiguration {
    const val group = "tech.antibytes.kfixture"

    val publishing = Publishing

    object Publishing : FixturePublishingConfiguration() {
        val packageConfiguration = PackageConfiguration(
            pom = PomConfiguration(
                name = "fixture",
                description = "Convenience tools for generated dummy test values for Kotlin Multiplatform.",
                year = 2022,
                url = "https://$gitHubRepositoryPath"
            ),
            developers = listOf(developer),
            license = license,
            scm = sourceControl
        )
    }
}
