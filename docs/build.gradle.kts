/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
import ru.vyarus.gradle.plugin.python.PythonExtension
import tech.antibytes.gradle.kfixture.config.publishing.FixturePublishingConfiguration

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.mkDocs)
}

python {
    scope = PythonExtension.Scope.USER
}

antibytesDocumentation {
    versioning.set(FixturePublishingConfiguration(project).versioning)
}
