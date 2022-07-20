/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import tech.antibytes.kfixture.PublicApi

class SignedNumberGeneratorStub<T>(
    @JsName("generateStub")
    var generate: (() -> T)? = null,
    @JsName("generateWithRangeStub")
    var generateWithRange: ((T, T) -> T)? = null,
    @JsName("generateWithSignStub")
    var generateWithSign: ((PublicApi.Sign) -> T)? = null,
) : PublicApi.SignedNumberGenerator<T> where T : Number, T : Comparable<T> {
    override fun generate(): T {
        return generate?.invoke() ?: throw RuntimeException("Missing SideEffect for generate.")
    }

    override fun generate(from: T, to: T): T {
        return generateWithRange?.invoke(from, to)
            ?: throw RuntimeException("Missing SideEffect for generateWithRange.")
    }

    override fun generate(sign: PublicApi.Sign): T {
        return generateWithSign?.invoke(sign)
            ?: throw RuntimeException("Missing SideEffect for generateWithType.")
    }
}
