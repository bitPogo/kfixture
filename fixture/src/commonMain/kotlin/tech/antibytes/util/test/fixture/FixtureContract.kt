/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.test.fixture

internal interface FixtureContract {
    interface Configuration : PublicApi.Configuration {
        fun build(): PublicApi.Fixture
    }

    companion object {
        const val SEPARATOR = ":"
        const val QUALIFIER_PREFIX = "q:"
    }
}
