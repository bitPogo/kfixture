/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("OPT_IN_USAGE", "OPT_IN_OVERRIDE")

package tech.antibytes.kfixture

import kotlin.random.Random
import kotlin.reflect.KClass
import tech.antibytes.kfixture.PublicApi.DependentGeneratorFactory
import tech.antibytes.kfixture.PublicApi.Generator
import tech.antibytes.kfixture.PublicApi.GeneratorFactory
import tech.antibytes.kfixture.PublicApi.RangedGenerator
import tech.antibytes.kfixture.PublicApi.SignedNumberGenerator
import tech.antibytes.kfixture.generator.array.BooleanArrayGenerator
import tech.antibytes.kfixture.generator.array.ByteArrayGenerator
import tech.antibytes.kfixture.generator.array.CharArrayGenerator
import tech.antibytes.kfixture.generator.array.DoubleArrayGenerator
import tech.antibytes.kfixture.generator.array.FloatArrayGenerator
import tech.antibytes.kfixture.generator.array.IntArrayGenerator
import tech.antibytes.kfixture.generator.array.LongArrayGenerator
import tech.antibytes.kfixture.generator.array.ShortArrayGenerator
import tech.antibytes.kfixture.generator.array.StringGenerator
import tech.antibytes.kfixture.generator.array.UByteArrayGenerator
import tech.antibytes.kfixture.generator.array.UIntArrayGenerator
import tech.antibytes.kfixture.generator.array.ULongArrayGenerator
import tech.antibytes.kfixture.generator.array.UShortArrayGenerator
import tech.antibytes.kfixture.generator.primitive.AnyGenerator
import tech.antibytes.kfixture.generator.primitive.BooleanGenerator
import tech.antibytes.kfixture.generator.primitive.ByteGenerator
import tech.antibytes.kfixture.generator.primitive.CharGenerator
import tech.antibytes.kfixture.generator.primitive.DoubleGenerator
import tech.antibytes.kfixture.generator.primitive.FloatGenerator
import tech.antibytes.kfixture.generator.primitive.IntegerGenerator
import tech.antibytes.kfixture.generator.primitive.LongGenerator
import tech.antibytes.kfixture.generator.primitive.ShortGenerator
import tech.antibytes.kfixture.generator.primitive.UByteGenerator
import tech.antibytes.kfixture.generator.primitive.UIntegerGenerator
import tech.antibytes.kfixture.generator.primitive.ULongGenerator
import tech.antibytes.kfixture.generator.primitive.UShortGenerator
import tech.antibytes.kfixture.generator.primitive.UnitGenerator
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

