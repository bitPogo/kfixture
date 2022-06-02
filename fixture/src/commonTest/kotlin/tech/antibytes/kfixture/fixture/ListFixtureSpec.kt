/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.fixture

import co.touchlab.stately.isolate.IsolateState
import kotlinx.atomicfu.atomic
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.int
import tech.antibytes.kfixture.listFixture
import tech.antibytes.kfixture.mock.GeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mutableListFixture
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@Suppress("USELESS_CAST")
class ListFixtureSpec {
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
    fun `Given listFixture is called it returns a Fixture for the derived Type`() {
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
        val result: Any = fixture.listFixture<Int>()

        // Then
        assertTrue(result is List<*>)
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
    fun `Given fixture is called with a size it returns a Fixture for the derived List Type in the given size`() {
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
        val result: List<Int> = fixture.fixture(
            type = List::class
        )

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
    @JsName("fn12")
    fun `Given mutableListFixture is called it fails if the Type has no corresponding Generator`() {
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
            fixture.mutableListFixture<Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int)."
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn13")
    fun `Given mutableListFixture is called it returns a Fixture for the derived Type`() {
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
        val result: Any = fixture.mutableListFixture<Int>()

        // Then
        assertTrue(result is MutableList<*>)
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
            expected = mutableListOf(
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
    @JsName("fn14")
    fun `Given mutableListFixture is called it returns a Fixture while respecting nullability`() {
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
        val result = fixture.mutableListFixture<Int?>()

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
        assertEquals<MutableList<Int?>>(
            actual = result,
            expected = mutableListOf(
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
    @JsName("fn15")
    fun `Given mutableListFixture is called with a qualifier it returns a Fixture for the derrived Type`() {
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
        val result = fixture.mutableListFixture<Int>(StringQualifier(qualifier))

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
    @JsName("fn16")
    fun `Given mutableListFixture is called with a size it returns a Fixture for the derived Type in the given size`() {
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
        val result = fixture.mutableListFixture<Int>(size = size)

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
    @JsName("fn17")
    fun `Given fixture is called with a size it returns a Fixture for the derived MutableList Type in the given size`() {
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
        val result: MutableList<Int> = fixture.fixture(
            type = MutableList::class
        )

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
}
