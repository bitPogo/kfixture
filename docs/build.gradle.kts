/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.kfixture.config.publishing.FixturePublishingConfiguration

// see: https://github.com/bitfunk/gradle-plugins/blob/29c798a0e0fc572fa94f49da85407c3769dc11cc/docs/build.gradle.kts
plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.mkDocs)
}

antibytesDocumentation {
    versioning.set(FixturePublishingConfiguration().versioning)
}
