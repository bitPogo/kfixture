/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.gradle.kfixture.dependency.Dependency as LocalDependency
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration
import tech.antibytes.gradle.coverage.api.AndroidJacocoConfiguration
import tech.antibytes.gradle.coverage.api.JacocoVerificationRule
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoCounter
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoMeasurement
import tech.antibytes.gradle.publishing.api.DocumentationConfiguration
import tech.antibytes.gradle.configuration.ensureIosDeviceCompatibility
import tech.antibytes.gradle.configuration.isIdea
import tech.antibytes.gradle.kfixture.config.publishing.FixtureKtxDateTimeConfiguration

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.publishing)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)
    id(antibytesCatalog.plugins.kotlinx.atomicfu.get().pluginId)
    alias(antibytesCatalog.plugins.gradle.antibytes.dokkaConfiguration)
}

group = FixtureKtxDateTimeConfiguration.group

antiBytesPublishing {
    packaging.set(FixtureKtxDateTimeConfiguration.publishing.packageConfiguration)
    repositories.set(FixtureKtxDateTimeConfiguration.publishing.repositories)
    versioning.set(FixtureKtxDateTimeConfiguration.publishing.versioning)
    documentation.set(
        DocumentationConfiguration(
            tasks = setOf("dokkaHtml"),
            outputDir = buildDir.resolve("dokka")
        )
    )
    signing.set(FixtureKtxDateTimeConfiguration.publishing.signing)
}

antiBytesCoverage {
    val branchCoverage = JacocoVerificationRule(
        counter = JacocoCounter.BRANCH,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.90)
    )

    val instructionCoverage = JacocoVerificationRule(
        counter = JacocoCounter.INSTRUCTION,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.97)
    )

    val excludes = setOf(
        "**/DateTimeFixtureKt*",
        "**/InstantFixtureKt*",
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
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}

kotlin {
    explicitApi()

    android()

    js(IR) {
        nodejs()
        browser()
    }

    jvm()

    ios()
    iosSimulatorArm64()
    ensureIosDeviceCompatibility()

    linuxX64()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.ExperimentalUnsignedTypes")
                optIn("kotlin.RequiresOptIn")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.common)
                implementation(Dependency.multiplatform.dateTime)
                implementation(project(":core"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.test.common)
                implementation(Dependency.multiplatform.test.annotations)
                implementation(Dependency.multiplatform.atomicFu.common)
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
                implementation(Dependency.multiplatform.kotlin.android)
            }
        }
        if (!isIdea()) {
            val androidAndroidTestRelease by getting {
                dependsOn(noJsTest)
            }
            val androidAndroidTest by getting {
                dependsOn(noJsTest)
                dependsOn(androidAndroidTestRelease)
            }
            val androidTestFixturesDebug by getting {
                dependsOn(noJsTest)
            }
            val androidTestFixturesRelease by getting {
                dependsOn(noJsTest)
            }

            val androidTestFixtures by getting {
                dependsOn(noJsTest)
                dependsOn(androidTestFixturesDebug)
                dependsOn(androidTestFixturesRelease)
            }

            val androidTest by getting {
                dependsOn(androidTestFixtures)
            }
        }
        val androidTest by getting {
            dependsOn(noJsTest)

            dependencies {
                implementation(Dependency.multiplatform.test.jvm)
                implementation(Dependency.multiplatform.test.junit)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.js)
                implementation(Dependency.js.nodejs)
                implementation(peerNpm(LocalDependency.npm.joda.name, LocalDependency.npm.joda.version))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.test.js)
                implementation(npm(LocalDependency.npm.joda.name, LocalDependency.npm.joda.version))
            }
        }

        val jvmMain by getting {
            dependsOn(noJsMain)
            dependencies {
                implementation(Dependency.multiplatform.kotlin.jdk8)
            }
        }
        val jvmTest by getting {
            dependsOn(noJsTest)
            dependencies {
                implementation(Dependency.multiplatform.test.jvm)
                implementation(Dependency.multiplatform.test.junit)
            }
        }

        val nativeMain by creating {
            dependsOn(noJsMain)
        }

        val nativeTest by creating {
            dependsOn(noJsTest)
        }

        val darwinMain by creating {
            dependsOn(nativeMain)
        }

        val darwinTest by creating {
            dependsOn(nativeTest)
        }

        val otherMain by creating {
            dependsOn(nativeMain)
        }

        val otherTest by creating {
            dependsOn(nativeTest)
        }

        val linuxX64Main by getting {
            dependsOn(otherMain)
        }

        val linuxX64Test by getting {
            dependsOn(otherTest)
        }

        val iosMain by getting {
            dependsOn(darwinMain)
        }

        val iosTest by getting {
            dependsOn(darwinTest)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }
    }
}

android {
    namespace = "tech.antibytes.kfixture.ktx.datetime"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
