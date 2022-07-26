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

class UByteArrayGeneratorSpec {
    private val random = RandomStub()
    private val range: AtomicRef<Pair<Int, Int>?> = atomic(null)

    @AfterTest
    fun tearDown() {
        random.clear()
        range.getAndSet(null)
    }

    @Test
    @JsName("fn0")
    fun `It fulfils RangedArrayGenerator`() {
        val generator: Any = UByteArrayGenerator(random, RangedGeneratorStub())

        assertTrue(generator is PublicApi.RangedArrayGenerator<*, *>)
    }

    @Test
    @JsName("fn1")
    fun `Given generate is called it returns a UByteArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toUByte()
        val expected = UByteArray(size) { expectedValue }
        val auxiliaryGenerator = RangedGeneratorStub<UByte, UByte>()

        auxiliaryGenerator.generate = { expectedValue }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = UByteArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called and a predicate it returns a UByteArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toUByte()
        val expected = UByteArray(size) { expectedValue }
        val expectedPredicate: Function1<UByte?, Boolean> = { true }
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = RangedGeneratorStub<UByte, UByte>()

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = UByteArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with a size it returns a UByteArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23.toUByte()
        val expected = UByteArray(size) { expectedValue }
        val auxiliaryGenerator = RangedGeneratorStub<UByte, UByte>()

        auxiliaryGenerator.generate = { expectedValue }

        // When
        val generator = UByteArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with a size and a predicate it returns a UByteArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23.toUByte()
        val expected = UByteArray(size) { expectedValue }
        val expectedPredicate: Function1<UByte?, Boolean> = { true }
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = RangedGeneratorStub<UByte, UByte>()

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }

        // When
        val generator = UByteArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with boundaries it returns a UByteArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toUByte()
        val expectedMax = 42.toUByte()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = RangedGeneratorStub<UByte, UByte>()

        val expected = listOf(
            23.toUByte(),
            7.toUByte(),
            39.toUByte(),
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
        val generator = UByteArrayGenerator(random, auxiliaryGenerator)
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
            expected.toUByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn3a")
    fun `Given generate is called with boundaries and a predicate it returns a UByteArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toUByte()
        val expectedMax = 42.toUByte()
        val expectedPredicate: Function1<UByte?, Boolean> = { true }

        var capturedMin: Int? = null
        var capturedMax: Int? = null
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = RangedGeneratorStub<UByte, UByte>()

        val expected = listOf(
            23.toUByte(),
            7.toUByte(),
            39.toUByte(),
        )
        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin = givenMin.toInt()
            capturedMax = givenMax.toInt()
            capturedPredicate = givenPredicate

