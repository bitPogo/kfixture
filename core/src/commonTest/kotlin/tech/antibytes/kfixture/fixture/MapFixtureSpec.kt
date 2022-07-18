/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.fixture

import co.touchlab.stately.collections.sharedMutableListOf
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.atomicfu.atomic
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.int
import tech.antibytes.kfixture.mapFixture
import tech.antibytes.kfixture.mock.GeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mutableMapFixture
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

@Suppress("USELESS_CAST")
class MapFixtureSpec {
    private val random = RandomStub()
    private val capturedMinimum = atomic(-1)
    private val capturedMaximum = atomic(-1)

    @AfterTest
    fun tearDown() {
        random.clear()
        capturedMinimum.getAndSet(-1)
        capturedMaximum.getAndSet(-1)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils Fixture`() {
        val fixture: Any = Fixture(random, emptyMap())

        assertTrue(fixture is PublicApi.Fixture)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given mapFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> 42 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.mapFixture<Int, Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given mapFixture is called it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.mapFixture<Int, Int>()

        // Then
        assertTrue(result is Map<*, *>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 10,
        )
        assertEquals(
            actual = result.size,
            expected = 1,
        )

        assertEquals(
            actual = result.keys,
            expected = setOf(expected),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expected),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given mapFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }
        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.mapFixture<Int, Int?>()

        // Then
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 10,
        )
        assertEquals(
            actual = result.size,
            expected = 1,
        )

        assertEquals(
            actual = result.keys,
            expected = setOf(expected),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(null),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given mapFixture is called with a Key and ValueQualifier it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            ),
        )

        // When
        val result = fixture.mapFixture<Int, Int>(
            StringQualifier(keyQualifier),
            StringQualifier(valueQualifier),
        )

        // Then
        assertEquals(
            actual = result.keys,
            expected = setOf(expected),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expected),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given mapFixture is called with a size it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = GeneratorStub<Int>()

        val randomValues = sharedMutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        generator.generate = { randomValues.removeFirst() }
        random.nextIntRanged = { _, _ -> 23 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(int to generator),
        )

        // When
        val result = fixture.mapFixture<Int, Int>(size = size)

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given fixture is called with a size it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = GeneratorStub<Int>()

        val randomValues = sharedMutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        generator.generate = { randomValues.removeFirst() }
        random.nextIntRanged = { _, _ -> 23 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(int to generator),
        )

        // When
        val result: Map<Int, Int> = fixture.fixture(
            type = Map::class,
            size = size,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn7")
    fun `Given mutableMapFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> 42 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.mutableMapFixture<Int, Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn8")
    fun `Given mutableMapFixture is called it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.mutableMapFixture<Int, Int>()

        // Then
        assertTrue(result is MutableMap<*, *>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 10,
        )
        assertEquals(
            actual = result.size,
            expected = 1,
        )

        assertEquals(
            actual = result.keys,
            expected = mutableSetOf(expected),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expected),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn9")
    fun `Given mutableMapFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }
        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.mutableMapFixture<Int, Int?>()

        // Then
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 10,
        )
        assertEquals(
            actual = result.size,
            expected = 1,
        )

        assertEquals(
            actual = result.keys,
            expected = setOf(expected),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(null),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn10")
    fun `Given mutableMapFixture is called with a Key and ValueQualifier it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            ),
        )

        // When
        val result = fixture.mutableMapFixture<Int, Int>(
            StringQualifier(keyQualifier),
            StringQualifier(valueQualifier),
        )

        // Then
        assertEquals(
            actual = result.keys,
            expected = setOf(expected),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expected),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn11")
    fun `Given mutableMapFixture is called with a size it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = GeneratorStub<Int>()

        val randomValues = sharedMutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        generator.generate = { randomValues.removeFirst() }
        random.nextIntRanged = { _, _ -> 23 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(int to generator),
        )

        // When
        val result = fixture.mutableMapFixture<Int, Int>(size = size)

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn12")
    fun `Given fixture is called with a size it returns a Fixture for the derived Type for the given Size as MutableMap`() {
        // Given
        val size = 5
        val generator = GeneratorStub<Int>()

        val randomValues = sharedMutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        generator.generate = { randomValues.removeFirst() }
        random.nextIntRanged = { _, _ -> 23 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(int to generator),
        )

        // When
        val result: MutableMap<Int, Int> = fixture.fixture(
            type = MutableMap::class,
            size = size,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
    }
}
