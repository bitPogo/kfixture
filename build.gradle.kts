/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.helper.addCustomRepositories
import tech.antibytes.gradle.dependency.helper.customArtifact
import tech.antibytes.gradle.kfixture.config.publishing.FixturePublishingConfiguration
import tech.antibytes.gradle.dependency.helper.ensureKotlinVersion
import tech.antibytes.gradle.kfixture.config.repositories.Repositories.pluginRepositories
import tech.antibytes.gradle.quality.api.CodeAnalysisConfiguration
import tech.antibytes.gradle.quality.api.StableApiConfiguration

plugins {
    id("tech.antibytes.gradle.setup")

    id("tech.antibytes.gradle.kfixture.dependency")

    id("tech.antibytes.gradle.dependency")

    alias(antibytesCatalog.plugins.gradle.antibytes.dependencyHelper)
    alias(antibytesCatalog.plugins.gradle.antibytes.publishing)
    alias(antibytesCatalog.plugins.gradle.antibytes.quality)
}

antiBytesPublishing {
    versioning.set(FixturePublishingConfiguration.versioning)
    repositories.set(FixturePublishingConfiguration.repositories)
}

antiBytesQuality {
    codeAnalysis.set(
        CodeAnalysisConfiguration(project = project).copy(
            configurationFiles = files(
                project.customArtifact("tech.antibytes.gradle:antibytes-detekt-configuration:c189d8a"),
                "$rootDir/detekt/kfixture.config.yml"
            )
        )
    )
    stableApi.set(StableApiConfiguration(excludeProjects = setOf("docs")))
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
    }

    ensureKotlinVersion()
}

repositories {
    addCustomRepositories(pluginRepositories)
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "7.5.1"
    distributionType = Wrapper.DistributionType.ALL
}
