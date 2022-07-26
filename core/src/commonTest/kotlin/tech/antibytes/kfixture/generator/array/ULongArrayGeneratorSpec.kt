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
import tech.antibytes.kfixture.mock.RangedGeneratorStub

class ULongArrayGeneratorSpec {
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
        val generator: Any = ULongArrayGenerator(random, RangedGeneratorStub())

        assertTrue(generator is PublicApi.RangedArrayGenerator<*, *>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a ULongArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toULong()
        val expected = ULongArray(size) { expectedValue }
        val auxiliaryGenerator = RangedGeneratorStub<ULong, ULong>()

        auxiliaryGenerator.generate = { expectedValue }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = ULongArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called and a predicate it returns a ULongArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toULong()
        val expected = ULongArray(size) { expectedValue }
        val expectedPredicate: Function1<ULong?, Boolean> = { true }
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = RangedGeneratorStub<ULong, ULong>()

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = ULongArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with a size it returns a ULongArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23.toULong()
        val expected = ULongArray(size) { expectedValue }
        val auxiliaryGenerator = RangedGeneratorStub<ULong, ULong>()

        auxiliaryGenerator.generate = { expectedValue }

        // When
        val generator = ULongArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with a size and a predicate it returns a ULongArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23.toULong()
        val expected = ULongArray(size) { expectedValue }
        val expectedPredicate: Function1<ULong?, Boolean> = { true }
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = RangedGeneratorStub<ULong, ULong>()

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }

        // When
        val generator = ULongArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with boundaries it returns a ULongArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toULong()
        val expectedMax = 42.toULong()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = RangedGeneratorStub<ULong, ULong>()

        val expected = listOf(
            23.toULong(),
            7.toULong(),
            39.toULong(),
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
        val generator = ULongArrayGenerator(random, auxiliaryGenerator)
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
            expected.toULongArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given generate is called with boundaries it returns a ULongArray with a given Size`() {
        // Given
        val size = 3
        val expectedMin = 0.toULong()
        val expectedMax = 42.toULong()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = RangedGeneratorStub<ULong, ULong>()

        val expected = listOf(
            23.toULong(),
            7.toULong(),
            39.toULong(),
        )
        val consumableItem = expected.toSharedMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin = givenMin.toInt()
            capturedMax = givenMax.toInt()

            consumableItem.removeFirst()
        }

        // When
        val generator = ULongArrayGenerator(random, auxiliaryGenerator)
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
            expected.toULongArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given generate is called with ranges it returns a ULongArray`() {
        // Given
        val expectedMin1 = 0.toULong()
        val expectedMax1 = 42.toULong()
        val expectedMin2 = 3.toULong()
        val expectedMax2 = 41.toULong()

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<ULong, ULong>()

        val expected = listOf(
            23.toULong(),
            7.toULong(),
            39.toULong(),
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
        val generator = ULongArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            ULongRange(expectedMin1, expectedMax1),
            ULongRange(expectedMin2, expectedMax2),
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
            expected.toULongArray().contentEquals(result),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given generate is called with ranges it returns a ULongArray with a given Size`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toULong()
        val expectedMax1 = 42.toULong()
        val expectedMin2 = 3.toULong()
        val expectedMax2 = 41.toULong()

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<ULong, ULong>()

        val expected = listOf(
            23.toULong(),
            7.toULong(),
            39.toULong(),
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
        val generator = ULongArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            ULongRange(expectedMin1, expectedMax1),
            ULongRange(expectedMin2, expectedMax2),
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
            expected.toULongArray().contentEquals(result),
        )
    }
}

private data class ULongRange(
    override val start: ULong,
    override val endInclusive: ULong,
) : ClosedRange<ULong>
