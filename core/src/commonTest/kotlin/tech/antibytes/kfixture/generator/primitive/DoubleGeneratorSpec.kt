/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub

class DoubleGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils SignedNumberGenerator`() {
        val generator: Any = DoubleGenerator(random)

        assertTrue(generator is PublicApi.SignedNumberGenerator<*, *>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a Double`() {
        // Given
        val expected = 23.0
        random.nextDoubleRanged = { _, _ -> expected }

        val generator = DoubleGenerator(random)

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate is called with a predicate it returns a Double`() {
        // Given
        val expected = 23.0
        val doubles = mutableListOf(21.0, 194.0, expected)
        random.nextDoubleRanged = { _, _ -> doubles.removeFirst() }

        val generator = DoubleGenerator(random)

        // When
        val result = generator.generate { double -> double == expected }

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given generate is called with a range it fails since the start is less equal to the end`() {
        // Given
        val expected = IllegalArgumentException()

        random.nextDoubleRanged = { _, _ -> throw expected }

        val generator = DoubleGenerator(random)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(-1.0, 0.0)
        }

        // Then
        assertSame(
            actual = error,
            expected = expected,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given generate is called with a range and a predicate it fails since the start is less equal to the end`() {
        // Given
        val expected = IllegalArgumentException()

        random.nextDoubleRanged = { _, _ -> throw expected }

        val generator = DoubleGenerator(random)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(-1.0, 0.0) { false }
        }

        // Then
        assertSame(
            actual = error,
            expected = expected,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given generate is called with boundaries it returns a Double`() {
        // Given
        val expected = 107.0
        val expectedMin = 0.0
        val expectedMax = 42.0

        var capturedMin: Double? = null
        var capturedMax: Double? = null

        random.nextDoubleRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }
        random.nextBoolean = { false }

        val generator = DoubleGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given generate is called with boundaries and a predicate it returns a Double`() {
        // Given
        val expected = 107.0
        val doubles = mutableListOf(21.0, 194.0, expected)
        val expectedMin = 0.0
        val expectedMax = 42.0

        var capturedMin: Double? = null
        var capturedMax: Double? = null

        random.nextDoubleRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            doubles.removeFirst()
        }
        random.nextBoolean = { false }

        val generator = DoubleGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        ) { double -> double == expected }

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn7")
    fun `Given generate is called with boundaries it returns a Double while filliing up to the limit`() {
        // Given
        val expected = 107.0
        val expectedMin = 0.0
        val expectedMax = 42.0

        var capturedMin: Double? = null
        var capturedMax: Double? = null

        random.nextDoubleRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }
        random.nextBoolean = { true }

        val generator = DoubleGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expectedMax,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn8")
    fun `Given generate is called with boundaries and a predicate it returns a Double while filliing up to the limit`() {
        // Given
        val expected = 107.0
        val doubles = mutableListOf(21.0, 194.0, expected)
        val expectedMin = 0.0
        val expectedMax = 42.0

        var capturedMin: Double? = null
        var capturedMax: Double? = null

        random.nextDoubleRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            doubles.removeFirst()
        }
        random.nextBoolean = { true }

        val generator = DoubleGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        ) { double -> double == expectedMax }

        // Then
        assertEquals(
            actual = result,
            expected = expectedMax,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn9")
    fun `Given generate is called with POSITIVE it returns a Double`() {
        // Given
        val expected = 107.0

        var capturedMin: Double? = null
        var capturedMax: Double? = null

        random.nextDoubleRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = DoubleGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.POSITIVE,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedMin,
            expected = 0.0,
        )
        assertEquals(
            actual = capturedMax,
            expected = Double.MAX_VALUE,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn10")
    fun `Given generate is called with POSITIVE and a predicate it returns a Double`() {
        // Given
        val expected = 107.0
        val doubles = mutableListOf(21.0, 194.0, expected)

        var capturedMin: Double? = null
        var capturedMax: Double? = null

        random.nextDoubleRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            doubles.removeFirst()
        }

        val generator = DoubleGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.POSITIVE,
        ) { double -> double == expected }

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedMin,
            expected = 0.0,
        )
        assertEquals(
            actual = capturedMax,
            expected = Double.MAX_VALUE,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn11")
    fun `Given generate is called with NEGATIVE it returns a Double`() {
        // Given
        val expected = 107.0

        var capturedMin: Double? = null
        var capturedMax: Double? = null

        random.nextDoubleRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }
        random.nextBoolean = { false }

        val generator = DoubleGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.NEGATIVE,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedMin,
            expected = Double.MIN_VALUE,
        )
        assertEquals(
            actual = capturedMax,
            expected = 0.0,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn12")
    fun `Given generate is called with NEGATIVE and a predicate it returns a Double`() {
        // Given
        val expected = 107.0
        val doubles = mutableListOf(21.0, 194.0, expected)

        var capturedMin: Double? = null
        var capturedMax: Double? = null

        random.nextDoubleRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            doubles.removeFirst()
        }
        random.nextBoolean = { false }

        val generator = DoubleGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.NEGATIVE,
        ) { double -> double == expected }

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedMin,
            expected = Double.MIN_VALUE,
        )
        assertEquals(
            actual = capturedMax,
            expected = 0.0,
        )
    }
}
