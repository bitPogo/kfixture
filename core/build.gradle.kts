/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration
import tech.antibytes.gradle.coverage.api.AndroidJacocoConfiguration
import tech.antibytes.gradle.coverage.api.JacocoVerificationRule
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoCounter
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoMeasurement
import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import tech.antibytes.gradle.configuration.sourcesets.linux
import tech.antibytes.gradle.configuration.sourcesets.native
import tech.antibytes.gradle.configuration.sourcesets.nativeCoroutine
import tech.antibytes.gradle.kfixture.config.publishing.FixtureCoreConfiguration

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.publishing)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)
    alias(antibytesCatalog.plugins.gradle.antibytes.dokkaConfiguration)
}

group = FixtureCoreConfiguration.group
val publishingConfiguration = FixtureCoreConfiguration(project)

antibytesPublishing {
    packaging.set(publishingConfiguration.publishing.packageConfiguration)
    repositories.set(publishingConfiguration.publishing.repositories)
    versioning.set(publishingConfiguration.publishing.versioning)
    documentation.set(publishingConfiguration.publishing.documentation)
    signing.set(publishingConfiguration.publishing.signing)
}

antibytesCoverage {
    val branchCoverage = JacocoVerificationRule(
        counter = JacocoCounter.BRANCH,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.97)
    )

    val instructionCoverage = JacocoVerificationRule(
        counter = JacocoCounter.INSTRUCTION,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.97)
    )

    val excludes = setOf(
        "**/FixtureKt*",
        "**/EnumFixtureKt*",
        "**/ListFixtureNoJsKt*",
        "**/ListFixtureKt*",
        "**/SetFixtureKt*",
        "**/TupleFixtureKt*",
        "**/MapFixtureKt**",
        "**/CollectionFixtureKt*",
        "**/SequenceFixtureKt**",
        "**/ArrayFixtureKt**",
        "**/FilterableFixtureKt*",
        "**/RangedFixtureKt*",
        "**/RangedNumericArrayFixtureKt*",
        "**/SignedNumericFixtureKt*",
        "**/SignedNumericArrayFixtureKt*",
        "**/BindingKt*",
        "**/RangedSpecialArrayFixtureKt*",
    ) // Inline Function cannot be covered

    val jvmCoverage = JvmJacocoConfiguration.createJvmKmpConfiguration(
        project,
        classFilter = excludes,
        verificationRules = setOf(
            branchCoverage,
            instructionCoverage
        ),
    )

    val androidCoverage = AndroidJacocoConfiguration.createAndroidLibraryKmpConfiguration(
        project,
        classFilter = excludes, // Inline Function cannot be covered
        verificationRules = setOf(
            branchCoverage,
            instructionCoverage
        ),
    )

    configurations.put("jvm", jvmCoverage)
    configurations.put("android", androidCoverage)
}

android {
    namespace = "tech.antibytes.kfixture"

    defaultConfig {
        minSdk = local.versions.minSdk.get().toInt()
    }
}

kotlin {
    androidTarget()

    js(IR) {
        nodejs()
        browser()
    }

    jvm()

    nativeCoroutine()
    linux()

    ensureAppleDeviceCompatibility()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.ExperimentalUnsignedTypes")
                optIn("kotlin.RequiresOptIn")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(antibytesCatalog.common.kotlin.stdlib)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(antibytesCatalog.common.test.kotlin.core)
                implementation(antibytesCatalog.common.test.kotlin.annotations)
            }
        }

        val noJsMain by creating {
            dependsOn(commonMain)
        }
        val noJsTest by creating {
            dependsOn(commonTest)
        }

        val androidMain by getting {
            dependsOn(noJsMain)
            dependencies {
                implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk8)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(antibytesCatalog.jvm.test.kotlin.core)
                implementation(antibytesCatalog.jvm.test.kotlin.junit4)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(antibytesCatalog.js.kotlin.stdlib)
                implementation(antibytesCatalog.js.kotlinx.nodeJs)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(antibytesCatalog.js.test.kotlin.core)
            }
        }

        val jvmMain by getting {
            dependsOn(noJsMain)
        }
        val jvmTest by getting {
            dependsOn(noJsTest)
            dependencies {
                implementation(antibytesCatalog.jvm.test.kotlin.core)
                implementation(antibytesCatalog.jvm.test.kotlin.junit4)
            }
        }

        val nativeMain by getting {
            dependsOn(noJsMain)
        }
        val nativeTest by getting {
            dependsOn(noJsTest)
        }
    }
}
