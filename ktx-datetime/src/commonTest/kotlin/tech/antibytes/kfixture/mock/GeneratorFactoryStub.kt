/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

class GeneratorFactoryStub<T : Any>(
    @JsName("generateStub")
    var generate: (() -> T)? = null,
) : PublicApi.GeneratorFactory<T> {
    var lastRandom: Random = Random(49)
    var lastInstance: FilterableGeneratorStub<T, T>? = null

    override fun getInstance(random: Random): PublicApi.Generator<T> {
        return FilterableGeneratorStub<T, T>(generate).also {
            lastRandom = random
            lastInstance = it
        }
    }
}
