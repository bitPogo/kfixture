/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

class DependentGeneratorStub<T : Any>(
    @JsName("dependentGenerateStub")
    var generate: (() -> T)? = null,
) : PublicApi.Generator<T> {
    override fun generate(): T = generate?.invoke() ?: throw RuntimeException("Missing sideeffect for generate.")
}

class DependentGeneratorFactoryStub<T : Any>(
    @JsName("dependentGenerateStub")
    var generate: (() -> T)? = null,
) : PublicApi.DependentGeneratorFactory<T> {
    var lastRandom: Random = Random(49)
    var lastInstance: DependentGeneratorStub<T>? = null
    var lastGenerators: Map<String, PublicApi.Generator<out Any>>? = null

    override fun getInstance(
        random: Random,
        generators: Map<String, PublicApi.Generator<out Any>>,
    ): PublicApi.Generator<T> {
        return DependentGeneratorStub(generate).also {
            lastRandom = random
            lastInstance = it
            lastGenerators = generators
        }
    }
}
