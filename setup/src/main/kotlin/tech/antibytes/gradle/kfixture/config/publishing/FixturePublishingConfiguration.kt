/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.kfixture.config.publishing

import org.gradle.api.Project
import tech.antibytes.gradle.configuration.BasePublishingConfiguration

open class FixturePublishingConfiguration(
    project: Project,
) : BasePublishingConfiguration(project, "kfixture")
