/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import tech.antibytes.kfixture.PublicApi

internal abstract class FilterableGenerator<R : Any> : PublicApi.FilterableGenerator<R, R> {
    override fun generate(predicate: (R?) -> Boolean): R {
        var value: R

        do {
            value = generate()
        } while (!predicate(value))

        return value
    }
}
