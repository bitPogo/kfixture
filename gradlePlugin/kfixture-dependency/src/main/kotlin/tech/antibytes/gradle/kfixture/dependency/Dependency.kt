/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.kfixture.dependency

data class NpmPackage(
    val name: String,
    val version: String,
)

object Dependency {
    val gradle = GradlePlugin

    val npm = NPM

    object NPM {
        val joda = NpmPackage("@js-joda/timezone", Version.npm.joda)
    }
}
