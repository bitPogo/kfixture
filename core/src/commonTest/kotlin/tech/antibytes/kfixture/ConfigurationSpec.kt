/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("OPT_IN_USAGE")

package tech.antibytes.kfixture

import co.touchlab.stately.isFrozen
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import tech.antibytes.kfixture.generator.RandomWrapper
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
import tech.antibytes.kfixture.mock.DependentGeneratorFactoryStub
import tech.antibytes.kfixture.mock.GeneratorFactoryStub
import tech.antibytes.kfixture.qualifier.qualifiedBy

class ConfigurationSpec {
    @Test
    @JsName("fn0")
    fun `It fulfils Configuration`() {
        val config: Any = Configuration()

        assertTrue(config is PublicApi.Configuration)
    }

    @Test
    @JsName("fn1")
    fun `It fulfils InternalConfiguration`() {
        val config: Any = Configuration()

        assertTrue(config is FixtureContract.Configuration)
    }

    @Test
    @JsName("fn2")
    fun `It has default Seed of 0`() {
        val config = Configuration()

        assertEquals(
            actual = config.seed,
            expected = 0,
        )
    }

    @Test
    @JsName("fn3")
    fun `Given build is called it delegates a Random Instance with the given Seed to the Fixture`() {
        // Given
        val seed = 23

        // When
        val fixture = Configuration(seed).build()

        // Then
        assertTrue(fixture.random is RandomWrapper)
        assertEquals(
            actual = fixture.random.nextDouble(),
            expected = Random(seed).nextDouble(),
        )
    }

    @Test
    @JsName("fn4")
    fun `Given build is called it delegates the default Generators to the Fixture`() {
        // Given
        val seed = 23
        val mapping = mapOf(
            boolean to BooleanGenerator::class,
            booleanArray to BooleanArrayGenerator::class,
            short to ShortGenerator::class,
            shortArray to ShortArrayGenerator::class,
            int to IntegerGenerator::class,
            intArray to IntArrayGenerator::class,
            char to CharGenerator::class,
            charArray to CharArrayGenerator::class,
            float to FloatGenerator::class,
            floatArray to FloatArrayGenerator::class,
            long to LongGenerator::class,
            longArray to LongArrayGenerator::class,
            double to DoubleGenerator::class,
            doubleArray to DoubleArrayGenerator::class,
            string to StringGenerator::class,
            uShort to UShortGenerator::class,
            uShortArray to UShortArrayGenerator::class,
            uInt to UIntegerGenerator::class,
            uIntArray to UIntArrayGenerator::class,
            uLong to ULongGenerator::class,
            uLongArray to ULongArrayGenerator::class,
            byte to ByteGenerator::class,
            byteArray to ByteArrayGenerator::class,
            uByte to UByteGenerator::class,
            uByteArray to UByteArrayGenerator::class,
            any to AnyGenerator::class,
            unit to UnitGenerator::class,
        )

        // When
        val fixture = Configuration(seed).build()

        // Then
        assertEquals(
            actual = mapping.size,
            expected = fixture.generators.size,
        )
        fixture.generators.forEach { (key, generator) ->
            assertTrue(
                mapping.containsKey(key),
                message = "Unknown Key ($key)",
            )

            assertTrue(
                mapping[key]!!.isInstance(generator),
                message = "Unexpected Generator for Key ($key)",
            )
        }
    }

    @Test
    @JsName("fn5")
    fun `Given addGenerator is called with a Klass and a GeneratorFactory it adds the a custom Generator`() {
        // Given
        val klass = TestClass::class
        val generator = TestGenerator
        val seed = 23

        // When
        val config = Configuration(seed)
            .addGenerator(klass, generator)
        val fixture = (config as FixtureContract.Configuration).build()

        // Then
        val generators = fixture.generators

        assertTrue(
            generators.containsKey("tech.antibytes.kfixture.TestClass") || generators.containsKey("TestClass"),
            message = "Missing Key (tech.antibytes.kfixture.TestClass)",
        )
        assertTrue(
            generators["tech.antibytes.kfixture.TestClass"] is TestGenerator ||
                generators["TestClass"] is TestGenerator,
        )

        if (!TestGenerator.lastRandom.isFrozen) {
            assertEquals(
                actual = TestGenerator.lastRandom.nextDouble(),
                expected = Random(seed).nextDouble(),
            )
        }

        assertTrue(TestGenerator.lastRandom is RandomWrapper)
    }

    @Test
    @JsName("fn6")
    fun `Given addGenerator is called with a Klass and a GeneratorFactory it prevents overriding buildins`() {
        // Given
        val klass = Int::class
        val generator = GeneratorFactoryStub<Int>()
        val seed = 23

        // When
        val config = Configuration(seed)
            .addGenerator(klass, generator)
        val fixture = (config as FixtureContract.Configuration).build()

        // Then
        val generators = fixture.generators

        assertNotEquals(
            actual = generators["int"],
            illegal = generator.lastInstance,
        )
    }

