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
    fun `It fulfils RangedGenerator`() {
        val generator: Any = FloatGenerator(random)

        assertTrue(generator is PublicApi.RangedGenerator<*, *>)
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
        val expectedMax = 1000.23f
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
    @JsName("fn5a")
    fun `Given generate is called with boundaries it returns a Float while respecting the lower bound`() {
        // Given
        val expected = -107
        val expectedMin = -42.23f
        val expectedMax = 0f
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
            expected = expected.toFloat(),
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5b")
    fun `Given generate is called with boundaries it returns a Float while respecting the lower bound exactly`() {
        // Given
        val expected = -107
        val expectedMin = -107.23f
        val expectedMax = 0f
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
            expected = expected + (-expectedFloat),
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5c")
    fun `Given generate is called with boundaries it returns a Float while respecting the upper bound`() {
        // Given
        val expected = 23
        val expectedMin = -42.23f
        val expectedMax = 0f
        val expectedFloat = 0.0f

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
            expected = 23F,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5d")
    fun `Given generate is called with boundaries it returns a Float while respecting the upper bound exactly`() {
        // Given
        val expected = 0
        val expectedMin = 0f
        val expectedMax = 0.23f
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
            expected = 0.23f,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given generate is called with boundaries and a predicates it returns a Float`() {
        // Given
        val expected = 107
        val expectedMin = 0.1f
        val expectedMax = 1000.23f
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
        val expected = 123
        val expectedMin = 0.1f
        val expectedMax = 123f

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        random.nextFloat = { 0.999f }

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
            expected = 0,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.toInt() + 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn8")
    fun `Given generate is called with boundaries and a predicate it returns a Float while cutting the float point number`() {
        // Given
        val expected = 123
        val expectedMin = 0.1f
        val expectedMax = 123f

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        random.nextFloat = { 0.999f }

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
            expected = 0,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.toInt() + 1,
        )
    }
}
