/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.Version
import tech.antibytes.gradle.util.test.config.FixturePublishingConfiguration
import tech.antibytes.gradle.util.test.dependency.addCustomRepositories
import tech.antibytes.gradle.util.test.dependency.ensureKotlinVersion

plugins {
    id("tech.antibytes.gradle.util.test.dependency")

    id("tech.antibytes.gradle.dependency")

    id("tech.antibytes.gradle.util.test.script.quality-spotless")

    id("org.owasp.dependencycheck")

    id("tech.antibytes.gradle.publishing")
}

antiBytesPublishing {
    versioning = FixturePublishingConfiguration.versioning
    repositoryConfiguration = FixturePublishingConfiguration.repositories
}

allprojects {
    repositories {
        addCustomRepositories()
        mavenCentral()
        google()
        jcenter()
    }

    ensureKotlinVersion(Version.kotlin.language)
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "7.4.2"
    distributionType = Wrapper.DistributionType.ALL
}
