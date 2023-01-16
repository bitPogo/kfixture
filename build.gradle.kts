/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.helper.addCustomRepositories
import tech.antibytes.gradle.dependency.helper.ensureKotlinVersion
import tech.antibytes.gradle.kfixture.config.publishing.FixturePublishingConfiguration
import tech.antibytes.gradle.kfixture.config.repositories.Repositories.pluginRepositories
import tech.antibytes.gradle.quality.api.CodeAnalysisConfiguration
import tech.antibytes.gradle.quality.api.StableApiConfiguration

plugins {
    id("tech.antibytes.gradle.setup")

    alias(antibytesCatalog.plugins.gradle.antibytes.dependencyHelper)
    alias(antibytesCatalog.plugins.gradle.antibytes.publishing)
    alias(antibytesCatalog.plugins.gradle.antibytes.quality)
}

val publishing = FixturePublishingConfiguration(project)

antibytesPublishing {
    additionalPublishingTasks.set(publishing.additionalPublishingTasks)
    versioning.set(publishing.versioning)
    repositories.set(publishing.repositories)
}

antibytesQuality {
    codeAnalysis.set(CodeAnalysisConfiguration(project = project))
    stableApi.set(StableApiConfiguration())
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
