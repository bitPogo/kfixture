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
    @JsName("fn0")
    fun `It fulfils SignedNumericArrayGenerator`() {
        val generator: Any = LongArrayGenerator(random, SignedNumberGeneratorStub())

        assertTrue(generator is PublicApi.SignedNumericArrayGenerator<*, *>)
    }

    @Test
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
    @JsName("fn3")
    fun `Given generate is called with boundaries it returns a LongArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toLong()
        val expectedMax = 42.toLong()

        var capturedMin: Long? = null
        var capturedMax: Long? = null

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
            capturedMin = givenMin
            capturedMax = givenMax

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
            expected = expectedMin,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax,
        )
        assertTrue(
            expected.toLongArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn3a")
    fun `Given generate is called with boundaries and a predicate it returns a LongArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toLong()
        val expectedMax = 42.toLong()
        val expectedPredicate: Function1<Long?, Boolean> = { true }

        var capturedMin: Long? = null
        var capturedMax: Long? = null
        var capturedPredicate: Function<Boolean>? = null

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

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin = givenMin
            capturedMax = givenMax
            capturedPredicate = givenPredicate

            consumableItem.removeFirst()
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax, predicate = expectedPredicate)

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
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
        assertTrue(
            expected.toLongArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn4")
    fun `Given generate is called with boundaries it returns a LongArray with a given Size`() {
        // Given
        val size = 3
        val expectedMin = 0.toLong()
        val expectedMax = 42.toLong()

        var capturedMin: Long? = null
        var capturedMax: Long? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        val expected = listOf(
            23.toLong(),
            7.toLong(),
            39.toLong(),
        )
        val consumableItem = expected.toSharedMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin = givenMin
            capturedMax = givenMax

            consumableItem.removeFirst()
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
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
            expected.toLongArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn4a")
    fun `Given generate is called with boundaries and a size and a predicate it returns a LongArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toLong()
        val expectedMax = 42.toLong()
        val expectedPredicate: Function1<Long?, Boolean> = { true }

        var capturedMin: Long? = null
        var capturedMax: Long? = null
        var capturedPredicate: Function<Boolean>? = null
        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        val expected = listOf(
            23.toLong(),
            7.toLong(),
            39.toLong(),
        )
        val consumableItem = expected.toSharedMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin = givenMin
            capturedMax = givenMax
            capturedPredicate = givenPredicate

            consumableItem.removeFirst()
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
            size = size,
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = capturedMin,
            expected = expectedMin,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
        assertTrue(
            expected.toLongArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn5")
    fun `Given generate is called with ranges it returns a LongArray`() {
        // Given
        val expectedMin1 = 0.toLong()
        val expectedMax1 = 42.toLong()
        val expectedMin2 = 3.toLong()
        val expectedMax2 = 41.toLong()

        val capturedMin: MutableList<Long> = sharedMutableListOf()
        val capturedMax: MutableList<Long> = sharedMutableListOf()

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
            capturedMin.add(givenMin)
            capturedMax.add(givenMax)

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
            expected.toLongArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn5a")
    fun `Given generate is called with ranges and a predicate it returns a LongArray`() {
        // Given
        val expectedMin1 = 0.toLong()
        val expectedMax1 = 42.toLong()
        val expectedMin2 = 3.toLong()
        val expectedMax2 = 41.toLong()
        val expectedPredicate: Function1<Long?, Boolean> = { true }

        val capturedMin: MutableList<Long> = sharedMutableListOf()
        val capturedMax: MutableList<Long> = sharedMutableListOf()
        val capturedPredicate: MutableList<Function<Boolean>> = sharedMutableListOf()

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

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin.add(givenMin)
            capturedMax.add(givenMax)
            capturedPredicate.add(givenPredicate)

            consumableItem.removeFirst()
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            LongRange(expectedMin1, expectedMax1),
            LongRange(expectedMin2, expectedMax2),
            predicate = expectedPredicate,
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
        assertSame(
            actual = capturedPredicate[0],
            expected = expectedPredicate,
        )
        assertSame(
            actual = capturedPredicate[1],
            expected = expectedPredicate,
        )

        assertTrue(
            expected.toLongArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn6")
    fun `Given generate is called with ranges it returns a LongArray with a given Size`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toLong()
        val expectedMax1 = 42.toLong()
        val expectedMin2 = 3.toLong()
        val expectedMax2 = 41.toLong()

        val capturedMin: MutableList<Long> = sharedMutableListOf()
        val capturedMax: MutableList<Long> = sharedMutableListOf()

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
            capturedMin.add(givenMin)
            capturedMax.add(givenMax)

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
            expected.toLongArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn6a")
    fun `Given generate is called with ranges and a size and a predicate it returns a LongArray`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toLong()
        val expectedMax1 = 42.toLong()
        val expectedMin2 = 3.toLong()
        val expectedMax2 = 41.toLong()
        val expectedPredicate: Function1<Long?, Boolean> = { true }

        val capturedMin: MutableList<Long> = sharedMutableListOf()
        val capturedMax: MutableList<Long> = sharedMutableListOf()
        val capturedPredicate: MutableList<Function<Boolean>> = sharedMutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Long, Long>()

        val expected = listOf(
            23L,
            7L,
            39L,
        )
        val ranges = sharedMutableListOf(1, 0, 1)

        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin.add(givenMin)
            capturedMax.add(givenMax)
            capturedPredicate.add(givenPredicate)

            consumableItem.removeFirst()
        }

        // When
        val generator = LongArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            LongRange(expectedMin1, expectedMax1),
            LongRange(expectedMin2, expectedMax2),
            size = expectedSize,
            predicate = expectedPredicate,
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
        assertSame(
            actual = capturedPredicate[0],
            expected = expectedPredicate,
        )
        assertSame(
            actual = capturedPredicate[1],
            expected = expectedPredicate,
        )

        assertTrue(
            expected.toLongArray().contentEquals(result),
        )
    }

    @Test
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
