/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
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

class FloatGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils SignedNumberGenerator`() {
        val generator: Any = FloatGenerator(random)

        assertTrue(generator is PublicApi.SignedNumberGenerator<*, *>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a Float`() {
        // Given
        val expected = 23
        val expectedFloat = 0.23f
        random.nextInt = { expected }
        random.nextFloat = { expectedFloat }

        val generator = FloatGenerator(random)

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = (expected + expectedFloat),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate is called with a predicate it returns a Float`() {
        // Given
        val expected = 23
        val expectedFloat = 0.23f
        val floats = mutableListOf(0.12f, 0.13f, expectedFloat)
        random.nextInt = { expected }
        random.nextFloat = { floats.removeFirst() }

        val generator = FloatGenerator(random)

        // When
        val result = generator.generate { float -> float == expected + expectedFloat }

        // Then
        assertEquals(
            actual = result,
            expected = (expected + expectedFloat),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given generate is called with a range it fails since the start is less equal to the end`() {
        // Given
        val expected = IllegalArgumentException()

        random.nextIntRanged = { _, _ -> throw expected }

        val generator = FloatGenerator(random)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(-1f, 0f)
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

        random.nextIntRanged = { _, _ -> throw expected }

        val generator = FloatGenerator(random)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(-1f, 0f) { false }
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
    fun `Given generate is called with boundaries it returns a Float`() {
        // Given
        val expected = 107
        val expectedMin = 0.1f
        val expectedMax = 42.23f
        val expectedFloat = 0.23f

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }
        random.nextFloat = { expectedFloat }

        val generator = FloatGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected + expectedFloat,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = (expectedMax + 1).toInt(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given generate is called with boundaries and a predicates it returns a Float`() {
        // Given
        val expected = 107
        val expectedMin = 0.1f
        val expectedMax = 42.23f
        val expectedFloat = 0.23f
        val floats = mutableListOf(0.12f, 0.13f, expectedFloat)

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }
        random.nextFloat = { floats.removeFirst() }

        val generator = FloatGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        ) { float -> float == expected + expectedFloat }

        // Then
        assertEquals(
            actual = result,
            expected = expected + expectedFloat,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = (expectedMax + 1).toInt(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn7")
    fun `Given generate is called with boundaries it returns a Float while cutting the float point number`() {
        // Given
        val expected = Float.MAX_VALUE.toInt()
        val expectedMin = 0.1f
        val expectedMax = Float.MAX_VALUE

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected - 1
        }

        val generator = FloatGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected.toFloat(),
        )
        assertEquals(
            actual = capturedMin,
            expected = -1,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.toInt(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn8")
    fun `Given generate is called with boundaries and a predicate it returns a Float while cutting the float point number`() {
        // Given
        val expected = Float.MAX_VALUE.toInt()
        val expectedMin = 0.1f
        val expectedMax = Float.MAX_VALUE

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected - 1
        }

        val generator = FloatGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        ) { float -> float == expected.toFloat() }

        // Then
        assertEquals(
            actual = result,
            expected = expected.toFloat(),
        )
        assertEquals(
            actual = capturedMin,
            expected = -1,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.toInt(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn9")
    fun `Given generate is called with POSITIVE it returns a Float`() {
        // Given
        val expected = 107
        val expectedFloat = 0.23f

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected - 1
        }
        random.nextFloat = { expectedFloat }

        val generator = FloatGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.POSITIVE,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected + expectedFloat,
        )
        assertEquals(
            actual = capturedMin,
            expected = -1,
        )
        assertEquals(
            actual = capturedMax,
            expected = Float.MAX_VALUE.toInt(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn10")
    fun `Given generate is called with POSITIVE and a predicate it returns a Float`() {
        // Given
        val expected = 107
        val expectedFloat = 0.23f
        val floats = mutableListOf(0.12f, 0.13f, expectedFloat)

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected - 1
        }
        random.nextFloat = { floats.removeFirst() }

        val generator = FloatGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.POSITIVE,
        ) { float -> float == expected + expectedFloat }

        // Then
        assertEquals(
            actual = result,
            expected = expected + expectedFloat,
        )
        assertEquals(
            actual = capturedMin,
            expected = -1,
        )
        assertEquals(
            actual = capturedMax,
            expected = Float.MAX_VALUE.toInt(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn11")
    fun `Given generate is called with NEGATIVE it returns a Float`() {
        // Given
        val expected = 107
        val expectedFloat = 0.23f

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }
        random.nextFloat = { expectedFloat }

        val generator = FloatGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.NEGATIVE,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected + expectedFloat,
        )
        assertEquals(
            actual = capturedMin,
            expected = Float.MIN_VALUE.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn12")
    fun `Given generate is called with NEGATIVE and a predicate it returns a Float`() {
        // Given
        val expected = 107
        val expectedFloat = 0.23f
        val floats = mutableListOf(0.12f, 0.13f, expectedFloat)

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }
        random.nextFloat = { floats.removeFirst() }

        val generator = FloatGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.NEGATIVE,
        ) { float -> float == expectedFloat + expected }

        // Then
        assertEquals(
            actual = result,
            expected = expected + expectedFloat,
        )
        assertEquals(
            actual = capturedMin,
            expected = Float.MIN_VALUE.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = 1,
        )
    }
}
