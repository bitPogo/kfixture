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

class LongGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils SignedNumberGenerator`() {
        val generator: Any = LongGenerator(random)

        assertTrue(generator is PublicApi.SignedNumberGenerator<*, *>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a Long`() {
        // Given
        val expected = 23L
        random.nextLong = { expected }

        val generator = LongGenerator(random)

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
    fun `Given generate is called with a range it fails since the start is less equal to the end`() {
        // Given
        val expected = IllegalArgumentException()

        random.nextLongRanged = { _, _ -> throw expected }

        val generator = LongGenerator(random)

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
    @JsName("fn3")
    fun `Given generate is called with boundaries it returns a Long`() {
        // Given
        val expected = 107L
        val expectedMin = 0L
        val expectedMax = 42L

        var capturedMin: Long? = null
        var capturedMax: Long? = null

        random.nextLongRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = LongGenerator(random)

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
            expected = expectedMax + 1,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given generate is called with POSITIVE it returns a Long`() {
        // Given
        val expected = 107L

        var capturedMin: Long? = null
        var capturedMax: Long? = null

        random.nextLongRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = LongGenerator(random)

        // When
        val result = generator.generate(
            PublicApi.Sign.POSITIVE,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected + 1,
        )
        assertEquals(
            actual = capturedMin,
            expected = -1,
        )
        assertEquals(
            actual = capturedMax,
            expected = Long.MAX_VALUE,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given generate is called with NEGATIVE it returns a Long`() {
        // Given
        val expected = 107L

        var capturedMin: Long? = null
        var capturedMax: Long? = null

        random.nextLongRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = LongGenerator(random)

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
            expected = Long.MIN_VALUE,
        )
        assertEquals(
            actual = capturedMax,
            expected = 1,
        )
    }
}
