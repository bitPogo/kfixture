/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.RangedGeneratorStub

class UIntArrayGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils RangedArrayGenerator`() {
        val generator: Any = UIntArrayGenerator(random, RangedGeneratorStub())

        assertTrue(generator is PublicApi.RangedArrayGenerator<*, *>)
    }

    @Test
    @JsName("fn1")
    fun `Given generate is called it returns a UIntArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toUInt()
        val expected = UIntArray(size) { expectedValue }
        val auxiliaryGenerator = RangedGeneratorStub<UInt, UInt>()

        var range: Pair<Int, Int>? = null

        auxiliaryGenerator.generate = { expectedValue }
        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        // When
        val generator = UIntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate()

        // Then
        assertEquals(
            actual = Pair(1, 11),
            expected = range,
        )
        assertTrue(
            expected.contentEquals(result),
        )
    }

    @Test
    @JsName("fn1a")
    fun `Given generate is called with a predicate it returns a UIntArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toUInt()
        val expected = UIntArray(size) { expectedValue }
        val expectedPredicate: Function1<UInt?, Boolean> = { true }
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = RangedGeneratorStub<UInt, UInt>()

        var range: Pair<Int, Int>? = null

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }
        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        // When
        val generator = UIntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(expectedPredicate)

        // Then
        assertEquals(
            actual = Pair(1, 11),
            expected = range,
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
    fun `Given generate is called with a size it returns a UIntArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23.toUInt()
        val expected = UIntArray(size) { expectedValue }
        val auxiliaryGenerator = RangedGeneratorStub<UInt, UInt>()

        auxiliaryGenerator.generate = { expectedValue }

        // When
        val generator = UIntArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with a size and a predicate it returns a UIntArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23.toUInt()
        val expected = UIntArray(size) { expectedValue }
        val expectedPredicate: Function1<UInt?, Boolean> = { true }
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = RangedGeneratorStub<UInt, UInt>()

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }

        // When
        val generator = UIntArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with boundaries it returns a UIntArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toUInt()
        val expectedMax = 42.toUInt()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = RangedGeneratorStub<UInt, UInt>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23.toUInt(),
            7.toUInt(),
            39.toUInt(),
        )
        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin = givenMin.toInt()
            capturedMax = givenMax.toInt()

            consumableItem.removeFirst()
        }

        // When
        val generator = UIntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax)

        // Then
        assertEquals(
            actual = Pair(1, 11),
            expected = range,
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
            expected.toUIntArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn3a")
    fun `Given generate is called with boundaries and a predicate it returns a UIntArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toUInt()
        val expectedMax = 42.toUInt()
        val expectedPredicate: Function1<UInt?, Boolean> = { true }

        var capturedMin: Int? = null
        var capturedMax: Int? = null
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = RangedGeneratorStub<UInt, UInt>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23.toUInt(),
            7.toUInt(),
            39.toUInt(),
        )
        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin = givenMin.toInt()
            capturedMax = givenMax.toInt()
            capturedPredicate = givenPredicate

            consumableItem.removeFirst()
        }

        // When
        val generator = UIntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax, predicate = expectedPredicate)

        // Then
        assertEquals(
            actual = Pair(1, 11),
            expected = range,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.toInt(),
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
        assertTrue(
            expected.toUIntArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn4")
    fun `Given generate is called with boundaries it returns a UIntArray with a given Size`() {
        // Given
        val size = 3
        val expectedMin = 0.toUInt()
        val expectedMax = 42.toUInt()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = RangedGeneratorStub<UInt, UInt>()

        val expected = listOf(
            23.toUInt(),
            7.toUInt(),
            39.toUInt(),
        )
        val consumableItem = expected.toMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin = givenMin.toInt()
            capturedMax = givenMax.toInt()

            consumableItem.removeFirst()
        }

        // When
        val generator = UIntArrayGenerator(random, auxiliaryGenerator)
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
            expected.toUIntArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn4a")
    fun `Given generate is called with boundaries and a predicate it returns a UIntArray with a given Size`() {
        // Given
        val size = 3
        val expectedMin = 0.toUInt()
        val expectedMax = 42.toUInt()
        val expectedPredicate: Function1<UInt?, Boolean> = { true }

        var capturedMin: Int? = null
        var capturedMax: Int? = null
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = RangedGeneratorStub<UInt, UInt>()

        val expected = listOf(
            23.toUInt(),
            7.toUInt(),
            39.toUInt(),
        )
        val consumableItem = expected.toMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin = givenMin.toInt()
            capturedMax = givenMax.toInt()
            capturedPredicate = givenPredicate

            consumableItem.removeFirst()
        }

        // When
        val generator = UIntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
            size = size,
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.toInt(),
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.toInt(),
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
        assertTrue(
            expected.toUIntArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn5")
    fun `Given generate is called with ranges it returns a UIntArray`() {
        // Given
        val expectedMin1 = 0.toUInt()
        val expectedMax1 = 42.toUInt()
        val expectedMin2 = 3.toUInt()
        val expectedMax2 = 41.toUInt()

        val capturedMin: MutableList<Int> = mutableListOf()
        val capturedMax: MutableList<Int> = mutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<UInt, UInt>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23.toUInt(),
            7.toUInt(),
            39.toUInt(),
        )
        val ranges = mutableListOf(3, 1, 0, 1)

        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin.add(givenMin.toInt())
            capturedMax.add(givenMax.toInt())

            consumableItem.removeFirst()
        }

        // When
        val generator = UIntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            UIntRange(expectedMin1, expectedMax1),
            UIntRange(expectedMin2, expectedMax2),
        )

        // Then
        assertEquals(
            actual = range,
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
            expected.toUIntArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn5a")
    fun `Given generate is called with ranges and a predicate it returns a UIntArray`() {
        // Given
        val expectedMin1 = 0.toUInt()
        val expectedMax1 = 42.toUInt()
        val expectedMin2 = 3.toUInt()
        val expectedMax2 = 41.toUInt()
        val expectedPredicate: Function1<UInt?, Boolean> = { true }

        val capturedMin: MutableList<Int> = mutableListOf()
        val capturedMax: MutableList<Int> = mutableListOf()
        val capturedPredicate: MutableList<Function<Boolean>> = mutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<UInt, UInt>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23.toUInt(),
            7.toUInt(),
            39.toUInt(),
        )
        val ranges = mutableListOf(3, 1, 0, 1)

        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin.add(givenMin.toInt())
            capturedMax.add(givenMax.toInt())
            capturedPredicate.add(givenPredicate)

            consumableItem.removeFirst()
        }

        // When
        val generator = UIntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            UIntRange(expectedMin1, expectedMax1),
            UIntRange(expectedMin2, expectedMax2),
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = range,
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
        assertSame(
            actual = capturedPredicate[0],
            expected = expectedPredicate,
        )
        assertSame(
            actual = capturedPredicate[1],
            expected = expectedPredicate,
        )

        assertTrue(
            expected.toUIntArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn6")
    fun `Given generate is called with ranges it returns a UIntArray with a given Size`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toUInt()
        val expectedMax1 = 42.toUInt()
        val expectedMin2 = 3.toUInt()
        val expectedMax2 = 41.toUInt()

        val capturedMin: MutableList<Int> = mutableListOf()
        val capturedMax: MutableList<Int> = mutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<UInt, UInt>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23.toUInt(),
            7.toUInt(),
            39.toUInt(),
        )
        val ranges = mutableListOf(1, 0, 1)

        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin.add(givenMin.toInt())
            capturedMax.add(givenMax.toInt())

            consumableItem.removeFirst()
        }

        // When
        val generator = UIntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            UIntRange(expectedMin1, expectedMax1),
            UIntRange(expectedMin2, expectedMax2),
            size = expectedSize,
        )

        // Then
        assertEquals(
            actual = range,
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
            expected.toUIntArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn6a")
    fun `Given generate is called with ranges and size and predicate it returns a UIntArray`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toUInt()
        val expectedMax1 = 42.toUInt()
        val expectedMin2 = 3.toUInt()
        val expectedMax2 = 41.toUInt()
        val expectedPredicate: Function1<UInt?, Boolean> = { true }

        val capturedMin: MutableList<Int> = mutableListOf()
        val capturedMax: MutableList<Int> = mutableListOf()
        val capturedPredicate: MutableList<Function<Boolean>> = mutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<UInt, UInt>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23.toUInt(),
            7.toUInt(),
            39.toUInt(),
        )
        val ranges = mutableListOf(1, 0, 1)

        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin.add(givenMin.toInt())
            capturedMax.add(givenMax.toInt())
            capturedPredicate.add(givenPredicate)

            consumableItem.removeFirst()
        }

        // When
        val generator = UIntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            UIntRange(expectedMin1, expectedMax1),
            UIntRange(expectedMin2, expectedMax2),
            size = expectedSize,
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = range,
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
        assertSame(
            actual = capturedPredicate[0],
            expected = expectedPredicate,
        )
        assertSame(
            actual = capturedPredicate[1],
            expected = expectedPredicate,
        )

        assertTrue(
            expected.toUIntArray().contentEquals(result),
        )
    }
}

private data class UIntRange(
    override val start: UInt,
    override val endInclusive: UInt,
) : ClosedRange<UInt>
