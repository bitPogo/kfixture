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
import kotlin.test.assertTrue
import kotlinx.atomicfu.atomic
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.int
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.RangedNumericArrayGeneratorStub
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

class RangedNumericArrayFixtureSpec {
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
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()
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
                size = 42,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn3")
    fun `Given fixture is called with a upper and lower bound it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val expectedSize = 97
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedSize: Int? = null

        generator.generateWithRange = { givenFrom, givenTo, givenSize ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedSize = givenSize

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture(
            from = expectedFrom,
            to = expectedTo,
            size = expectedSize,
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
        assertEquals(
            actual = capturedSize,
            expected = expectedSize,
        )
    }

    @Test
    @JsName("fn4")
    fun `Given fixture is called with a upper and lower bound it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val expectedSize = 39
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedSize: Int? = null

        generator.generateWithRange = { givenFrom, givenTo, givenSize ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedSize = givenSize

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
            size = expectedSize,
        )

        // Then
        assertNull(result)
        assertNull(capturedFrom)
        assertNull(capturedTo)
        assertNull(capturedSize)
    }

    @Test
    @JsName("fn5")
    fun `Given fixture is called  with a upper and lower bound and a qualifier it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedFrom = 12
        val expectedTo = 42
        val expectedSize = 39
        val qualifier = "test"
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedSize: Int? = null

        generator.generateWithRange = { givenFrom, givenTo, givenSize ->
            capturedFrom = givenFrom
            capturedTo = givenTo
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
            from = expectedFrom,
            to = expectedTo,
            size = expectedSize,
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
        assertEquals(
            actual = capturedSize,
            expected = expectedSize,
        )
    }

    @Test
    @JsName("fn6")
    fun `Given fixture is called with a range it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()
        generator.generateWithRange = { _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int>(ranges = arrayOf(0..23))
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn7")
    fun `Given fixture is called with a range and size it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()
        generator.generateWithRange = { _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, Int>(ranges = arrayOf(0..23), size = 23)
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn8")
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
            fixture.fixture<Int, Int>(ranges = arrayOf(0..23))
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn9")
    fun `Given fixture is called with a range and size it fails the corresponding Generator is not a RangedGenerator`() {
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
            fixture.fixture<Int, Int>(ranges = arrayOf(0..23), size = 23)
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn10")
    fun `Given fixture is called with a range it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedRange = 12..42
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()

        var capturedRange: Array<out ClosedRange<Int>>? = null

        generator.generateWithRanges = { givenRange ->
            capturedRange = givenRange

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture(ranges = arrayOf(expectedRange))

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertTrue {
            capturedRange.contentEquals(arrayOf(expectedRange))
        }
    }

    @Test
    @JsName("fn11")
    fun `Given fixture is called with a range and size it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedRange = 12..42
        val expectedSize = 39
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()

        var capturedRange: Array<out ClosedRange<Int>>? = null
        var capturedSize: Int? = null

        generator.generateWithRangesAndSize = { givenRange, givenSize ->
            capturedRange = givenRange
            capturedSize = givenSize

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture(expectedRange, size = expectedSize)

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertTrue {
            capturedRange.contentEquals(arrayOf(expectedRange))
        }
        assertEquals(
            actual = capturedSize,
            expected = expectedSize,
        )
    }

    @Test
    @JsName("fn12")
    fun `Given fixture is called with a range it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val expectedRange = 12..42
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()

        var capturedRange: Array<out ClosedRange<Int>>? = null

        generator.generateWithRanges = { givenRange ->
            capturedRange = givenRange

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture(ranges = arrayOf(expectedRange))

        // Then
        assertNull(result)
        assertNull(capturedRange)
    }

    @Test
    @JsName("fn13")
    fun `Given fixture is called with a range and size it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val expectedRange = 12..42
        val expectedSize = 39
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()

        var capturedRange: Array<out ClosedRange<Int>>? = null
        var capturedSize: Int? = null

        generator.generateWithRangesAndSize = { givenRange, givenSize ->
            capturedRange = givenRange
            capturedSize = givenSize

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture(expectedRange, size = expectedSize)

        // Then
        assertNull(result)
        assertNull(capturedRange)
        assertNull(capturedSize)
    }

    @Test
    @JsName("fn14")
    fun `Given fixture is called with a range and a qualifier it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedRange = 12..42
        val qualifier = "test"
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()

        var capturedRange: Array<out ClosedRange<Int>>? = null

        generator.generateWithRanges = { givenRange ->
            capturedRange = givenRange

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
            ranges = arrayOf(expectedRange),
            qualifier = StringQualifier(qualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertTrue {
            capturedRange.contentEquals(arrayOf(expectedRange))
        }
    }

    @Test
    @JsName("fn16")
    fun `Given fixture is called with a range and size and a qualifier it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedRange = 12..42
        val expectedSize = 39
        val qualifier = "test"
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()

        var capturedRange: Array<out ClosedRange<Int>>? = null
        var capturedSize: Int? = null

        generator.generateWithRangesAndSize = { givenRange, givenSize ->
            capturedRange = givenRange
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
            expectedRange,
            qualifier = StringQualifier(qualifier),
            size = expectedSize,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertTrue {
            capturedRange.contentEquals(arrayOf(expectedRange))
        }
        assertEquals(
            actual = capturedSize,
            expected = expectedSize,
        )
    }
}
