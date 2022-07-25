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

class UShortGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils RangedGenerator`() {
        val generator: Any = UShortGenerator(random)

        assertTrue(generator is PublicApi.RangedGenerator<*, *>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a UShort`() {
        // Given
        val expected = 23
        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = UShortGenerator(random)

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = expected.toUShort(),
        )
        assertEquals(
            actual = capturedMin,
            expected = 0,
        )
        assertEquals(
            actual = capturedMax,
            expected = UShort.MAX_VALUE.toInt() + 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate is called with a predicate it returns a UShort`() {
        // Given
        val expected = 23
        val values = mutableListOf(108, 109, expected)
        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            values.removeFirst()
        }

        val generator = UShortGenerator(random)

        // When
        val result = generator.generate { uShort -> uShort == expected.toUShort() }

        // Then
        assertEquals(
            actual = result,
            expected = expected.toUShort(),
        )
        assertEquals(
            actual = capturedMin,
            expected = 0,
        )
        assertEquals(
            actual = capturedMax,
            expected = UShort.MAX_VALUE.toInt() + 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given generate is called with a range it fails since the start is less equal to the end`() {
        // Given
        val expected = IllegalArgumentException()

        random.nextIntRanged = { _, _ -> throw expected }

        val generator = UShortGenerator(random)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(1.toUShort(), 0.toUShort())
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

        val generator = UShortGenerator(random)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(1.toUShort(), 0.toUShort()) { false }
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
    fun `Given generate is called with boundaries it returns a UShort`() {
        // Given
        val expected = 107
        val expectedMin = 0.toUShort()
        val expectedMax = 42.toUShort()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = UShortGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected.toUShort(),
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
    fun `Given generate is called with boundaries and a predicate it returns a UShort`() {
        // Given
        val expected = 107
        val expectedMin = 0.toUShort()
        val expectedMax = 42.toUShort()
        val values = mutableListOf(108, 109, expected)

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            values.removeFirst()
        }

        val generator = UShortGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        ) { uShort -> uShort == expected.toUShort() }

        // Then
        assertEquals(
            actual = result,
            expected = expected.toUShort(),
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
}
