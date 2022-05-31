/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.generator.array.ByteArrayGenerator
import tech.antibytes.kfixture.generator.array.UByteArrayGenerator
import tech.antibytes.kfixture.generator.primitive.AnyGenerator
import tech.antibytes.kfixture.generator.primitive.BooleanGenerator
import tech.antibytes.kfixture.generator.primitive.CharGenerator
import tech.antibytes.kfixture.generator.primitive.DoubleGenerator
import tech.antibytes.kfixture.generator.primitive.FloatGenerator
import tech.antibytes.kfixture.generator.primitive.IntegerGenerator
import tech.antibytes.kfixture.generator.primitive.LongGenerator
import tech.antibytes.kfixture.generator.primitive.ShortGenerator
import tech.antibytes.kfixture.generator.primitive.StringGenerator
import tech.antibytes.kfixture.generator.primitive.UIntegerGenerator
import tech.antibytes.kfixture.generator.primitive.ULongGenerator
import tech.antibytes.kfixture.generator.primitive.UShortGenerator
import tech.antibytes.kfixture.generator.primitive.UnitGenerator
import tech.antibytes.kfixture.qualifier.resolveId
import kotlin.random.Random
import kotlin.reflect.KClass

internal class Configuration(
    override var seed: Int = 0,
) : FixtureContract.Configuration {
    private val customGenerators: MutableMap<String, PublicApi.GeneratorFactory<out Any>> = mutableMapOf()

    private fun initializeDefaultsGenerators(random: IsolateState<Random>): Map<String, PublicApi.Generator<out Any>> {
        return mapOf(
            resolveClassName(Boolean::class) to BooleanGenerator(random),
            resolveClassName(Short::class) to ShortGenerator(random),
            resolveClassName(Int::class) to IntegerGenerator(random),
            resolveClassName(Float::class) to FloatGenerator(random),
            resolveClassName(Char::class) to CharGenerator(random),
            resolveClassName(Long::class) to LongGenerator(random),
            resolveClassName(Double::class) to DoubleGenerator(random),
            resolveClassName(String::class) to StringGenerator(random),
            resolveClassName(ByteArray::class) to ByteArrayGenerator(random),
            resolveClassName(UShort::class) to UShortGenerator(random),
            resolveClassName(UInt::class) to UIntegerGenerator(random),
            resolveClassName(ULong::class) to ULongGenerator(random),
            resolveClassName(UByteArray::class) to UByteArrayGenerator(random),
            resolveClassName(Any::class) to AnyGenerator,
            resolveClassName(Unit::class) to UnitGenerator

        )
    }

    private fun initializeCustomGenerators(random: IsolateState<Random>): MutableMap<String, PublicApi.Generator<out Any>> {
        val initializedGenerators: MutableMap<String, PublicApi.Generator<out Any>> = mutableMapOf()

        customGenerators.forEach { (key, factory) ->
            initializedGenerators[key] = factory.getInstance(random)
        }

        return initializedGenerators
    }

    override fun <T : Any> addGenerator(
        clazz: KClass<T>,
        factory: PublicApi.GeneratorFactory<T>,
        qualifier: PublicApi.Qualifier?
    ): PublicApi.Configuration {
        val id = resolveId(
            clazz,
            qualifier
        )

        return this.also {
            customGenerators[id] = factory
        }
    }

    override fun build(): PublicApi.Fixture {
        val random = IsolateState { Random(seed) }
        val generators = initializeCustomGenerators(random).also {
            it.putAll(initializeDefaultsGenerators(random))
        }

        return Fixture(random, generators)
    }
}
