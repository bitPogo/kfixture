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
    var generateWithRange: ((T, T, Int, Function1<T?, Boolean>) -> R)? = null,
    @JsName("generateWithRangesStub")
    var generateWithRanges: ((Array<out ClosedRange<T>>) -> R)? = null,
    @JsName("generateWithSizedRangesStub")
    var generateWithRangesAndSize: ((Array<out ClosedRange<T>>, Int?, Function1<T?, Boolean>) -> R)? = null,
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

    override fun generate(from: T, to: T, size: Int, predicate: (T?) -> Boolean): R {
        return generateWithRange?.invoke(from, to, size, predicate)
            ?: throw RuntimeException("Missing SideEffect for generateWithRange.")
    }

    override fun generate(vararg ranges: ClosedRange<T>, size: Int?, predicate: (T?) -> Boolean): R {
        return generateWithRangesAndSize?.invoke(ranges, size, predicate)
            ?: throw RuntimeException("Missing SideEffect for generateWithRangesAndSize.")
    }
}
