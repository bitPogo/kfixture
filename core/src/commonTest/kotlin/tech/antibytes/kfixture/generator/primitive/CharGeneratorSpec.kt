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
import kotlin.test.assertTrue
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub

class CharGeneratorSpec {
    private val random = RandomStub()
    private val range: AtomicRef<Pair<Int, Int>?> = atomic(null)

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils RangedGenerator`() {
        val generator: Any = CharGenerator(random)

        assertTrue(generator is PublicApi.RangedGenerator<*, *>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a Char`() {
        // Given
        val expected = 100

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            expected
        }

        val generator = CharGenerator(random)

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = range.value,
            expected = Pair(32, 126),
        )
        assertEquals(
            actual = result,
            expected = expected.toChar(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate is called with a predicate it returns a Char`() {
        // Given
        val expected = 100
        val chars = mutableListOf(42, 78, expected)

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }

            chars.removeFirst()
        }

        val generator = CharGenerator(random)

        // When
        val result = generator.generate { char -> char == expected.toChar() }

        // Then
        assertEquals(
            actual = range.value,
            expected = Pair(32, 126),
        )
        assertEquals(
            actual = result,
            expected = expected.toChar(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given generate is called it returns a Char in given boundaries`() {
        // Given
        val expectedMin = 102.toChar()
        val expectedMax = 189.toChar()
        val expected = 100

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            expected
        }

        val generator = CharGenerator(random)

        // When
        val result = generator.generate(expectedMin, expectedMax)

        // Then
        assertEquals(
            actual = range.value,
            expected = Pair(expectedMin.code, expectedMax.code),
        )
        assertEquals(
            actual = result,
            expected = expected.toChar(),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given generate is called with a predicate it returns a Char in given boundaries`() {
        // Given
        val expectedMin = 102.toChar()
        val expectedMax = 189.toChar()
        val expected = 100
        val chars = mutableListOf(42, 78, expected)

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }

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
            actual = range.value,
            expected = Pair(expectedMin.code, expectedMax.code),
        )
        assertEquals(
            actual = result,
            expected = expected.toChar(),
        )
    }
}
