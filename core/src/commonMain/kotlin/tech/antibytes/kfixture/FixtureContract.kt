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
    const val ACCESS_LOWER_BOUND = 0
    const val ARRAY_LOWER_BOUND = 1
    const val ARRAY_UPPER_BOUND = 11
    const val CHAR_LOWER_BOUND = 32.toChar()
    const val CHAR_UPPER_BOUND = 126.toChar()
    const val COLLECTION_LOWER_BOUND = 1
    const val COLLECTION_UPPER_BOUND = 11
}