            consumableItem.removeFirst()
        }

        // When
        val generator = UByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax, predicate = expectedPredicate)

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
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
        assertTrue(
            expected.toUByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn4")
    fun `Given generate is called with boundaries it returns a UByteArray with a given Size`() {
        // Given
        val size = 3
        val expectedMin = 0.toUByte()
        val expectedMax = 42.toUByte()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = RangedGeneratorStub<UByte, UByte>()

        val expected = listOf(
            23.toUByte(),
            7.toUByte(),
            39.toUByte(),
        )
        val consumableItem = expected.toSharedMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin = givenMin.toInt()
            capturedMax = givenMax.toInt()

            consumableItem.removeFirst()
        }

        // When
        val generator = UByteArrayGenerator(random, auxiliaryGenerator)
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
            expected.toUByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn4a")
    fun `Given generate is called with boundaries and size and a predcate it returns a UByteArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toUByte()
        val expectedMax = 42.toUByte()
        val expectedPredicate: Function1<UByte?, Boolean> = { true }

        var capturedMin: Int? = null
        var capturedMax: Int? = null
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = RangedGeneratorStub<UByte, UByte>()

        val expected = listOf(
            23.toUByte(),
            7.toUByte(),
            39.toUByte(),
        )
        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin = givenMin.toInt()
            capturedMax = givenMax.toInt()
            capturedPredicate = givenPredicate

            consumableItem.removeFirst()
        }

        // When
        val generator = UByteArrayGenerator(random, auxiliaryGenerator)
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
            expected.toUByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn5")
    fun `Given generate is called with ranges it returns a UByteArray`() {
        // Given
        val expectedMin1 = 0.toUByte()
        val expectedMax1 = 42.toUByte()
        val expectedMin2 = 3.toUByte()
        val expectedMax2 = 41.toUByte()

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<UByte, UByte>()

        val expected = listOf(
            23.toUByte(),
            7.toUByte(),
            39.toUByte(),
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
        val generator = UByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            UByteRange(expectedMin1, expectedMax1),
            UByteRange(expectedMin2, expectedMax2),
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
            expected.toUByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn5a")
    fun `Given generate is called with ranges and a predicate it returns a UByteArray`() {
        // Given
        val expectedMin1 = 0.toUByte()
        val expectedMax1 = 42.toUByte()
        val expectedMin2 = 3.toUByte()
        val expectedMax2 = 41.toUByte()
        val expectedPredicate: Function1<UByte?, Boolean> = { true }

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()
        val capturedPredicate: MutableList<Function<Boolean>> = sharedMutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<UByte, UByte>()

        val expected = listOf(
            23.toUByte(),
            7.toUByte(),
            39.toUByte(),
        )
        val ranges = sharedMutableListOf(3, 1, 0, 1)

        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin.add(givenMin.toInt())
            capturedMax.add(givenMax.toInt())
            capturedPredicate.add(givenPredicate)

            consumableItem.removeFirst()
        }

        // When
        val generator = UByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            UByteRange(expectedMin1, expectedMax1),
            UByteRange(expectedMin2, expectedMax2),
            predicate = expectedPredicate,
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
        assertSame(
            actual = capturedPredicate[0],
            expected = expectedPredicate,
        )
        assertSame(
            actual = capturedPredicate[1],
            expected = expectedPredicate,
        )

        assertTrue(
            expected.toUByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn6")
    fun `Given generate is called with ranges it returns a UByteArray with a given Size`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toUByte()
        val expectedMax1 = 42.toUByte()
        val expectedMin2 = 3.toUByte()
        val expectedMax2 = 41.toUByte()

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<UByte, UByte>()

        val expected = listOf(
            23.toUByte(),
            7.toUByte(),
            39.toUByte(),
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
        val generator = UByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            UByteRange(expectedMin1, expectedMax1),
            UByteRange(expectedMin2, expectedMax2),
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
            expected.toUByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn6a")
    fun `Given generate is called with ranges a size and a predicate it returns a UByteArray`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toUByte()
        val expectedMax1 = 42.toUByte()
        val expectedMin2 = 3.toUByte()
        val expectedMax2 = 41.toUByte()
        val expectedPredicate: Function1<UByte?, Boolean> = { true }

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()
        val capturedPredicate: MutableList<Function<Boolean>> = sharedMutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<UByte, UByte>()

        val expected = listOf(
            23.toUByte(),
            7.toUByte(),
            39.toUByte(),
        )
        val ranges = sharedMutableListOf(1, 0, 1)

        val consumableItem = expected.toSharedMutableList()

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin.add(givenMin.toInt())
            capturedMax.add(givenMax.toInt())
            capturedPredicate.add(givenPredicate)

            consumableItem.removeFirst()
        }

        // When
        val generator = UByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            UByteRange(expectedMin1, expectedMax1),
            UByteRange(expectedMin2, expectedMax2),
            size = expectedSize,
            predicate = expectedPredicate,
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
        assertSame(
            actual = capturedPredicate[0],
            expected = expectedPredicate,
        )
        assertSame(
            actual = capturedPredicate[1],
            expected = expectedPredicate,
        )

        assertTrue(
            expected.toUByteArray().contentEquals(result),
        )
    }
}

private data class UByteRange(
    override val start: UByte,
    override val endInclusive: UByte,
) : ClosedRange<UByte>
