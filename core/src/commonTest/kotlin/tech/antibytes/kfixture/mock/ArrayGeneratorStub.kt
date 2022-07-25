/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import tech.antibytes.kfixture.PublicApi

class ArrayGeneratorStub<T : Any>(
    @JsName("generateStub")
    var generate: ((Int) -> T)? = null,
) : PublicApi.ArrayGenerator<T> {
    override fun generate(): T {
        TODO("Not yet implemented")
    }

    override fun generate(size: Int): T {
        return generate?.invoke(size) ?: throw RuntimeException("Missing sideeffect for generate.")
    }

    override fun generate(predicate: (T) -> Boolean): T {
        TODO("Not yet implemented")
    }
}
