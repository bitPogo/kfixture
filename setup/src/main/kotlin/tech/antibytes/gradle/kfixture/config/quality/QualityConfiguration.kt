/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.kfixture.config.quality

import tech.antibytes.gradle.quality.api.StableApiConfiguration

object QualityConfiguration {
    val stableApi = StableApiConfiguration(
        excludeProjects = setOf(
            "docs"
        ),
    )
}
