/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import co.touchlab.stately.collections.sharedMutableListOf
import co.touchlab.stately.isolate.IsolateState
import kotlinx.atomicfu.atomic
import tech.antibytes.kfixture.mock.GeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.qualifier.StringQualifier
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FixtureSpec {
    private val random = IsolateState { RandomStub() }
    private val capturedMinimum = atomic(-1)
    private val capturedMaximum = atomic(-1)

    @AfterTest
    fun tearDown() {
        random.access { it.clear() }
        capturedMinimum.getAndSet(-1)
        capturedMaximum.getAndSet(-1)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils Fixture`() {
        val fixture: Any = Fixture(random as IsolateState<Random>, emptyMap())

        assertTrue(fixture is PublicApi.Fixture)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given fixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int)."
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given fixture is called it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture()

        // Then
        assertEquals(
            actual = result,
            expected = expected
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given fixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()

        random.access { it.nextBoolean = { true } }
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture()

        // Then
        assertNull(result)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given fixture is called with a qualifier it returns a Fixture for the derrived Type`() {
        // Given
        val expected = 23
        val qualifier = "test"
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random as IsolateState<Random>,
            mapOf("q:$qualifier:$int" to generator)
        )

        // When
        val result: Int = fixture.fixture(StringQualifier(qualifier))

        // Then
        assertEquals(
            actual = result,
            expected = expected
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given listFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }
        random.access { it.nextIntRanged = { _, _ -> 42 } }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.listFixture<Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int)."
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn7")
    fun `Given listFixture is called it returns a Fixture for the derrived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.access { stub ->
            (stub as RandomStub).nextIntRanged = { givenMinimum, givenMaximum ->
                capturedMinimum.getAndSet(givenMinimum)
                capturedMaximum.getAndSet(givenMaximum)
                size
            }
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, mapOf(int to generator))

        // When
        val result = fixture.listFixture<Int>()

        // Then
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 10
        )
        assertEquals(
            actual = result.size,
            expected = size
        )
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected
            )
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn8")
    fun `Given listFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.access { stub ->
            (stub as RandomStub).nextIntRanged = { givenMinimum, givenMaximum ->
                capturedMinimum.getAndSet(givenMinimum)
                capturedMaximum.getAndSet(givenMaximum)
                size
            }
        }

        random.access { it.nextBoolean = { true } }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, mapOf(int to generator))

        // When
        val result = fixture.listFixture<Int?>()

        // Then
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 10
        )
        assertEquals(
            actual = result.size,
            expected = size
        )
        assertEquals(
            actual = result,
            expected = listOf(
                null,
                null,
                null,
                null,
                null
            )
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn9")
    fun `Given listFixture is called with a qualifier it returns a Fixture for the derrived Type`() {
        // Given
        val size = 5
        val expected = 23
        val qualifier = "test"
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.access { it.nextIntRanged = { _, _ -> size } }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, mapOf("q:$qualifier:$int" to generator))

        // When
        val result = fixture.listFixture<Int>(StringQualifier(qualifier))

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected
            )
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn10")
    fun `Given listFixture is called with a size it returns a Fixture for the derived Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.access { it.nextIntRanged = { _, _ -> size } }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, mapOf(int to generator))

        // When
        val result = fixture.listFixture<Int>(size = size)

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected
            )
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn11")
    fun `Given pairFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.pairFixture<Int, Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int)."
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn12")
    fun `Given pairFixture is called it returns a Fixture for the derrived Type`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, mapOf(int to generator))

        // When
        val result = fixture.pairFixture<Int, Int>()

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expected, expected)
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn13")
    fun `Given pairFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.access { it.nextBoolean = { true } }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, mapOf(int to generator))

        // When
        val result = fixture.pairFixture<Int, Int?>()

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expected, null)
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn14")
    fun `Given pairFixture is called with qualifiers it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random as IsolateState<Random>,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            )
        )

        // When
        val result = fixture.pairFixture<Int, Int>(
            StringQualifier(keyQualifier),
            StringQualifier(valueQualifier)
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expected, expected)
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn15")
    fun `Given mapFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.access { it.nextIntRanged = { _, _ -> 42 } }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.mapFixture<Int, Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int)."
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn16")
    fun `Given mapFixture is called it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.access { stub ->
            (stub as RandomStub).nextIntRanged = { givenMinimum, givenMaximum ->
                capturedMinimum.getAndSet(givenMinimum)
                capturedMaximum.getAndSet(givenMaximum)
                size
            }
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, mapOf(int to generator))

        // When
        val result = fixture.mapFixture<Int, Int>()

        // Then
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 10
        )
        assertEquals(
            actual = result.size,
            expected = 1
        )

        assertEquals(
            actual = result.keys,
            expected = setOf(expected)
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expected)
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn17")
    fun `Given mapFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.access { stub ->
            (stub as RandomStub).nextIntRanged = { givenMinimum, givenMaximum ->
                capturedMinimum.getAndSet(givenMinimum)
                capturedMaximum.getAndSet(givenMaximum)
                size
            }
        }
        random.access { it.nextBoolean = { true } }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, mapOf(int to generator))

        // When
        val result = fixture.mapFixture<Int, Int?>()

        // Then
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 10
        )
        assertEquals(
            actual = result.size,
            expected = 1
        )

        assertEquals(
            actual = result.keys,
            expected = setOf(expected)
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(null)
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn18")
    fun `Given mapFixture is called with a Key and ValueQualifier it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.access { it.nextIntRanged = { _, _ -> size } }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random as IsolateState<Random>,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            )
        )

        // When
        val result = fixture.mapFixture<Int, Int>(
            StringQualifier(keyQualifier),
            StringQualifier(valueQualifier),
        )

        // Then
        assertEquals(
            actual = result.keys,
            expected = setOf(expected)
        )
        assertEquals(
            actual = result.values.toList(),
            expected = listOf(expected)
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn19")
    fun `Given mapFixture is called with a size it returns a Fixture for the derived Type for the given Size`() {
        // Given
        val size = 5
        val generator = GeneratorStub<Int>()

        val randomValues = sharedMutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        generator.generate = { randomValues.removeFirst() }
        random.access { it.nextIntRanged = { _, _ -> 23 } }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random as IsolateState<Random>,
            mapOf(int to generator)
        )

        // When
        val result = fixture.mapFixture<Int, Int>(size = size)

        // Then
        assertEquals(
            actual = result.keys.size,
            expected = size
        )
    }
}
