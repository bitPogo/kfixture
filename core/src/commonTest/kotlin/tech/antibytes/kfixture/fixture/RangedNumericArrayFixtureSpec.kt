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
import tech.antibytes.kfixture.intArray
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.RangedNumericArrayGeneratorStub
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

class RangedNumericArrayFixtureSpec {
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
    fun `Given fixture is called with a upper and lower bound it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()
        generator.generateWithRange = { _, _, _, _ ->
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
        val expected = intArrayOf(23)
        val generator = FilterableGeneratorStub<Int, IntArray>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, IntArray>(
                from = 0,
                to = 23,
                size = 42,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($intArray).",
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

        generator.generateWithRange = { givenFrom, givenTo, givenSize, _ ->
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
    @JsName("fn3a")
    fun `Given fixture is called with a upper and lower bound and predicate it returns a Fixture for the derived Type`() {
        // Given
        val expected = intArrayOf(23)
        val expectedFrom = 12
        val expectedTo = 42
        val expectedSize = 97
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedNumericArrayGeneratorStub<Int, IntArray>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedSize: Int? = null
        var capturedPredicate: Function1<Int?, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenSize, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedSize = givenSize
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(intArray to generator))

        // When
        val result: IntArray = fixture.fixture(
            from = expectedFrom,
            to = expectedTo,
            size = expectedSize,
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

        generator.generateWithRange = { givenFrom, givenTo, givenSize, _ ->
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
    @JsName("fn4a")
    fun `Given fixture is called with a upper and lower bound it and a predicate returns a Fixture while respecting nullability`() {
        // Given
        val expected = intArrayOf(23)
        val expectedFrom = 12
        val expectedTo = 42
        val expectedSize = 39
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedNumericArrayGeneratorStub<Int, IntArray>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedSize: Int? = null
        var capturedPredicate: Function1<Int?, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenSize, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedSize = givenSize
            capturedPredicate = givenPredicate

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(IntArray::class)

        val fixture = Fixture(random, mapOf(intArray to generator))

        // When
        val result: IntArray? = fixture.fixture(
            from = expectedFrom,
            to = expectedTo,
            size = expectedSize,
            predicate = expectedPredicate,
        )

        // Then
        assertNull(result)
        assertNull(capturedFrom)
        assertNull(capturedTo)
        assertNull(capturedSize)
        assertNull(capturedPredicate)
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

        generator.generateWithRange = { givenFrom, givenTo, givenSize, _ ->
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
    @JsName("fn5a")
    fun `Given fixture is called  with a upper and lower bound and a qualifier and a predicate it returns a Fixture for the derived Type`() {
        // Given
        val expected = intArrayOf(23)
        val expectedFrom = 12
        val expectedTo = 42
        val expectedSize = 39
        val qualifier = "test"
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedNumericArrayGeneratorStub<Int, IntArray>()

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedSize: Int? = null
        var capturedPredicate: Function1<Int?, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenSize, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedSize = givenSize
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(IntArray::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$intArray" to generator),
        )

        // When
        val result: IntArray = fixture.fixture(
            from = expectedFrom,
            to = expectedTo,
            size = expectedSize,
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
        assertEquals(
            actual = capturedSize,
            expected = expectedSize,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @JsName("fn6")
    fun `Given fixture is called with a range it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()
        generator.generateWithRange = { _, _, _, _ ->
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
    @JsName("fn6a")
    fun `Given fixture is called with a range and a predicate it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = intArrayOf(23)
        val generator = RangedNumericArrayGeneratorStub<Int, IntArray>()
        generator.generateWithRange = { _, _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, IntArray>(
                ranges = arrayOf(0..23),
            ) { true }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($intArray).",
        )
    }

    @Test
    @JsName("fn7")
    fun `Given fixture is called with a range and size it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = RangedNumericArrayGeneratorStub<Int, Int>()
        generator.generateWithRange = { _, _, _, _ ->
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
    @JsName("fn8a")
    fun `Given fixture is called with a range and a predicate it fails the corresponding Generator is not a RangedGenerator`() {
        // Given
        val expected = intArrayOf(23)
        val generator = FilterableGeneratorStub<Int, IntArray>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(IntArray::class)

        val fixture = Fixture(random, mapOf(intArray to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, IntArray>(ranges = arrayOf(0..23)) { true }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($intArray).",
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
    @JsName("fn9a")
    fun `Given fixture is called with a range and size and predicate it fails the corresponding Generator is not a RangedGenerator`() {
        // Given
        val expected = intArrayOf(23)
        val generator = FilterableGeneratorStub<Int, IntArray>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(IntArray::class)

        val fixture = Fixture(random, mapOf(intArray to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int, IntArray>(ranges = arrayOf(0..23), size = 23) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($intArray).",
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

        generator.generateWithRangesAndSize = { givenRange, _, _ ->
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
    @JsName("fn10a")
    fun `Given fixture is called with a range and a predicate it returns a Fixture for the derived Type`() {
        // Given
        val expected = intArrayOf(23)
        val expectedRange = 12..42
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedNumericArrayGeneratorStub<Int, IntArray>()

        var capturedRange: Array<out ClosedRange<Int>>? = null
        var capturedPredicate: Function1<Int?, Boolean>? = null

        generator.generateWithRangesAndSize = { givenRange, _, givenPredicate ->
            capturedRange = givenRange
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(IntArray::class)

        val fixture = Fixture(random, mapOf(intArray to generator))

        // When
        val result: IntArray = fixture.fixture(
            ranges = arrayOf(expectedRange),
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

        generator.generateWithRangesAndSize = { givenRange, givenSize, _ ->
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
    @JsName("fn11a")
    fun `Given fixture is called with a range and size and predcate it returns a Fixture for the derived Type`() {
        // Given
        val expected = intArrayOf(23)
        val expectedRange = 12..42
        val expectedSize = 39
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedNumericArrayGeneratorStub<Int, IntArray>()

        var capturedRange: Array<out ClosedRange<Int>>? = null
        var capturedSize: Int? = null
        var capturedPredicate: Function1<Int?, Boolean>? = null

        generator.generateWithRangesAndSize = { givenRange, givenSize, givenPredicate ->
            capturedRange = givenRange
            capturedSize = givenSize
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(IntArray::class)

        val fixture = Fixture(random, mapOf(intArray to generator))

        // When
        val result: IntArray = fixture.fixture(
            expectedRange,
            size = expectedSize,
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertTrue {
            capturedRange.contentEquals(arrayOf(expectedRange))
        }
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
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

        generator.generateWithRangesAndSize = { givenRange, _, _ ->
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
    @JsName("fn12a")
    fun `Given fixture is called with a range and a predicate it returns a Fixture while respecting nullability`() {
        // Given
        val expected = intArrayOf(23)
        val expectedRange = 12..42
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedNumericArrayGeneratorStub<Int, IntArray>()

        var capturedRange: Array<out ClosedRange<Int>>? = null
        var capturedPredicate: Function1<Int?, Boolean>? = null

        generator.generateWithRangesAndSize = { givenRange, _, givenPredicate ->
            capturedRange = givenRange
            capturedPredicate = givenPredicate

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(IntArray::class)

        val fixture = Fixture(random, mapOf(intArray to generator))

        // When
        val result: IntArray? = fixture.fixture(
            ranges = arrayOf(expectedRange),
            predicate = expectedPredicate,
        )

        // Then
        assertNull(result)
        assertNull(capturedRange)
        assertNull(capturedPredicate)
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

        generator.generateWithRangesAndSize = { givenRange, givenSize, _ ->
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

        generator.generateWithRangesAndSize = { givenRange, _, _ ->
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
    @JsName("fn14a")
    fun `Given fixture is called with a range and a qualifier and a predicate it returns a Fixture for the derived Type`() {
        // Given
        val expected = intArrayOf(23)
        val expectedRange = 12..42
        val qualifier = "test"
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val generator = RangedNumericArrayGeneratorStub<Int, IntArray>()

        var capturedRange: Array<out ClosedRange<Int>>? = null
        var capturedPredicate: Function1<Int?, Boolean>? = null

        generator.generateWithRangesAndSize = { givenRange, _, givenPredicate ->
            capturedRange = givenRange
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(IntArray::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$intArray" to generator),
        )

        // When
        val result: IntArray = fixture.fixture(
            ranges = arrayOf(expectedRange),
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

        generator.generateWithRangesAndSize = { givenRange, givenSize, _ ->
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
