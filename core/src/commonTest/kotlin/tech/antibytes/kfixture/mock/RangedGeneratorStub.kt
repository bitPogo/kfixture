/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import tech.antibytes.kfixture.PublicApi

class RangedGeneratorStub<T, R: Any>(
    @JsName("generateStub")
    var generate: (() -> R)? = null,
    @JsName("generateWithRangeStub")
    var generateWithRange: ((T, T) -> R)? = null,
) : PublicApi.RangedGenerator<T, R> where T : Any, T : Comparable<T> {
    override fun generate(): R {
        return generate?.invoke() ?: throw RuntimeException("Missing SideEffect for generate.")
    }

    override fun generate(from: T, to: T): R {
        return generateWithRange?.invoke(from, to)
            ?: throw RuntimeException("Missing SideEffect for generateWithRange.")
    }
}
