/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import kotlin.random.Random
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import tech.antibytes.kfixture.PublicApi

class DependentGeneratorStub<T : Any>(
    @JsName("dependentGenerateStub")
    var generate: (() -> T)? = null,
) : PublicApi.Generator<T> {
    override fun generate(): T {
        return generate?.invoke() ?: throw RuntimeException("Missing sideeffect for generate.")
    }

    override fun generate(predicate: (T) -> Boolean): T {
        TODO("Not yet implemented")
    }
}

class DependentGeneratorFactoryStub<T : Any>(
    @JsName("dependentGenerateStub")
    var generate: (() -> T)? = null,
) : PublicApi.DependentGeneratorFactory<T> {
    private val _lastRandom: AtomicRef<Random> = atomic(Random(49))
    private val _lastInstance: AtomicRef<DependentGeneratorStub<T>?> = atomic(null)
    private val _lastGenerators: AtomicRef<Map<String, PublicApi.Generator<out Any>>?> = atomic(null)

    var lastRandom by _lastRandom
    var lastInstance by _lastInstance
    var lastGenerators by _lastGenerators

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
