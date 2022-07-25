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
import tech.antibytes.kfixture.mock.SignedNumberGeneratorStub

class IntArrayGeneratorSpec {
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
    fun `It fulfils Generator`() {
        val generator: Any = IntArrayGenerator(random, SignedNumberGeneratorStub())

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a IntArray`() {
        // Given
        val size = 23
        val expectedValue = 23
        val expected = IntArray(size) { expectedValue }
        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        auxiliaryGenerator.generate = { expectedValue }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with a size it returns a IntArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23
        val expected = IntArray(size) { expectedValue }
        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        auxiliaryGenerator.generate = { expectedValue }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with boundaries it returns a IntArray`() {
        // Given
        val size = 3
        val expectedMin = 0
        val expectedMax = 42

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        val expected = listOf(
            23,
            7,
            39,
        )
        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            consumableItem.removeFirst()
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax)

        // Then
        assertEquals(
            actual = Pair(1, 10),
            expected = range.value,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax,
        )
        assertTrue(
            expected.toIntArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given generate is called with boundaries it returns a IntArray with a given Size`() {
        // Given
        val size = 3
        val expectedMin = 0
        val expectedMax = 42

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        val expected = listOf(
            23,
            7,
            39,
        )
        val consumableItem = expected.toSharedMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            consumableItem.removeFirst()
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax, size = size)

        // Then
        assertEquals(
            actual = capturedMin,
            expected = expectedMin,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax,
        )
        assertTrue(
            expected.toIntArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given generate is called with ranges it returns a IntArray`() {
        // Given
        val expectedMin1 = 0
        val expectedMax1 = 42
        val expectedMin2 = 3
        val expectedMax2 = 41

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        val expected = listOf(
            23,
            7,
            39,
        )
        val ranges = sharedMutableListOf(3, 1, 0, 1)

        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax ->
            capturedMin.add(givenMin)
            capturedMax.add(givenMax)

            consumableItem.removeFirst()
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            IntRange(expectedMin1, expectedMax1),
            IntRange(expectedMin2, expectedMax2),
        )

        // Then
        assertEquals(
            actual = range.value,
            expected = Pair(1, 2),
        )
        assertTrue(
            actual = expectedMin1 in capturedMin,
        )
        assertTrue(
            actual = expectedMin2 in capturedMin,
        )
        assertTrue(
            actual = expectedMax1 in capturedMax,
        )
        assertTrue(
            actual = expectedMax2 in capturedMax,
        )

        assertTrue(
            expected.toIntArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given generate is called with ranges it returns a IntArray with a given Size`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0
        val expectedMax1 = 42
        val expectedMin2 = 3
        val expectedMax2 = 41

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        val expected = listOf(
            23,
            7,
            39,
        )
        val ranges = sharedMutableListOf(1, 0, 1)

        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax ->
            capturedMin.add(givenMin)
            capturedMax.add(givenMax)

            consumableItem.removeFirst()
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            IntRange(expectedMin1, expectedMax1),
            IntRange(expectedMin2, expectedMax2),
            size = expectedSize,
        )

        // Then
        assertEquals(
            actual = range.value,
            expected = Pair(1, 2),
        )
        assertTrue(
            actual = expectedMin1 in capturedMin,
        )
        assertTrue(
            actual = expectedMin2 in capturedMin,
        )
        assertTrue(
            actual = expectedMax1 in capturedMax,
        )
        assertTrue(
            actual = expectedMax2 in capturedMax,
        )

        assertTrue(
            expected.toIntArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn7")
    fun `Given generate is called with a Sign it returns a IntArray`() {
        // Given
        val expectedSign = PublicApi.Sign.NEGATIVE
        val size = 23

        var capturedSign: PublicApi.Sign? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        val expectedValue = 42
        val expected = IntArray(size) { expectedValue }

        auxiliaryGenerator.generateWithSign = { givenSign ->
            capturedSign = givenSign

            expectedValue
        }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(expectedSign)

        // Then
        assertEquals(
            actual = Pair(1, 10),
            expected = range.value,
        )
        assertEquals(
            actual = capturedSign,
            expected = expectedSign,
        )
        assertTrue(
            expected.contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn8")
    fun `Given generate is called with a Sign and Size it returns a IntArray`() {
        // Given
        val expectedSign = PublicApi.Sign.NEGATIVE
        val size = 23

        var capturedSign: PublicApi.Sign? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        val expectedValue = 42
        val expected = IntArray(size) { expectedValue }

        auxiliaryGenerator.generateWithSign = { givenSign ->
            capturedSign = givenSign

            expectedValue
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(expectedSign, size)

        // Then
        assertEquals(
            actual = capturedSign,
            expected = expectedSign,
        )
        assertTrue(
            expected.contentEquals(result),
        )
    }
}