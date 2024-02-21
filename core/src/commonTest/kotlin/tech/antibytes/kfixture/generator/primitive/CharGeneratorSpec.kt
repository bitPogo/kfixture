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
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub

class CharGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils RangedGenerator`() {
        val generator: Any = CharGenerator(random)

        assertTrue(generator is PublicApi.RangedGenerator<*, *>)
    }

    @Test
    @JsName("fn1")
    fun `Given generate is called it returns a Char`() {
        // Given
        val expected = 100
        var range: Pair<Int, Int>? = null

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            expected
        }

        val generator = CharGenerator(random)

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = range,
            expected = Pair(32, 127),
        )
        assertEquals(
            actual = result,
            expected = expected.toChar(),
        )
    }

    @Test
    @JsName("fn2")
    fun `Given generate is called with a predicate it returns a Char`() {
        // Given
        val expected = 100
        val chars = mutableListOf(42, 78, expected)
        var range: Pair<Int, Int>? = null

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)

            chars.removeFirst()
        }

        val generator = CharGenerator(random)

        // When
        val result = generator.generate { char -> char == expected.toChar() }

        // Then
        assertEquals(
            actual = range,
            expected = Pair(32, 127),
        )
        assertEquals(
            actual = result,
            expected = expected.toChar(),
        )
    }

    @Test
    @JsName("fn3")
    fun `Given generate is called it returns a Char in given boundaries`() {
        // Given
        val expectedMin = 102.toChar()
        val expectedMax = 189.toChar()
        val expected = 100
        var range: Pair<Int, Int>? = null

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            expected
        }

        val generator = CharGenerator(random)

        // When
        val result = generator.generate(expectedMin, expectedMax)

        // Then
        assertEquals(
            actual = range,
            expected = Pair(expectedMin.code, expectedMax.code + 1),
        )
        assertEquals(
            actual = result,
            expected = expected.toChar(),
        )
    }

    @Test
    @JsName("fn4")
    fun `Given generate is called with a predicate it returns a Char in given boundaries`() {
        // Given
        val expectedMin = 102.toChar()
        val expectedMax = 189.toChar()
        val expected = 100
        val chars = mutableListOf(42, 78, expected)
        var range: Pair<Int, Int>? = null

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)

            chars.removeFirst()
        }

        val generator = CharGenerator(random)

        // When
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
        ) { char -> char == expected.toChar() }

        // Then
        assertEquals(
            actual = range,
            expected = Pair(expectedMin.code, expectedMax.code + 1),
        )
        assertEquals(
            actual = result,
            expected = expected.toChar(),
        )
    }
}
