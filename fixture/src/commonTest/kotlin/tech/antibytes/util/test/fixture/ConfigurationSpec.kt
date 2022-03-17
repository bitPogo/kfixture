/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.test.fixture

import co.touchlab.stately.isFrozen
import co.touchlab.stately.isolate.IsolateState
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
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
import tech.antibytes.util.test.fixture.mock.GeneratorFactoryStub
import tech.antibytes.util.test.fixture.qualifier.named
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

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
            expected = 0
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
        assertEquals(
            actual = fixture.random.access { it.nextDouble() },
            expected = Random(seed).nextDouble()
        )
    }

    @Test
    @JsName("fn4")
    fun `Given build is called it delegates the default Generators to the Fixture`() {
        // Given
        val seed = 23
        val mapping = mapOf(
            boolean to BooleanGenerator::class,
            short to ShortGenerator::class,
            int to IntegerGenerator::class,
            char to CharGenerator::class,
            float to FloatGenerator::class,
            long to LongGenerator::class,
            double to DoubleGenerator::class,
            string to StringGenerator::class,
            uShort to UShortGenerator::class,
            uInt to UIntegerGenerator::class,
            uLong to ULongGenerator::class,
            byteArray to ByteArrayGenerator::class,
            uByteArray to UByteArrayGenerator::class,
            any to AnyGenerator::class,
            unit to UnitGenerator::class
        )

        // When
        val fixture = Configuration(seed).build()

        // Then
        assertEquals(
            actual = mapping.size,
            expected = fixture.generators.size
        )
        fixture.generators.forEach { (key, generator) ->
            assertTrue(
                mapping.containsKey(key),
                message = "Unknown Key ($key)"
            )

            assertTrue(
                mapping[key]!!.isInstance(generator),
                message = "Unexpected Generator for Key ($key)"
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
            generators.containsKey("tech.antibytes.util.test.fixture.TestClass") || generators.containsKey("TestClass"),
            message = "Missing Key (tech.antibytes.util.test.fixture.TestClass)"
        )
        assertTrue(
            generators["tech.antibytes.util.test.fixture.TestClass"] is TestGenerator ||
                generators["TestClass"] is TestGenerator
        )

        if (!TestGenerator.lastRandom.isFrozen) {
            assertEquals(
                actual = TestGenerator.lastRandom.access { it.nextDouble() },
                expected = Random(seed).nextDouble()
            )
        }
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
            illegal = generator.lastInstance
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
            .addGenerator(klass, generator, named(qualifier))

        val fixture = (config as FixtureContract.Configuration).build()

        // Then
        val generators = fixture.generators
        assertTrue(
            generators.containsKey("q:$qualifier:tech.antibytes.util.test.fixture.TestClass") ||
                generators.containsKey("q:$qualifier:TestClass"),
            message = "Missing Key (q:$qualifier:tech.antibytes.util.test.fixture.TestClass)"
        )
        assertTrue(
            generators["q:$qualifier:tech.antibytes.util.test.fixture.TestClass"] is TestGenerator ||
                generators["q:$qualifier:TestClass"] is TestGenerator
        )
    }
}

private data class TestClass(val value: String = "test")
private class TestGenerator : PublicApi.Generator<TestClass> {
    override fun generate(): TestClass = TestClass()

    companion object : PublicApi.GeneratorFactory<TestClass> {
        private val _lastRandom: AtomicRef<IsolateState<Random>?> = atomic(null)

        var lastRandom: IsolateState<Random>
            get() = _lastRandom.value!!
            set(value) {
                _lastRandom.update { value }
            }

        override fun getInstance(random: IsolateState<Random>): PublicApi.Generator<TestClass> {
            return TestGenerator().also {
                lastRandom = random
            }
        }
    }
}
