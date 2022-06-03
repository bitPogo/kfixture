/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

@InternalAPI
@PublishedApi
internal object FixtureContract {
    interface Configuration : PublicApi.Configuration {
        fun build(): PublicApi.Fixture
    }

    const val SEPARATOR = ":"
    const val QUALIFIER_PREFIX = "q:"
    const val ARRAY_LOWER_BOUND = 1
    const val ARRAY_UPPER_BOUND = 100
    const val CHAR_LOWER_BOUND = 33
    const val CHAR_UPPER_BOUND = 126
    const val STRING_LOWER_BOUND = 1
    const val STRING_UPPER_BOUND = 10
    const val COLLECTION_LOWER_BOUND = 1
    const val COLLECTION_UPPER_BOUND = 10
}
