/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import tech.antibytes.kfixture.PublicApi

class FilterableGeneratorStub<T : Any, R : Any>(
    @JsName("generateStub")
    var generate: (() -> R)? = null,
    @JsName("generateWithPredicateStub")
    var generateWithPredicate: ((Function1<T?, Boolean>) -> R)? = null,
) : PublicApi.FilterableGenerator<T, R> {
    override fun generate(): R {
        return generate?.invoke() ?: throw RuntimeException("Missing SideEffect for generate.")
    }

    override fun generate(predicate: (T?) -> Boolean): R {
        return generateWithPredicate?.invoke(predicate)
            ?: throw RuntimeException("Missing SideEffect for generateWithPredicate.")
    }

    fun clear() {
        generate = null
        generateWithPredicate = null
    }
}
