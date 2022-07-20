/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import tech.antibytes.kfixture.PublicApi

class RangedGeneratorStub<T>(
    @JsName("generateStub")
    var generate: (() -> T)? = null,
    @JsName("generateWithRangeStub")
    var generateWithRange: ((T, T) -> T)? = null,
) : PublicApi.RangedGenerator<T> where T : Any, T : Comparable<T> {
    override fun generate(): T {
        return generate?.invoke() ?: throw RuntimeException("Missing SideEffect for generate.")
    }

    override fun generate(from: T, to: T): T {
        return generateWithRange?.invoke(from, to)
            ?: throw RuntimeException("Missing SideEffect for generateWithRange.")
    }
}
