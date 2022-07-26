/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.fixture

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.int
import tech.antibytes.kfixture.mock.FilterableArrayGeneratorStub
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.GeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

class FilterableFixtureSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils Fixture`() {
        val fixture: Any = Fixture(random, emptyMap())

        assertTrue(fixture is PublicApi.Fixture)
    }

    @Test
    @JsName("fn1")
    fun `Given fixture is called with a predicate it fails if the Type has no corresponding Generator`() {
        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int> { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn2")
    fun `Given fixture is called with a predicate it fails if the Type the corresponding Generator is not filterable`() {
        // Given
        val generator = GeneratorStub<Int>()

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int> { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn3")
    fun `Given fixture is called with a predicate it returns the derived type`() {
        // Given
        val expected = 23
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedPredicate: Function1<Int?, Boolean>? = null

        generator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture(predicate = expectedPredicate)

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @JsName("fn4")
    fun `Given fixture is called with a predicate it returns the derived type while respecting nullability`() {
        // Given
        val expected = 23
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedPredicate: Function1<Int?, Boolean>? = null

        generator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture(predicate = expectedPredicate)

        // Then
        assertNull(result)
        assertNull(capturedPredicate)
    }

    @Test
    @JsName("fn5")
    fun `Given fixture is called with a qualifier and a predicate it returns the derived type`() {
        // Given
        val expected = 23
        val qualifier = "test"
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedPredicate: Function1<Int?, Boolean>? = null

        generator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$int" to generator),
        )

        // When
        val result: Int = fixture.fixture(
            qualifier = StringQualifier(qualifier),
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @JsName("fn6")
    fun `Given fixture is called with a size and a predicate it fails if the Type has no corresponding Generator`() {
        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int>(23) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn7")
    fun `Given fixture is called with a size and a predicate it fails if the Type the corresponding Generator is not filterable`() {
        // Given
        val generator = GeneratorStub<Int>()

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int>(23) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn8")
    fun `Given fixture is called with a size and a predicate it returns the derived type`() {
        // Given
        val size = 23
        val expected = 23
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = FilterableArrayGeneratorStub<Int, Int>()

        var capturedPredicate: Function1<Int?, Boolean>? = null
        var capturedSize: Int? = null

        generator.generateWithPredicate = { givenSize, givenPredicate ->
            capturedPredicate = givenPredicate
            capturedSize = givenSize

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture(size = size, predicate = expectedPredicate)

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedSize,
            expected = size,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @JsName("fn9")
    fun `Given fixture is called with a size and a predicate it returns the derived type while respecting nullability`() {
        // Given
        val size = 23
        val expected = 23
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = FilterableArrayGeneratorStub<Int, Int>()

        var capturedPredicate: Function1<Int?, Boolean>? = null
        var capturedSize: Int? = null

        generator.generateWithPredicate = { givenSize, givenPredicate ->
            capturedPredicate = givenPredicate
            capturedSize = givenSize

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture(size = size, predicate = expectedPredicate)

        // Then
        assertNull(result)
        assertNull(capturedPredicate)
        assertNull(capturedSize)
    }

    @Test
    @JsName("fn10")
    fun `Given fixture is called with a size and a qualifier and a predicate it returns the derived type`() {
        // Given
        val size = 23
        val expected = 23
        val qualifier = "test"
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = FilterableArrayGeneratorStub<Int, Int>()

        var capturedPredicate: Function1<Int?, Boolean>? = null
        var capturedSize: Int? = null

        generator.generateWithPredicate = { givenSize, givenPredicate ->
            capturedPredicate = givenPredicate
            capturedSize = givenSize

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$int" to generator),
        )

        // When
        val result: Int = fixture.fixture(
            size = size,
            qualifier = StringQualifier(qualifier),
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedSize,
            expected = size,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }
}
