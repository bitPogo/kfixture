/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.defaultPredicate

internal abstract class Generator<T : Any> : PublicApi.Generator<T> {
    private val defaultPredicate: Function1<T?, Boolean> = ::defaultPredicate

    private fun filterValues(
        predicate: (T?) -> Boolean,
        generate: () -> T,
    ): T {
        var returnValue: T

        do {
            returnValue = generate()
        } while (!predicate(returnValue))

        return returnValue
    }

    protected fun returnFilteredValue(
        predicate: (T?) -> Boolean,
        generate: () -> T,
    ): T {
        return if (predicate == defaultPredicate) {
            generate()
        } else {
            filterValues(predicate, generate)
        }
    }
}
