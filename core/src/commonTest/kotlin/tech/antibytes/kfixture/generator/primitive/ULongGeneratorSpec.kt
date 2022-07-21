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
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub

class ULongGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils RangedGenerator`() {
        val generator: Any = ULongGenerator(random)

        assertTrue(generator is PublicApi.RangedGenerator<*>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a ULong`() {
        // Given
        val expected = 23L

        random.nextLong = { expected }

        val generator = ULongGenerator(random)

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = expected.toULong(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate is called with a range it fails since the start is less equal to the end`() {
        // Given
        val generator = ULongGenerator(random)

        // Then
        assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(1.toULong(), 0.toULong())
        }
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given generate is called with boundaries it returns a ULong`() {
        // Given
        val expected = 107L
        val expectedMin = 0.toULong()
        val expectedMax = 42.toULong()

        var capturedMin: Long? = null
        var capturedMax: Long? = null

        random.nextLongRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = ULongGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        )

        // Then
        assertEquals(
            actual = result,
            expected = (expected xor Long.MIN_VALUE).toULong(),
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toLong() xor Long.MIN_VALUE,
        )
        assertEquals(
            actual = capturedMax,
            expected = ((expectedMax.toLong() xor Long.MIN_VALUE) + 1),
        )
    }
}
