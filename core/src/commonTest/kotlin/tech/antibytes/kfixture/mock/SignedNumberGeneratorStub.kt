/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import tech.antibytes.kfixture.PublicApi

class SignedNumberGeneratorStub<T, R : Any>(
    @JsName("generateStub")
    var generate: (() -> R)? = null,
    @JsName("generateWithPredicateStub")
    var generateWithPredicate: (((T?) -> Boolean) -> R)? = null,
    @JsName("generateWithRangeStub")
    var generateWithRange: ((T, T, (T?) -> Boolean) -> R)? = null,
    @JsName("generateWithSignStub")
    var generateWithSign: ((PublicApi.Sign, predicate: (T?) -> Boolean) -> R)? = null,
) : PublicApi.SignedNumberGenerator<T, R> where T : Number, T : Comparable<T> {
    override fun generate(): R {
        return generate?.invoke() ?: throw RuntimeException("Missing SideEffect for generate.")
    }

    override fun generate(predicate: (T?) -> Boolean): R {
        return generateWithPredicate?.invoke(predicate)
            ?: throw RuntimeException("Missing SideEffect for generateWithPredicate.")
    }

    override fun generate(sign: PublicApi.Sign, predicate: (T?) -> Boolean): R {
        return generateWithSign?.invoke(sign, predicate)
            ?: throw RuntimeException("Missing SideEffect for generateWithType.")
    }

    override fun generate(from: T, to: T, predicate: (T?) -> Boolean): R {
        return generateWithRange?.invoke(from, to, predicate)
            ?: throw RuntimeException("Missing SideEffect for generateWithRange.")
    }
}
