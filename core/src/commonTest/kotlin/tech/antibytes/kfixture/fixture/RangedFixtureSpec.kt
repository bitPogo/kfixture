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
import kotlinx.atomicfu.atomic
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.int
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.RangedGeneratorStub
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

class RangedFixtureSpec {
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
    fun `Given fixture is called with a upper and lower bound it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = RangedGeneratorStub<Int, Int>()
        generator.generateWithRange = { _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int>(
                from = 0,
                to = 23,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn2")
    fun `Given fixture is called with a upper and lower bound and a predicate it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = RangedGeneratorStub<Int, Int>()
        generator.generateWithRange = { _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int>(
                from = 0,
                to = 23,
            ) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn3")
    fun `Given fixture is called with a upper and lower bound it fails the corresponding Generator is not a RangedGenerator`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int>(
                from = 0,
                to = 23,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn4")
    fun `Given fixture is called with a upper and lower bound and predicate it fails the corresponding Generator is not a RangedGenerator`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int>(
                from = 0,
                to = 23,
            ) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn5")
    fun `Given fixture is called with a upper and lower bound it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val generator = RangedGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture(
            from = expectedFrom,
            to = expectedTo,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = expectedFrom,
        )
        assertEquals(
            actual = capturedTo,
            expected = expectedTo,
        )
    }

    @Test
    @JsName("fn6")
    fun `Given fixture is called with a upper and lower bound and predicate it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedPredicate: Function1<Int?, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture(
            from = expectedFrom,
            to = expectedTo,
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = expectedFrom,
        )
        assertEquals(
            actual = capturedTo,
            expected = expectedTo,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @JsName("fn7")
    fun `Given fixture is called with a upper and lower bound it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val generator = RangedGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture(
            from = expectedFrom,
            to = expectedTo,
        )

        // Then
        assertNull(result)
        assertNull(capturedFrom)
        assertNull(capturedTo)
    }

    @Test
    @JsName("fn8")
    fun `Given fixture is called with a upper and lower bound with a predicate it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedPredicate: Function1<Int, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture(
            from = expectedFrom,
            to = expectedTo,
            predicate = expectedPredicate,
        )

        // Then
        assertNull(result)
        assertNull(capturedFrom)
        assertNull(capturedTo)
        assertNull(capturedPredicate)
    }

    @Test
    @JsName("fn9")
    fun `Given fixture is called  with a upper and lower bound and a qualifier it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val qualifier = "test"
        val generator = RangedGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

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
            from = expectedFrom,
            to = expectedTo,
            qualifier = StringQualifier(qualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = expectedFrom,
        )
        assertEquals(
            actual = capturedTo,
            expected = expectedTo,
        )
    }

    @Test
    @JsName("fn10")
    fun `Given fixture is called  with a upper and lower bound and a qualifier and a predicate it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val qualifier = "test"
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedPredicate: Function1<Int, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
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
            from = expectedFrom,
            to = expectedTo,
            qualifier = StringQualifier(qualifier),
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = expectedFrom,
        )
        assertEquals(
            actual = capturedTo,
            expected = expectedTo,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @JsName("fn11")
    fun `Given fixture is called with a range it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = RangedGeneratorStub<Int, Int>()
        generator.generateWithRange = { _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int>(range = 0..23)
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn12")
    fun `Given fixture is called with a range and a predicate it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = RangedGeneratorStub<Int, Int>()
        generator.generateWithRange = { _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int>(
                range = 0..23,
            ) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn13")
    fun `Given fixture is called with a range it fails the corresponding Generator is not a RangedGenerator`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int>(range = 0..23)
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn14")
    fun `Given fixture is called with a range and a predicate it fails the corresponding Generator is not a RangedGenerator`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int>(range = 0..23) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn15")
    fun `Given fixture is called with a range it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val generator = RangedGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture(range = expectedFrom..expectedTo)

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = expectedFrom,
        )
        assertEquals(
            actual = capturedTo,
            expected = expectedTo,
        )
    }

    @Test
    @JsName("fn16")
    fun `Given fixture is called with a range and a predicate it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedPredicate: Function1<Int, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture(range = expectedFrom..expectedTo, predicate = expectedPredicate)

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = expectedFrom,
        )
        assertEquals(
            actual = capturedTo,
            expected = expectedTo,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @JsName("fn17")
    fun `Given fixture is called with a range it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val generator = RangedGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture(range = expectedFrom..expectedTo)

        // Then
        assertNull(result)
        assertNull(capturedFrom)
        assertNull(capturedTo)
    }

    @Test
    @JsName("fn18")
    fun `Given fixture is called with a range and a predicate it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedPredicate: Function1<Int, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture(range = expectedFrom..expectedTo, predicate = expectedPredicate)

        // Then
        assertNull(result)
        assertNull(capturedFrom)
        assertNull(capturedTo)
        assertNull(capturedPredicate)
    }

    @Test
    @JsName("fn19")
    fun `Given fixture is called with a range and a qualifier it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val qualifier = "test"
        val generator = RangedGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

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
            expectedFrom..expectedTo,
            qualifier = StringQualifier(qualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = expectedFrom,
        )
        assertEquals(
            actual = capturedTo,
            expected = expectedTo,
        )
    }

    @Test
    @JsName("fn20")
    fun `Given fixture is called with a range and a qualifier and a predicate it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val qualifier = "test"
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedPredicate: Function1<Int, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
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
            expectedFrom..expectedTo,
            qualifier = StringQualifier(qualifier),
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = expectedFrom,
        )
        assertEquals(
            actual = capturedTo,
            expected = expectedTo,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }
}
