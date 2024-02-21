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

class UByteGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils RangedGenerator`() {
        val generator: Any = UByteGenerator(random)

        assertTrue(generator is PublicApi.RangedGenerator<*, *>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a UByte`() {
        // Given
        val expected = 23
        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = UByteGenerator(random)

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = expected.toUByte(),
        )
        assertEquals(
            actual = capturedMin,
            expected = 0,
        )
        assertEquals(
            actual = capturedMax,
            expected = UByte.MAX_VALUE.toInt() + 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate with a predicate is called it returns a UByte`() {
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

        val generator = UByteGenerator(random)

        // When
        val result = generator.generate { uByte -> uByte == expected.toUByte() }

        // Then
        assertEquals(
            actual = result,
            expected = expected.toUByte(),
        )
        assertEquals(
            actual = capturedMin,
            expected = 0,
        )
        assertEquals(
            actual = capturedMax,
            expected = UByte.MAX_VALUE.toInt() + 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given generate is called with a range it fails since the start is less equal to the end`() {
        // Given
        val expected = IllegalArgumentException()

        random.nextIntRanged = { _, _ -> throw expected }

        val generator = UByteGenerator(random)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(1.toUByte(), 0.toUByte())
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

        val generator = UByteGenerator(random)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(1.toUByte(), 0.toUByte()) { false }
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
    fun `Given generate is called with boundaries it returns a UByte`() {
        // Given
        val expected = 107
        val expectedMin = 0.toUByte()
        val expectedMax = 42.toUByte()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = UByteGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected.toUByte(),
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
    fun `Given generate is called with boundaries and a predicate it returns a UByte`() {
        // Given
        val expected = 107
        val expectedMin = 0.toUByte()
        val expectedMax = 42.toUByte()

        val values = mutableListOf(108, 109, expected)

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            values.removeFirst()
        }

        val generator = UByteGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        ) { uByte -> uByte == expected.toUByte() }

        // Then
        assertEquals(
            actual = result,
            expected = expected.toUByte(),
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
