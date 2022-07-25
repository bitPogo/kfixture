/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.collections.sharedMutableListOf
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
import tech.antibytes.kfixture.mock.RangedGeneratorStub

class CharArrayGeneratorSpec {
    private val random = RandomStub()
    private val range: AtomicRef<Pair<Int, Int>?> = atomic(null)

    @AfterTest
    fun tearDown() {
        random.clear()
        range.getAndSet(null)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils RangedArrayGenerator`() {
        val generator: Any = CharArrayGenerator(random, RangedGeneratorStub())

        assertTrue(generator is PublicApi.RangedArrayGenerator<*, *>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a CharArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toChar()
        val expected = CharArray(size) { expectedValue }
        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        auxiliaryGenerator.generate = { expectedValue }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate()

        // Then
        assertEquals(
            actual = Pair(1, 10),
            expected = range.value,
        )
        assertTrue(
            expected.contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate is called with a size it returns a CharArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23.toChar()
        val expected = CharArray(size) { expectedValue }
        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        auxiliaryGenerator.generate = { expectedValue }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(size)

        // Then
        assertEquals(
            actual = result.size,
            expected = size,
        )
        assertTrue(
            expected.contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given generate is called with boundaries it returns a CharArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toChar()
        val expectedMax = 42.toChar()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        val expected = listOf(
            23.toChar(),
            7.toChar(),
            39.toChar(),
        )
        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax ->
            capturedMin = givenMin.code
            capturedMax = givenMax.code

            consumableItem.removeFirst()
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax)

        // Then
        assertEquals(
            actual = Pair(1, 10),
            expected = range.value,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.code,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.code,
        )
        assertTrue(
            expected.toCharArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given generate is called with boundaries it returns a CharArray with a given Size`() {
        // Given
        val size = 3
        val expectedMin = 0.toChar()
        val expectedMax = 42.toChar()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        val expected = listOf(
            23.toChar(),
            7.toChar(),
            39.toChar(),
        )
        val consumableItem = expected.toSharedMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax ->
            capturedMin = givenMin.code
            capturedMax = givenMax.code

            consumableItem.removeFirst()
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax, size = size)

        // Then
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.code,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.code,
        )
        assertTrue(
            expected.toCharArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given generate is called with ranges it returns a CharArray`() {
        // Given
        val expectedMin1 = 0.toChar()
        val expectedMax1 = 42.toChar()
        val expectedMin2 = 3.toChar()
        val expectedMax2 = 41.toChar()

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        val expected = listOf(
            23.toChar(),
            7.toChar(),
            39.toChar(),
        )
        val ranges = sharedMutableListOf(3, 1, 0, 1)

        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax ->
            capturedMin.add(givenMin.code)
            capturedMax.add(givenMax.code)

            consumableItem.removeFirst()
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            CharRange(expectedMin1, expectedMax1),
            CharRange(expectedMin2, expectedMax2),
        )

        // Then
        assertEquals(
            actual = range.value,
            expected = Pair(1, 2),
        )
        assertTrue(
            actual = expectedMin1.code in capturedMin,
        )
        assertTrue(
            actual = expectedMin2.code in capturedMin,
        )
        assertTrue(
            actual = expectedMax1.code in capturedMax,
        )
        assertTrue(
            actual = expectedMax2.code in capturedMax,
        )

        assertTrue(
            expected.toCharArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given generate is called with ranges it returns a CharArray with a given Size`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toChar()
        val expectedMax1 = 42.toChar()
        val expectedMin2 = 3.toChar()
        val expectedMax2 = 41.toChar()

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        val expected = listOf(
            23.toChar(),
            7.toChar(),
            39.toChar(),
        )
        val ranges = sharedMutableListOf(1, 0, 1)

        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax ->
            capturedMin.add(givenMin.code)
            capturedMax.add(givenMax.code)

            consumableItem.removeFirst()
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            CharRange(expectedMin1, expectedMax1),
            CharRange(expectedMin2, expectedMax2),
            size = expectedSize,
        )

        // Then
        assertEquals(
            actual = range.value,
            expected = Pair(1, 2),
        )
        assertTrue(
            actual = expectedMin1.code in capturedMin,
        )
        assertTrue(
            actual = expectedMin2.code in capturedMin,
        )
        assertTrue(
            actual = expectedMax1.code in capturedMax,
        )
        assertTrue(
            actual = expectedMax2.code in capturedMax,
        )

        assertTrue(
            expected.toCharArray().contentEquals(result),
        )
    }
}

private data class CharRange(
    override val start: Char,
    override val endInclusive: Char,
) : ClosedRange<Char>
