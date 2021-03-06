/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import tech.antibytes.gradle.dependency.Version
import tech.antibytes.gradle.kfixture.config.FixturePublishingConfiguration
import tech.antibytes.gradle.kfixture.dependency.addCustomRepositories
import tech.antibytes.gradle.kfixture.dependency.ensureKotlinVersion

plugins {
    id("tech.antibytes.gradle.kfixture.dependency")

    id("tech.antibytes.gradle.dependency")

    id("tech.antibytes.gradle.kfixture.script.quality-spotless")

    id("org.owasp.dependencycheck")

    id("tech.antibytes.gradle.publishing")

    id("io.gitlab.arturbosch.detekt") version "1.21.0"
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
    gradleVersion = "7.5"
    distributionType = Wrapper.DistributionType.ALL
}

detekt {
    toolVersion = "1.21.0"
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config = files("$projectDir/detekt/config.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/detekt/baseline.xml") // a way of suppressing issues before introducing detekt
    source = files(projectDir)
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = JavaVersion.VERSION_1_8.toString()
    exclude(
        "**/.gradle/**",
        "**/.idea/**",
        "**/build/**",
        "**/buildSrc/**",
        ".github/**",
        "gradle/**",
        "**/gradle/**",
        "**/example/**",
        "**/test/resources/**",
        "**/build.gradle.kts",
        "**/settings.gradle.kts",
        "**/Dangerfile.df.kts"
    )

    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(false)
        sarif.required.set(false)
    }
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = JavaVersion.VERSION_1_8.toString()

    exclude(
        "**/.gradle/**",
        "**/.idea/**",
        "**/build/**",
        "**/gradle/wrapper/**",
        ".github/**",
        "assets/**",
        "docs/**",
        "gradle/**",
        "**/example/**",
        "**/*.adoc",
        "**/*.md",
        "**/gradlew",
        "**/LICENSE",
        "**/.java-version",
        "**/gradlew.bat",
        "**/*.png",
        "**/*.properties",
        "**/*.pro",
        "**/*.sq",
        "**/*.xml",
        "**/*.yml"
    )
}
