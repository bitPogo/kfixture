/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import tech.antibytes.kfixture.PublicApi

class RangedNumericArrayGeneratorStub<T, R : Any>(
    @JsName("generateStub")
    var generate: ((Int) -> R)? = null,
    @JsName("generateWithRangeStub")
    var generateWithRange: ((T, T, Int) -> R)? = null,
    @JsName("generateWithRangesStub")
    var generateWithRanges: ((Array<out ClosedRange<T>>) -> R)? = null,
    @JsName("generateWithSizedRangesStub")
    var generateWithRangesAndSize: ((Array<out ClosedRange<T>>, Int) -> R)? = null,
) : PublicApi.RangedArrayGenerator<T, R> where T : Any, T : Comparable<T> {
    override fun generate(size: Int): R {
        return generate?.invoke(size) ?: throw RuntimeException("Missing SideEffect for generate.")
    }

    override fun generate(): R {
        TODO("Not yet implemented")
    }

    override fun generate(predicate: (T?) -> Boolean): R {
        TODO("Not yet implemented")
    }

    override fun generate(from: T, to: T, predicate: (T?) -> Boolean): R {
        TODO("Not yet implemented")
    }

    override fun generate(from: T, to: T, size: Int): R {
        return generateWithRange?.invoke(from, to, size)
            ?: throw RuntimeException("Missing SideEffect for generateWithRange.")
    }

    override fun generate(vararg ranges: ClosedRange<T>): R {
        return generateWithRanges?.invoke(ranges)
            ?: throw RuntimeException("Missing SideEffect for generateWithRanges.")
    }

    override fun generate(vararg ranges: ClosedRange<T>, size: Int): R {
        return generateWithRangesAndSize?.invoke(ranges, size)
            ?: throw RuntimeException("Missing SideEffect for generateWithRangesAndSize.")
    }
}