    @Test
    @JsName("fn7")
    fun `Given addGenerator is called with a Klass a GeneratorFactory and a Qualifier it prevents overriding buildins`() {
        // Given
        val klass = TestClass::class
        val generator = TestGenerator
        val seed = 42
        val qualifier = "test"

        // When
        val config = Configuration(seed)
            .addGenerator(klass, generator, qualifiedBy(qualifier))

        val fixture = (config as FixtureContract.Configuration).build()

        // Then
        val generators = fixture.generators
        assertTrue(
            generators.containsKey("q:$qualifier:tech.antibytes.kfixture.TestClass") ||
                generators.containsKey("q:$qualifier:TestClass"),
            message = "Missing Key (q:$qualifier:tech.antibytes.kfixture.TestClass)",
        )
        assertTrue(
            generators["q:$qualifier:tech.antibytes.kfixture.TestClass"] is TestGenerator ||
                generators["q:$qualifier:TestClass"] is TestGenerator,
        )
    }

    @Test
    @JsName("fn8")
    fun `Given addGenerator is called with a Klass and a DependentGeneratorFactory it adds the a custom Generator`() {
        // Given
        val klass = TestDependentClass::class
        val generator = TestDependentGenerator
        val seed = 23

        // When
        val config = Configuration(seed)
            .addGenerator(
                klass,
                generator,
            )
        val fixture = (config as FixtureContract.Configuration).build()

        // Then
        val generators = fixture.generators

        assertTrue(
            generators.containsKey("tech.antibytes.kfixture.TestDependentClass") || generators.containsKey("TestDependentClass"),
            message = "Missing Key (tech.antibytes.kfixture.TestDependentClass)",
        )
        assertTrue(
            generators["tech.antibytes.kfixture.TestDependentClass"] is TestDependentGenerator ||
                generators["TestDependentClass"] is TestDependentGenerator,
        )

        assertEquals(
            actual = TestDependentGenerator.lastRandom.nextDouble(),
            expected = Random(seed).nextDouble(),
        )

        assertTrue(TestDependentGenerator.lastRandom is RandomWrapper)
        assertEquals(
            actual = TestDependentGenerator.lastGenerators.size,
            expected = 27,
        )
    }

    @Test
    @JsName("fn9")
    fun `Given addGenerator is called with a Klass and a DependentGeneratorFactory it prevents overriding buildins`() {
        // Given
        val klass = Int::class
        val generator = DependentGeneratorFactoryStub<Int>()
        val seed = 23

        // When
        val config = Configuration(seed)
            .addGenerator(klass, generator)
        val fixture = (config as FixtureContract.Configuration).build()

        // Then
        val generators = fixture.generators

        assertNotEquals(
            actual = generators["int"],
            illegal = generator.lastInstance,
        )
        assertEquals(
            actual = generator.lastGenerators?.size,
            expected = 27,
        )
    }

    @Test
    @JsName("fn10")
    fun `Given addGenerator is called with a Klass a DependentGeneratorFactory and a Qualifier it prevents overriding buildins`() {
        // Given
        val klass = TestDependentClass::class
        val generator = TestDependentGenerator
        val seed = 42
        val qualifier = "test"

        // When
        val config = Configuration(seed)
            .addGenerator(klass, generator, qualifiedBy(qualifier))

        val fixture = (config as FixtureContract.Configuration).build()

        // Then
        val generators = fixture.generators
        assertTrue(
            generators.containsKey("q:$qualifier:tech.antibytes.kfixture.TestDependentClass") ||
                generators.containsKey("q:$qualifier:TestDependentClass"),
            message = "Missing Key (q:$qualifier:tech.antibytes.kfixture.TestDependentClass)",
        )
        assertTrue(
            generators["q:$qualifier:tech.antibytes.kfixture.TestDependentClass"] is TestDependentGenerator ||
                generators["q:$qualifier:TestDependentClass"] is TestDependentGenerator,
        )
    }
}

private data class TestClass(val value: String = "test")
private class TestGenerator : PublicApi.Generator<TestClass> {
    override fun generate(): TestClass = TestClass()

    companion object : PublicApi.GeneratorFactory<TestClass> {
        private val _lastRandom: AtomicRef<Random?> = atomic(null)

        var lastRandom: Random
            get() = _lastRandom.value!!
            set(value) {
                _lastRandom.update { value }
            }

        override fun getInstance(random: Random): PublicApi.Generator<TestClass> {
            return TestGenerator().also {
                lastRandom = random
            }
        }
    }
}

private data class TestDependentClass(val value: String = "testd")
private class TestDependentGenerator : PublicApi.Generator<TestDependentClass> {
    override fun generate(): TestDependentClass = TestDependentClass()

    companion object : PublicApi.DependentGeneratorFactory<TestDependentClass> {
        private val _lastRandom: AtomicRef<Random?> = atomic(null)
        private val _lastGenerators: AtomicRef<Map<String, PublicApi.Generator<out Any>>?> = atomic(null)

        var lastRandom: Random
            get() = _lastRandom.value!!
            set(value) {
                _lastRandom.update { value }
            }

        var lastGenerators: Map<String, PublicApi.Generator<out Any>>
            get() = _lastGenerators.value!!
            set(value) {
                _lastGenerators.update { value }
            }

        override fun getInstance(
            random: Random,
            generators: Map<String, PublicApi.Generator<out Any>>,
        ): PublicApi.Generator<TestDependentClass> {
            return TestDependentGenerator().also {
                lastRandom = random
                lastGenerators = generators.toMap()
            }
        }
    }
}
