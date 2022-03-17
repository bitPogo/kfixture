/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.test.fixture

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.util.test.fixture.generator.array.ByteArrayGenerator
import tech.antibytes.util.test.fixture.generator.array.UByteArrayGenerator
import tech.antibytes.util.test.fixture.generator.primitive.AnyGenerator
import tech.antibytes.util.test.fixture.generator.primitive.BooleanGenerator
import tech.antibytes.util.test.fixture.generator.primitive.CharGenerator
import tech.antibytes.util.test.fixture.generator.primitive.DoubleGenerator
import tech.antibytes.util.test.fixture.generator.primitive.FloatGenerator
import tech.antibytes.util.test.fixture.generator.primitive.IntegerGenerator
import tech.antibytes.util.test.fixture.generator.primitive.LongGenerator
import tech.antibytes.util.test.fixture.generator.primitive.ShortGenerator
import tech.antibytes.util.test.fixture.generator.primitive.StringGenerator
import tech.antibytes.util.test.fixture.generator.primitive.UIntegerGenerator
import tech.antibytes.util.test.fixture.generator.primitive.ULongGenerator
import tech.antibytes.util.test.fixture.generator.primitive.UShortGenerator
import tech.antibytes.util.test.fixture.generator.primitive.UnitGenerator
import tech.antibytes.util.test.fixture.qualifier.resolveId
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
