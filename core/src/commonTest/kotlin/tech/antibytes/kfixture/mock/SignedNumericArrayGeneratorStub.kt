/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import tech.antibytes.kfixture.PublicApi

class SignedNumericArrayGeneratorStub<T, R : Any>(
    @JsName("generateStub")
    var generate: ((Int) -> R)? = null,
    @JsName("generateWithRangeStub")
    var generateWithRange: ((T, T, Int) -> R)? = null,
    @JsName("generateWithSignStub")
    var generateWithSign: ((PublicApi.Sign, Int) -> R)? = null,
) : PublicApi.SignedNumericArrayGenerator<T, R> where T : Number, T : Comparable<T> {
    override fun generate(): R {
        TODO("Not yet implemented")
    }

    override fun generate(size: Int): R {
        return generate?.invoke(size) ?: throw RuntimeException("Missing SideEffect for generate.")
    }

    override fun generate(from: T, to: T): R {
        TODO("Not yet implemented")
    }

    override fun generate(from: T, to: T, size: Int): R {
        return generateWithRange?.invoke(from, to, size)
            ?: throw RuntimeException("Missing SideEffect for generateWithRange.")
    }

    override fun generate(sign: PublicApi.Sign): R {
        TODO("Not yet implemented")
    }

    override fun generate(sign: PublicApi.Sign, size: Int): R {
        return generateWithSign?.invoke(sign, size)
            ?: throw RuntimeException("Missing SideEffect for generateWithType.")
    }

    override fun generate(vararg ranges: ClosedRange<T>): R {
        TODO("Not yet implemented")
    }

    override fun generate(vararg ranges: ClosedRange<T>, size: Int): R {
        TODO("Not yet implemented")
    }
}
