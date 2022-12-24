/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
package tech.antibytes.gradle.kfixture.config.repositories

import tech.antibytes.gradle.dependency.helper.AntibytesRepository
import tech.antibytes.gradle.dependency.helper.AntibytesUrl

private val githubGroups = listOf(
    "tech.antibytes.gradle",
)

object Repositories {
    val pluginRepositories = listOf(
        AntibytesRepository(
            AntibytesUrl.SNAPSHOT,
            githubGroups,
        ),
        AntibytesRepository(
            AntibytesUrl.ROLLING,
            githubGroups,
        ),
    )
}
