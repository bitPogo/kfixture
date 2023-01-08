/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.kfixture.config.publishing

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.api.PackageConfiguration
import tech.antibytes.gradle.publishing.api.PomConfiguration

class FixtureCoreConfiguration(project: Project) {
    val publishing = Publishing(project)

    class Publishing(project: Project) : FixturePublishingConfiguration(project) {
        val packageConfiguration = PackageConfiguration(
            pom = PomConfiguration(
                name = "KFixture Core",
                description = "A tool to generate randomized test values for Kotlin Multiplatform.",
                year = 2022,
                url = "https://$gitHubRepositoryPath",
            ),
            developers = listOf(developer),
            license = license,
            scm = sourceControl,
        )
    }

    companion object {
        const val group = "tech.antibytes.kfixture"
    }
}
