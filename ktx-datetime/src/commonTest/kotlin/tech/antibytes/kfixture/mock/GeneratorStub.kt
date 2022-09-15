/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import tech.antibytes.kfixture.PublicApi

class GeneratorStub<T : Any>(
    @JsName("generateStub")
    var generate: (() -> T)? = null,
) : PublicApi.Generator<T> {
    override fun generate(): T {
        return generate?.invoke() ?: throw RuntimeException("Missing SideEffect for generate.")
    }

    fun clear() {
        generate = null
    }
}
