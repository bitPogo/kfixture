/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator

import tech.antibytes.kfixture.PublicApi

internal abstract class Generator<T : Any> : PublicApi.Generator<T> {
    protected fun returnFilteredValue(
        predicate: (T) -> Boolean,
        generate: () -> T,
    ): T {
        var returnValue: T

        do {
            returnValue = generate()
        } while (!predicate(returnValue))

        return returnValue
    }
}
