/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import tech.antibytes.kfixture.PublicApi

class FilterableArrayGeneratorStub<T : Any, R : Any>(
    @JsName("generateStub")
    var generate: ((Int) -> R)? = null,
    @JsName("generateWithPredicateStub")
    var generateWithPredicate: ((Int, (T?) -> Boolean) -> R)? = null,
) : PublicApi.FilterableArrayGenerator<T, R> {
    override fun generate(): R {
        TODO("Not yet implemented")
    }

    override fun generate(predicate: (T?) -> Boolean): R {
        TODO("Not yet implemented")
    }

    override fun generate(size: Int): R {
        return generate?.invoke(size) ?: throw RuntimeException("Missing SideEffect for generate.")
    }

    override fun generate(size: Int, predicate: (T?) -> Boolean): R {
        return generateWithPredicate?.invoke(size, predicate)
            ?: throw throw RuntimeException("Missing SideEffect for generateWithPredicate")
    }
}