internal class Configuration @InternalAPI constructor(
    override var seed: Int = 0,
) : FixtureContract.Configuration {
    private val customGenerators: MutableMap<String, GeneratorFactory<out Any>> = mutableMapOf()
    private val customDependentGenerators: MutableMap<String, DependentGeneratorFactory<out Any>> = mutableMapOf()

    private fun initializeDefaultsGenerators(random: Random): Map<String, Generator<out Any>> {
        return mutableMapOf(
            resolveClassName(Boolean::class) to BooleanGenerator(random),
            resolveClassName(Byte::class) to ByteGenerator(random),
            resolveClassName(Short::class) to ShortGenerator(random),
            resolveClassName(Int::class) to IntegerGenerator(random),
            resolveClassName(Float::class) to FloatGenerator(random),
            resolveClassName(Char::class) to CharGenerator(random),
            resolveClassName(Long::class) to LongGenerator(random),
            resolveClassName(Double::class) to DoubleGenerator(random),
            resolveClassName(UShort::class) to UShortGenerator(random),
            resolveClassName(UInt::class) to UIntegerGenerator(random),
            resolveClassName(ULong::class) to ULongGenerator(random),
            resolveClassName(UByte::class) to UByteGenerator(random),
            resolveClassName(Any::class) to AnyGenerator,
            resolveClassName(Unit::class) to UnitGenerator,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any, R : Generator<T>> Map<String, Generator<out Any>>.resolveGenerator(
        key: String,
    ): R = this[key] as R

    @Suppress("UNCHECKED_CAST")
    private fun <T, R> Map<String, Generator<out Any>>.resolveRangedGenerator(
        key: String,
    ): R where T : Any, T : Comparable<T>, R : RangedGenerator<T, T> = this[key] as R

    @Suppress("UNCHECKED_CAST")
    private fun <T, R> Map<String, Generator<out Any>>.resolveSignedGenerator(
        key: String,
    ): R where T : Any, T : Comparable<T>, R : SignedNumberGenerator<T, T> = this[key] as R

    @Suppress("UNCHECKED_CAST")
    private fun Map<String, Generator<out Any>>.initializeDefaultDependentGenerators(
        random: Random,
    ): Map<String, Generator<out Any>> {
        return this.toMutableMap().apply {
            this[resolveClassName(BooleanArray::class)] = BooleanArrayGenerator(
                random,
                this.resolveGenerator(resolveClassName(Boolean::class)),
            )
            this[resolveClassName(ByteArray::class)] = ByteArrayGenerator(
                random,
                this.resolveSignedGenerator(resolveClassName(Byte::class)),
            )
            this[resolveClassName(UByteArray::class)] = UByteArrayGenerator(
                random,
                this.resolveRangedGenerator(resolveClassName(UByte::class)),
            )
            this[resolveClassName(ShortArray::class)] = ShortArrayGenerator(
                random,
                this.resolveSignedGenerator(resolveClassName(Short::class)),
            )
            this[resolveClassName(UShortArray::class)] = UShortArrayGenerator(
                random,
                this.resolveRangedGenerator(resolveClassName(UShort::class)),
            )
            this[resolveClassName(IntArray::class)] = IntArrayGenerator(
                random,
                this.resolveSignedGenerator(resolveClassName(Int::class)),
            )
            this[resolveClassName(UIntArray::class)] = UIntArrayGenerator(
                random,
                this.resolveRangedGenerator(resolveClassName(UInt::class)),
            )
            this[resolveClassName(LongArray::class)] = LongArrayGenerator(
                random,
                this.resolveSignedGenerator(resolveClassName(Long::class)),
            )
            this[resolveClassName(ULongArray::class)] = ULongArrayGenerator(
                random,
                this.resolveRangedGenerator(resolveClassName(ULong::class)),
            )
            this[resolveClassName(FloatArray::class)] = FloatArrayGenerator(
                random,
                this.resolveRangedGenerator(resolveClassName(Float::class)),
            )
            this[resolveClassName(DoubleArray::class)] = DoubleArrayGenerator(
                random,
                this.resolveRangedGenerator(resolveClassName(Double::class)),
            )
            this[resolveClassName(CharArray::class)] = CharArrayGenerator(
                random,
                this.resolveRangedGenerator(resolveClassName(Char::class)),
            )
            this[resolveClassName(String::class)] = StringGenerator(
                random,
                this.resolveRangedGenerator(resolveClassName(Char::class)),
            )
        }
    }

    private fun Map<String, Generator<out Any>>.initializeCustomGenerators(
        random: Random,
    ): MutableMap<String, Generator<out Any>> {
        val initializedGenerators: MutableMap<String, Generator<out Any>> = this.toMutableMap()

        customGenerators.forEach { (key, factory) ->
            initializedGenerators[key] = factory.getInstance(random)
        }

        customDependentGenerators.forEach { (key, factory) ->
            initializedGenerators[key] = factory.getInstance(random, initializedGenerators.toMap())
        }

        return initializedGenerators
    }

    override fun <T : Any> addGenerator(
        clazz: KClass<out T>,
        factory: GeneratorFactory<out T>,
        qualifier: PublicApi.Qualifier?,
    ): PublicApi.Configuration {
        val id = resolveGeneratorId(
            clazz,
            qualifier,
        )
        customGenerators[id] = factory

        return this
    }

    override fun <T : Any> addGenerator(
        clazz: KClass<out T>,
        factory: DependentGeneratorFactory<out T>,
        qualifier: PublicApi.Qualifier?,
    ): PublicApi.Configuration {
        val id = resolveGeneratorId(
            clazz,
            qualifier,
        )
        customDependentGenerators[id] = factory

        return this
    }

    override fun build(): PublicApi.Fixture {
        val random = Random(seed)
        val internalGenerators = initializeDefaultsGenerators(random)
            .initializeDefaultDependentGenerators(random)
        val generators = internalGenerators
            .initializeCustomGenerators(random)
            .apply { putAll(internalGenerators) }

        return Fixture(random, generators)
    }
}
