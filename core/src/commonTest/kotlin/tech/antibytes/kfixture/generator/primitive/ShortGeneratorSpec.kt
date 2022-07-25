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

class ShortGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils SignedNumberGenerator`() {
        val generator: Any = ShortGenerator(random)

        assertTrue(generator is PublicApi.SignedNumberGenerator<*, *>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a Short`() {
        // Given
        val expected = 23
        random.nextIntRanged = { _, _ -> expected }

        val generator = ShortGenerator(random)

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = expected.toShort(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate is called with a predicate it returns a Short`() {
        // Given
        val expected = 23
        val shorts = mutableListOf(24, 26, expected)
        random.nextIntRanged = { _, _ -> shorts.removeFirst() }

        val generator = ShortGenerator(random)

        // When
        val result = generator.generate { short -> short == expected.toShort() }

        // Then
        assertEquals(
            actual = result,
            expected = expected.toShort(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given generate is called with a range it fails since the start is less equal to the end`() {
        // Given
        val expected = IllegalArgumentException()

        random.nextIntRanged = { _, _ -> throw expected }

        val generator = ShortGenerator(random)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(-1, 0)
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

        val generator = ShortGenerator(random)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(-1, 0) { false }
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
    fun `Given generate is called with boundaries it returns a Short`() {
        // Given
        val expected = 107
        val expectedMin = 0.toShort()
        val expectedMax = 42.toShort()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = ShortGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected.toShort(),
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.toInt() + 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given generate is called with boundaries and a predicate it returns a Short`() {
        // Given
        val expected = 107
        val expectedMin = 0.toShort()
        val expectedMax = 42.toShort()
        val shorts = mutableListOf(24, 26, expected)

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            shorts.removeFirst()
        }

        val generator = ShortGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        ) { short -> short == expected.toShort() }

        // Then
        assertEquals(
            actual = result,
            expected = expected.toShort(),
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.toInt() + 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn7")
    fun `Given generate is called with POSITIVE it returns a Short`() {
        // Given
        val expected = 107

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = ShortGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.POSITIVE,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected.toShort(),
        )
        assertEquals(
            actual = capturedMin,
            expected = 0,
        )
        assertEquals(
            actual = capturedMax,
            expected = Short.MAX_VALUE + 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn8")
    fun `Given generate is called with a predicate with POSITIVE it returns a Short`() {
        // Given
        val expected = 107
        val shorts = mutableListOf(24, 26, expected)

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            shorts.removeFirst()
        }

        val generator = ShortGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.POSITIVE,
        ) { short -> short == expected.toShort() }

        // Then
        assertEquals(
            actual = result,
            expected = expected.toShort(),
        )
        assertEquals(
            actual = capturedMin,
            expected = 0,
        )
        assertEquals(
            actual = capturedMax,
            expected = Short.MAX_VALUE + 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn9")
    fun `Given generate is called with NEGATIVE it returns a Short`() {
        // Given
        val expected = 107

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = ShortGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.NEGATIVE,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected.toShort(),
        )
        assertEquals(
            actual = capturedMin,
            expected = Short.MIN_VALUE.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn10")
    fun `Given generate is called with NEGATIVE and a predicate it returns a Short`() {
        // Given
        val expected = 107
        val shorts = mutableListOf(24, 26, expected)

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            shorts.removeFirst()
        }

        val generator = ShortGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.NEGATIVE,
        ) { short -> short == expected.toShort() }

        // Then
        assertEquals(
            actual = result,
            expected = expected.toShort(),
        )
        assertEquals(
            actual = capturedMin,
            expected = Short.MIN_VALUE.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = 1,
        )
    }
}
