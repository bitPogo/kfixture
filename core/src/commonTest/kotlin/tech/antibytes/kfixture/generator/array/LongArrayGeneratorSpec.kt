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
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.SignedNumberGeneratorStub

class LongArrayGeneratorSpec {
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
    fun `It fulfils SignedNumericArrayGenerator`() {
        val generator: Any = LongArrayGenerator(random, SignedNumberGeneratorStub())

        assertTrue(generator is PublicApi.SignedNumericArrayGenerator<*, *>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a LongArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toLong()
        val expected = LongArray(size) { expectedValue }
        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        auxiliaryGenerator.generate = { expectedValue }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
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
    @JsName("fn1a")
    fun `Given generate is called and a predicate it returns a LongArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toLong()
        val expected = LongArray(size) { expectedValue }
        val expectedPredicate: Function1<Long?, Boolean> = { true }
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(expectedPredicate)

        // Then
        assertEquals(
            actual = Pair(1, 10),
            expected = range.value,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
        assertTrue(
            expected.contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate is called with a size it returns a LongArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23.toLong()
        val expected = LongArray(size) { expectedValue }
        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        auxiliaryGenerator.generate = { expectedValue }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
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
    @JsName("fn2a")
    fun `Given generate is called with a size and a predicate it returns a LongArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23.toLong()
        val expected = LongArray(size) { expectedValue }
        val expectedPredicate: Function1<Long?, Boolean> = { true }
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(size, expectedPredicate)

        // Then
        assertEquals(
            actual = result.size,
            expected = size,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
        assertTrue(
            expected.contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given generate is called with boundaries it returns a LongArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toLong()
        val expectedMax = 42.toLong()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        val expected = listOf(
            23.toLong(),
            7.toLong(),
            39.toLong(),
        )
        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin = givenMin.toInt()
            capturedMax = givenMax.toInt()

            consumableItem.removeFirst()
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax)

        // Then
        assertEquals(
            actual = Pair(1, 10),
            expected = range.value,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.toInt(),
        )
        assertTrue(
            expected.toLongArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given generate is called with boundaries it returns a LongArray with a given Size`() {
        // Given
        val size = 3
        val expectedMin = 0.toLong()
        val expectedMax = 42.toLong()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        val expected = listOf(
            23.toLong(),
            7.toLong(),
            39.toLong(),
        )
        val consumableItem = expected.toSharedMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin = givenMin.toInt()
            capturedMax = givenMax.toInt()

            consumableItem.removeFirst()
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax, size = size)

        // Then
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.toInt(),
        )
        assertTrue(
            expected.toLongArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given generate is called with ranges it returns a LongArray`() {
        // Given
        val expectedMin1 = 0.toLong()
        val expectedMax1 = 42.toLong()
        val expectedMin2 = 3.toLong()
        val expectedMax2 = 41.toLong()

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        val expected = listOf(
            23.toLong(),
            7.toLong(),
            39.toLong(),
        )
        val ranges = sharedMutableListOf(3, 1, 0, 1)

        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin.add(givenMin.toInt())
            capturedMax.add(givenMax.toInt())

            consumableItem.removeFirst()
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            LongRange(expectedMin1, expectedMax1),
            LongRange(expectedMin2, expectedMax2),
        )

        // Then
        assertEquals(
            actual = range.value,
            expected = Pair(1, 2),
        )
        assertTrue(
            actual = expectedMin1.toInt() in capturedMin,
        )
        assertTrue(
            actual = expectedMin2.toInt() in capturedMin,
        )
        assertTrue(
            actual = expectedMax1.toInt() in capturedMax,
        )
        assertTrue(
            actual = expectedMax2.toInt() in capturedMax,
        )

        assertTrue(
            expected.toLongArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given generate is called with ranges it returns a LongArray with a given Size`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toLong()
        val expectedMax1 = 42.toLong()
        val expectedMin2 = 3.toLong()
        val expectedMax2 = 41.toLong()

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        val expected = listOf(
            23.toLong(),
            7.toLong(),
            39.toLong(),
        )
        val ranges = sharedMutableListOf(1, 0, 1)

        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin.add(givenMin.toInt())
            capturedMax.add(givenMax.toInt())

            consumableItem.removeFirst()
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            LongRange(expectedMin1, expectedMax1),
            LongRange(expectedMin2, expectedMax2),
            size = expectedSize,
        )

        // Then
        assertEquals(
            actual = range.value,
            expected = Pair(1, 2),
        )
        assertTrue(
            actual = expectedMin1.toInt() in capturedMin,
        )
        assertTrue(
            actual = expectedMin2.toInt() in capturedMin,
        )
        assertTrue(
            actual = expectedMax1.toInt() in capturedMax,
        )
        assertTrue(
            actual = expectedMax2.toInt() in capturedMax,
        )

        assertTrue(
            expected.toLongArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn7")
    fun `Given generate is called with a Sign it returns a LongArray`() {
        // Given
        val expectedSign = PublicApi.Sign.NEGATIVE
        val size = 23

        var capturedSign: PublicApi.Sign? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        val expectedValue = 42.toLong()
        val expected = LongArray(size) { expectedValue }

        auxiliaryGenerator.generateWithSign = { givenSign, _ ->
            capturedSign = givenSign

            expectedValue
        }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with a Sign and Size it returns a LongArray`() {
        // Given
        val expectedSign = PublicApi.Sign.NEGATIVE
        val size = 23

        var capturedSign: PublicApi.Sign? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        val expectedValue = 42.toLong()
        val expected = LongArray(size) { expectedValue }

        auxiliaryGenerator.generateWithSign = { givenSign, _ ->
            capturedSign = givenSign

            expectedValue
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
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

private data class LongRange(
    override val start: Long,
    override val endInclusive: Long,
) : ClosedRange<Long>
