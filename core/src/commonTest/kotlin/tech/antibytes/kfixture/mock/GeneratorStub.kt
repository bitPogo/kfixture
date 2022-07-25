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
        return generate?.invoke() ?: throw RuntimeException("Missing sideeffect for generate.")
    }

    override fun generate(predicate: (T) -> Boolean): T {
        TODO("Not yet implemented")
    }
}
