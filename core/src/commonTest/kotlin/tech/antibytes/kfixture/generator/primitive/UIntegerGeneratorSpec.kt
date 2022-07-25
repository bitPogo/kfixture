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

class UIntegerGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils RangedGenerator`() {
        val generator: Any = UIntegerGenerator(random)

        assertTrue(generator is PublicApi.RangedGenerator<*, *>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a UInt`() {
        // Given
        val expected = 23

        random.nextInt = { expected }

        val generator = UIntegerGenerator(random)

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = expected.toUInt(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate is called with a predicate it returns a UInt`() {
        // Given
        val expected = 23
        val ints = mutableListOf(12, 13, expected)

        random.nextInt = { ints.removeFirst() }

        val generator = UIntegerGenerator(random)

        // When
        val result = generator.generate { int -> int == expected.toUInt() }

        // Then
        assertEquals(
            actual = result,
            expected = expected.toUInt(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given generate is called with a range it fails since the start is less equal to the end`() {
        // Given
        val generator = UIntegerGenerator(random)

        // Then
        assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(1.toUInt(), 0.toUInt())
        }
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given generate is called with a range and a predicate it fails since the start is less equal to the end`() {
        // Given
        val generator = UIntegerGenerator(random)

        // Then
        assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(1.toUInt(), 0.toUInt()) { false }
        }
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given generate is called with boundaries it returns a UInt`() {
        // Given
        val expected = 107
        val expectedMin = 0.toUInt()
        val expectedMax = 42.toUInt()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            expected
        }

        val generator = UIntegerGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        )

        // Then
        assertEquals(
            actual = result,
            expected = (expected xor Int.MIN_VALUE).toUInt(),
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt() xor Int.MIN_VALUE,
        )
        assertEquals(
            actual = capturedMax,
            expected = ((expectedMax.toInt() xor Int.MIN_VALUE) + 1),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given generate is called with boundaries and a predicate it returns a UInt`() {
        // Given
        val expected = 107
        val ints = mutableListOf(12, 13, expected)
        val expectedMin = 0.toUInt()
        val expectedMax = 42.toUInt()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            ints.removeFirst()
        }

        val generator = UIntegerGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        ) { int -> int == (expected xor Int.MIN_VALUE).toUInt() }

        // Then
        assertEquals(
            actual = result,
            expected = (expected xor Int.MIN_VALUE).toUInt(),
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt() xor Int.MIN_VALUE,
        )
        assertEquals(
            actual = capturedMax,
            expected = ((expectedMax.toInt() xor Int.MIN_VALUE) + 1),
        )
    }
}
