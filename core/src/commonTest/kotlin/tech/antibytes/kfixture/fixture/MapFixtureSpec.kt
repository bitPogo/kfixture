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
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mutableMapFixture
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

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
    @JsName("fn0")
    fun `It fulfils Fixture`() {
        val fixture: Any = Fixture(random, emptyMap())

        assertTrue(fixture is PublicApi.Fixture)
    }

    @Test
    @JsName("fn1")
    fun `Given mapFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

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
    @JsName("fn2")
    fun `Given mapFixture is called it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

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
            expected = 11,
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
    @JsName("fn2a")
    fun `Given mapFixture is called with a key generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expectedKey = 42
        val expectedValue = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        generator.generate = { expectedValue }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.mapFixture<Int, Int>(
            keyGenerator = { expectedKey },
        )

        // Then
        assertTrue(result is Map<*, *>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = 1,
        )

        assertEquals(
            actual = result.keys,
            expected = setOf(expectedKey),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expectedValue),
        )
    }

    @Test
    @JsName("fn2b")
    fun `Given mapFixture is called with a value generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expectedKey = 42
        val expectedValue = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        generator.generate = { expectedKey }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.mapFixture<Int, Int>(
            valueGenerator = { expectedValue },
        )

        // Then
        assertTrue(result is Map<*, *>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = 1,
        )

        assertEquals(
            actual = result.keys,
            expected = setOf(expectedKey),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expectedValue),
        )
    }

    @Test
    @JsName("fn2c")
    fun `Given mapFixture is called with a key generator and a value generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expectedKey = 42
        val expectedValue = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.mapFixture(
            keyGenerator = { expectedKey },
            valueGenerator = { expectedValue },
        )

        // Then
        assertTrue(result is Map<*, *>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = 1,
        )

        assertEquals(
            actual = result.keys,
            expected = setOf(expectedKey),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expectedValue),
        )
    }

    @Test
    @JsName("fn3")
    fun `Given mapFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

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
            expected = 11,
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
    @JsName("fn4")
    fun `Given mapFixture is called with a Key and ValueQualifier it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

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
            keyQualifier = StringQualifier(keyQualifier),
            valueQualifier = StringQualifier(valueQualifier),
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
    @JsName("fn4a")
    fun `Given mapFixture is called with a Key and ValueQualifier and key generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expectedKey = 42
        val expectedValue = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedKeyQualifier: PublicApi.Qualifier? = null

        generator.generate = { expectedValue }
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
            keyQualifier = StringQualifier(keyQualifier),
            keyGenerator = { givenQualifier ->
                capturedKeyQualifier = givenQualifier

                expectedKey
            },
            valueQualifier = StringQualifier(valueQualifier),
        )

        // Then
        assertEquals(
            actual = result.keys,
            expected = setOf(expectedKey),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expectedValue),
        )
        assertEquals(
            actual = capturedKeyQualifier?.value,
            expected = StringQualifier(keyQualifier).value,
        )
    }

    @Test
    @JsName("fn4b")
    fun `Given mapFixture is called with a Key and ValueQualifier and value generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expectedKey = 42
        val expectedValue = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedKeyQualifier: PublicApi.Qualifier? = null

        generator.generate = { expectedKey }
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
            keyQualifier = StringQualifier(keyQualifier),
            valueQualifier = StringQualifier(valueQualifier),
            valueGenerator = { givenQualifier ->
                capturedKeyQualifier = givenQualifier

                expectedValue
            },
        )

        // Then
        assertEquals(
            actual = result.keys,
            expected = setOf(expectedKey),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expectedValue),
        )
        assertEquals(
            actual = capturedKeyQualifier?.value,
            expected = StringQualifier(valueQualifier).value,
        )
    }

    @Test
    @JsName("fn4c")
    fun `Given mapFixture is called with a Key and ValueQualifier and both generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expectedKey = 42
        val expectedValue = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedKeyQualifier: PublicApi.Qualifier? = null
        var capturedValueQualifier: PublicApi.Qualifier? = null

        generator.generate = { expectedKey }
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
        val result = fixture.mapFixture(
            keyQualifier = StringQualifier(keyQualifier),
            keyGenerator = { givenQualifier ->
                capturedKeyQualifier = givenQualifier

                expectedKey
            },
            valueQualifier = StringQualifier(valueQualifier),
            valueGenerator = { givenQualifier ->
                capturedValueQualifier = givenQualifier

                expectedValue
            },
        )

        // Then
        assertEquals(
            actual = result.keys,
            expected = setOf(expectedKey),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expectedValue),
        )
        assertEquals(
            actual = capturedKeyQualifier?.value,
            expected = StringQualifier(keyQualifier).value,
        )
        assertEquals(
            actual = capturedValueQualifier?.value,
            expected = StringQualifier(valueQualifier).value,
        )
    }

    @Test
    @JsName("fn5")
    fun `Given mapFixture is called with a size it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

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
    @JsName("fn5a")
    fun `Given mapFixture is called with a size and a key generator it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

        val keys = sharedMutableListOf(11, 12, 13, 14, 15)
        val expectedKeys = keys.toSet()
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
        val result = fixture.mapFixture<Int, Int>(
            keyGenerator = { keys.removeFirst() },
            size = size,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
        assertEquals(
            actual = result.keys,
            expected = expectedKeys,
        )
    }

    @Test
    @JsName("fn5b")
    fun `Given mapFixture is called with a size and a value generator it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

        val values = sharedMutableListOf(11, 12, 13, 14, 15)
        val expectedValues = values.toList()
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
        val result = fixture.mapFixture<Int, Int>(
            valueGenerator = { values.removeFirst() },
            size = size,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
        assertEquals(
            actual = result.values.toList(),
            expected = expectedValues,
        )
    }

    @Test
    @JsName("fn5c")
    fun `Given mapFixture is called with a size and a kex generator and a value generator it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

        val keys = sharedMutableListOf(1, 2, 3, 4, 0)
        val expectedKeys = keys.toSet()
        val values = sharedMutableListOf(11, 12, 13, 14, 15)
        val expectedValues = values.toList()

        random.nextIntRanged = { _, _ -> 23 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(int to generator),
        )

        // When
        val result = fixture.mapFixture(
            keyGenerator = { keys.removeFirst() },
            valueGenerator = { values.removeFirst() },
            size = size,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
        assertEquals(
            actual = result.keys,
            expected = expectedKeys,
        )
        assertEquals(
            actual = result.values.toList(),
            expected = expectedValues,
        )
    }

    @Test
    @JsName("fn6")
    fun `Given fixture is called with a size it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

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
    @JsName("fn6a")
    fun `Given fixture is called with a size and a key generator it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

        val keys = sharedMutableListOf(11, 12, 13, 14, 15)
        val expectedKeys = keys.toSet()
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
            keyGenerator = { keys.removeFirst() },
            size = size,
            type = Map::class,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
        assertEquals(
            actual = result.keys,
            expected = expectedKeys,
        )
    }

    @Test
    @JsName("fn6b")
    fun `Given fixture is called with a size and a value generator it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

        val values = sharedMutableListOf(11, 12, 13, 14, 15)
        val expectedValues = values.toList()
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
            valueGenerator = { values.removeFirst() },
            size = size,
            type = Map::class,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
        assertEquals(
            actual = result.values.toList(),
            expected = expectedValues,
        )
    }

    @Test
    @JsName("fn6c")
    fun `Given fixture is called with a size and a kex generator and a value generator it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

        val keys = sharedMutableListOf(1, 2, 3, 4, 0)
        val expectedKeys = keys.toSet()
        val values = sharedMutableListOf(11, 12, 13, 14, 15)
        val expectedValues = values.toList()

        random.nextIntRanged = { _, _ -> 23 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(int to generator),
        )

        // When
        val result: Map<Int, Int> = fixture.fixture(
            keyGenerator = { keys.removeFirst() },
            valueGenerator = { values.removeFirst() },
            size = size,
            type = Map::class,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
        assertEquals(
            actual = result.keys,
            expected = expectedKeys,
        )
        assertEquals(
            actual = result.values.toList(),
            expected = expectedValues,
        )
    }

    @Test
    @JsName("fn7")
    fun `Given mutableMapFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

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
    @JsName("fn8")
    fun `Given mutableMapFixture is called it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

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
            expected = 11,
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
    @JsName("fn8a")
    fun `Given mutableMapFixture is called with a key generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expectedKey = 42
        val expectedValue = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        generator.generate = { expectedValue }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.mutableMapFixture<Int, Int>(
            keyGenerator = { expectedKey },
        )

        // Then
        assertTrue(result is MutableMap<*, *>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = 1,
        )

        assertEquals(
            actual = result.keys,
            expected = mutableSetOf(expectedKey),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expectedValue),
        )
    }

    @Test
    @JsName("fn8b")
    fun `Given mutableMapFixture is called with a value generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expectedKey = 42
        val expectedValue = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        generator.generate = { expectedKey }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.mutableMapFixture<Int, Int>(
            valueGenerator = { expectedValue },
        )

        // Then
        assertTrue(result is MutableMap<*, *>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = 1,
        )

        assertEquals(
            actual = result.keys,
            expected = mutableSetOf(expectedKey),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expectedValue),
        )
    }

    @Test
    @JsName("fn8c")
    fun `Given mutableMapFixture is called with a key generator and a value generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expectedKey = 42
        val expectedValue = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.mutableMapFixture(
            keyGenerator = { expectedKey },
            valueGenerator = { expectedValue },
        )

        // Then
        assertTrue(result is MutableMap<*, *>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = 1,
        )

        assertEquals(
            actual = result.keys,
            expected = mutableSetOf(expectedKey),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expectedValue),
        )
    }

    @Test
    @JsName("fn9")
    fun `Given mutableMapFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

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
            expected = 11,
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
    @JsName("fn10")
    fun `Given mutableMapFixture is called with a Key and ValueQualifier it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

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
    @JsName("fn10a")
    fun `Given mutableMapFixture is called with a Key and ValueQualifier and a key generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expectedKey = 42
        val expectedValue = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedKeyQualifier: PublicApi.Qualifier? = null

        generator.generate = { expectedValue }
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
            keyQualifier = StringQualifier(keyQualifier),
            keyGenerator = { givenQualifier ->
                capturedKeyQualifier = givenQualifier

                expectedKey
            },
            valueQualifier = StringQualifier(valueQualifier),
        )

        // Then
        assertEquals(
            actual = result.keys,
            expected = setOf(expectedKey),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expectedValue),
        )
        assertEquals(
            actual = capturedKeyQualifier?.value,
            expected = StringQualifier(keyQualifier).value,
        )
    }

    @Test
    @JsName("fn10b")
    fun `Given mutableMapFixture is called with a Key and ValueQualifier and a value generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expectedKey = 42
        val expectedValue = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedValueQualifier: PublicApi.Qualifier? = null

        generator.generate = { expectedKey }
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
            keyQualifier = StringQualifier(keyQualifier),
            valueQualifier = StringQualifier(valueQualifier),
            valueGenerator = { givenQualifier ->
                capturedValueQualifier = givenQualifier

                expectedValue
            },
        )

        // Then
        assertEquals(
            actual = result.keys,
            expected = setOf(expectedKey),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expectedValue),
        )
        assertEquals(
            actual = capturedValueQualifier?.value,
            expected = StringQualifier(valueQualifier).value,
        )
    }

    @Test
    @JsName("fn10c")
    fun `Given mutableMapFixture is called with a Key and ValueQualifier and a key generator and a value generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expectedKey = 42
        val expectedValue = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedKeyQualifier: PublicApi.Qualifier? = null
        var capturedValueQualifier: PublicApi.Qualifier? = null

        generator.generate = { expectedKey }
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
        val result = fixture.mutableMapFixture(
            keyQualifier = StringQualifier(keyQualifier),
            keyGenerator = { givenQualifier ->
                capturedKeyQualifier = givenQualifier

                expectedKey
            },
            valueQualifier = StringQualifier(valueQualifier),
            valueGenerator = { givenQualifier ->
                capturedValueQualifier = givenQualifier

                expectedValue
            },
        )

        // Then
        assertEquals(
            actual = result.keys,
            expected = setOf(expectedKey),
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expectedValue),
        )
        assertEquals(
            actual = capturedKeyQualifier?.value,
            expected = StringQualifier(keyQualifier).value,
        )
        assertEquals(
            actual = capturedValueQualifier?.value,
            expected = StringQualifier(valueQualifier).value,
        )
    }

    @Test
    @JsName("fn11")
    fun `Given mutableMapFixture is called with a size it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

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
    @JsName("fn11a")
    fun `Given mutableMapFixture is called with a size and a key generator it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

        val keys = sharedMutableListOf(11, 12, 13, 14, 15)
        val expectedKeys = keys.toSet()
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
        val result = fixture.mutableMapFixture<Int, Int>(
            keyGenerator = { keys.removeFirst() },
            size = size,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
        assertEquals(
            actual = result.keys,
            expected = expectedKeys,
        )
    }

    @Test
    @JsName("fn11b")
    fun `Given mutableMapFixture is called with a size and a value generator it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

        val values = sharedMutableListOf(11, 12, 13, 14, 15)
        val expectedValues = values.toList()
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
        val result = fixture.mutableMapFixture<Int, Int>(
            valueGenerator = { values.removeFirst() },
            size = size,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
        assertEquals(
            actual = result.values.toList(),
            expected = expectedValues,
        )
    }

    @Test
    @JsName("fn11c")
    fun `Given mutableMapFixture is called with a size and a key generator and a value generator it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

        val keys = sharedMutableListOf(1, 2, 3, 4, 0)
        val expectedKeys = keys.toSet()
        val values = sharedMutableListOf(11, 12, 13, 14, 15)
        val expectedValues = values.toList()

        random.nextIntRanged = { _, _ -> 23 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(int to generator),
        )

        // When
        val result: Map<Int, Int> = fixture.mutableMapFixture(
            keyGenerator = { keys.removeFirst() },
            valueGenerator = { values.removeFirst() },
            size = size,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
        assertEquals(
            actual = result.keys,
            expected = expectedKeys,
        )
        assertEquals(
            actual = result.values.toList(),
            expected = expectedValues,
        )
    }

    @Test
    @JsName("fn12")
    fun `Given fixture is called with a size it returns a Fixture for the derived Type for the given Size as MutableMap`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

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

    @Test
    @JsName("fn12a")
    fun `Given fixture is called with a size and a key generator it returns a Fixture for the derived Type for the given Size for a MutableMap`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

        val keys = sharedMutableListOf(11, 12, 13, 14, 15)
        val expectedKeys = keys.toSet()
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
            keyGenerator = { keys.removeFirst() },
            size = size,
            type = MutableMap::class,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
        assertEquals(
            actual = result.keys,
            expected = expectedKeys,
        )
    }

    @Test
    @JsName("fn12b")
    fun `Given fixture is called with a size and a value generator it returns a Fixture for the derived Type for the given Size for a MutableMap`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

        val values = sharedMutableListOf(11, 12, 13, 14, 15)
        val expectedValues = values.toList()
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
            valueGenerator = { values.removeFirst() },
            size = size,
            type = MutableMap::class,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
        assertEquals(
            actual = result.values.toList(),
            expected = expectedValues,
        )
    }

    @Test
    @JsName("fn12c")
    fun `Given fixture is called with a size and a key generator and a value generator it returns a Fixture for the derived Type for the given Size for a MutableMap`() {
        // Given
        val size = 5
        val generator = FilterableGeneratorStub<Int, Int>()

        val keys = sharedMutableListOf(1, 2, 3, 4, 0)
        val expectedKeys = keys.toSet()
        val values = sharedMutableListOf(11, 12, 13, 14, 15)
        val expectedValues = values.toList()

        random.nextIntRanged = { _, _ -> 23 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(int to generator),
        )

        // When
        val result: MutableMap<Int, Int> = fixture.fixture(
            keyGenerator = { keys.removeFirst() },
            valueGenerator = { values.removeFirst() },
            size = size,
            type = MutableMap::class,
        )

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size,
        )
        assertEquals(
            actual = result.keys,
            expected = expectedKeys,
        )
        assertEquals(
            actual = result.values.toList(),
            expected = expectedValues,
        )
    }
}
