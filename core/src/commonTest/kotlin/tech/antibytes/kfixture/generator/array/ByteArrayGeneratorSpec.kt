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

class ByteArrayGeneratorSpec {
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
        val generator: Any = ByteArrayGenerator(random, SignedNumberGeneratorStub())

        assertTrue(generator is PublicApi.SignedNumericArrayGenerator<*, *>)
    }

    @Test
    @JsName("fn1")
    fun `Given generate is called it returns a ByteArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toByte()
        val expected = ByteArray(size) { expectedValue }
        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        auxiliaryGenerator.generate = { expectedValue }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate()

        // Then
        assertEquals(
            actual = Pair(1, 11),
            expected = range.value,
        )
        assertTrue(
            expected.contentEquals(result),
        )
    }

    @Test
    @JsName("fn2")
    fun `Given generate is called with a Predicate it returns a ByteArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toByte()
        val expectedPredicate: Function1<Byte?, Boolean> = { true }

        val expected = ByteArray(size) { expectedValue }
        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()
        var capturedPredicate: Function<Boolean>? = null

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(expectedPredicate)

        // Then
        assertEquals(
            actual = Pair(1, 11),
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
    @JsName("fn3")
    fun `Given generate is called with a size it returns a ByteArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23.toByte()
        val expected = ByteArray(size) { expectedValue }
        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        random.nextBytesArray = { arraySize -> ByteArray(arraySize) }
        auxiliaryGenerator.generate = { expectedValue }

        // When
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
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
    @JsName("fn3a")
    fun `Given generate is called with a size and a Predicate it returns a ByteArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23.toByte()
        val expectedPredicate: Function1<Byte?, Boolean> = { true }

        val expected = ByteArray(size) { expectedValue }
        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()
        var capturedPredicate: Function<Boolean>? = null

        random.nextBytesArray = { arraySize -> ByteArray(arraySize) }

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }

        // When
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
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
    @JsName("fn4")
    fun `Given generate is called with boundaries it returns a ByteArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toByte()
        val expectedMax = 42.toByte()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        val expected = listOf(
            23.toByte(),
            7.toByte(),
            39.toByte(),
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
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax)

        // Then
        assertEquals(
            actual = Pair(1, 11),
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
            expected.toByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn4a")
    fun `Given generate is called with a Predicate with boundaries it returns a ByteArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toByte()
        val expectedMax = 42.toByte()
        val expectedPredicate: Function1<Byte?, Boolean> = { true }

        var capturedMin: Int? = null
        var capturedMax: Int? = null
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        val expected = listOf(
            23.toByte(),
            7.toByte(),
            39.toByte(),
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
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax, predicate = expectedPredicate)

        // Then
        assertEquals(
            actual = Pair(1, 11),
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
            expected.toByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn5")
    fun `Given generate is called with boundaries it returns a ByteArray with a given Size`() {
        // Given
        val size = 3
        val expectedMin = 0.toByte()
        val expectedMax = 42.toByte()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        val expected = listOf(
            23.toByte(),
            7.toByte(),
            39.toByte(),
        )
        val consumableItem = expected.toSharedMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin = givenMin.toInt()
            capturedMax = givenMax.toInt()

            consumableItem.removeFirst()
        }

        // When
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
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
            expected.toByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn5a")
    fun `Given generate is called with boundaries and a Predicate it returns a ByteArray with a given Size`() {
        // Given
        val size = 3
        val expectedMin = 0.toByte()
        val expectedMax = 42.toByte()
        val expectedPredicate: Function1<Byte?, Boolean> = { true }

        var capturedMin: Int? = null
        var capturedMax: Int? = null
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        val expected = listOf(
            23.toByte(),
            7.toByte(),
            39.toByte(),
        )
        val consumableItem = expected.toSharedMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin = givenMin.toInt()
            capturedMax = givenMax.toInt()
            capturedPredicate = givenPredicate

            consumableItem.removeFirst()
        }

        // When
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
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
            expected.toByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn6")
    fun `Given generate is called with ranges it returns a ByteArray`() {
        // Given
        val expectedMin1 = 0.toByte()
        val expectedMax1 = 42.toByte()
        val expectedMin2 = 3.toByte()
        val expectedMax2 = 41.toByte()

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        val expected = listOf(
            23.toByte(),
            7.toByte(),
            39.toByte(),
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
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            ByteRange(expectedMin1, expectedMax1),
            ByteRange(expectedMin2, expectedMax2),
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
            expected.toByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn6a")
    fun `Given generate is called with ranges and a Predicate it returns a ByteArray`() {
        // Given
        val expectedMin1 = 0.toByte()
        val expectedMax1 = 42.toByte()
        val expectedMin2 = 3.toByte()
        val expectedMax2 = 41.toByte()
        val expectedPredicate: Function1<Byte?, Boolean> = { true }

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()
        val capturedPredicate: MutableList<Function<Boolean>> = sharedMutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        val expected = listOf(
            23.toByte(),
            7.toByte(),
            39.toByte(),
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
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            ByteRange(expectedMin1, expectedMax1),
            ByteRange(expectedMin2, expectedMax2),
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
            expected.toByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn7")
    fun `Given generate is called with ranges and a size it returns a ByteArray`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toByte()
        val expectedMax1 = 42.toByte()
        val expectedMin2 = 3.toByte()
        val expectedMax2 = 41.toByte()

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        val expected = listOf(
            23.toByte(),
            7.toByte(),
            39.toByte(),
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
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            ByteRange(expectedMin1, expectedMax1),
            ByteRange(expectedMin2, expectedMax2),
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
            expected.toByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn7a")
    fun `Given generate is called with ranges and a size and a Predicate it returns a ByteArray`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toByte()
        val expectedMax1 = 42.toByte()
        val expectedMin2 = 3.toByte()
        val expectedMax2 = 41.toByte()
        val expectedPredicate: Function1<Byte?, Boolean> = { true }

        val capturedMin: MutableList<Int> = sharedMutableListOf()
        val capturedMax: MutableList<Int> = sharedMutableListOf()
        val capturedPredicate: MutableList<Function<Boolean>> = sharedMutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        val expected = listOf(
            23.toByte(),
            7.toByte(),
            39.toByte(),
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
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            ByteRange(expectedMin1, expectedMax1),
            ByteRange(expectedMin2, expectedMax2),
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
            expected.toByteArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn8")
    fun `Given generate is called with a Sign it returns a ByteArray`() {
        // Given
        val expectedSign = PublicApi.Sign.NEGATIVE
        val size = 23

        var capturedSign: PublicApi.Sign? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        val expectedValue = 42.toByte()
        val expected = ByteArray(size) { expectedValue }

        auxiliaryGenerator.generateWithSign = { givenSign, _ ->
            capturedSign = givenSign

            expectedValue
        }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(expectedSign)

        // Then
        assertEquals(
            actual = Pair(1, 11),
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
    @JsName("fn8a")
    fun `Given generate is called with a Sign and a Predicate it returns a ByteArray`() {
        // Given
        val expectedSign = PublicApi.Sign.NEGATIVE
        val expectedPredicate: Function1<Byte?, Boolean> = { true }
        val size = 23

        var capturedSign: PublicApi.Sign? = null
        var capturedPredicate: Function1<Byte?, Boolean>? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        val expectedValue = 42.toByte()
        val expected = ByteArray(size) { expectedValue }

        auxiliaryGenerator.generateWithSign = { givenSign, givenPredicate ->
            capturedSign = givenSign
            capturedPredicate = givenPredicate

            expectedValue
        }
        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        // When
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(expectedSign, expectedPredicate)

        // Then
        assertEquals(
            actual = Pair(1, 11),
            expected = range.value,
        )
        assertEquals(
            actual = capturedSign,
            expected = expectedSign,
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
    @JsName("fn9")
    fun `Given generate is called with a Sign and Size it returns a ByteArray`() {
        // Given
        val expectedSign = PublicApi.Sign.NEGATIVE
        val size = 23

        var capturedSign: PublicApi.Sign? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        val expectedValue = 42.toByte()
        val expected = ByteArray(size) { expectedValue }

        auxiliaryGenerator.generateWithSign = { givenSign, _ ->
            capturedSign = givenSign

            expectedValue
        }

        // When
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
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

    @Test
    @JsName("fn9a")
    fun `Given generate is called with a Sign and Size and a Predicate it returns a ByteArray`() {
        // Given
        val expectedSign = PublicApi.Sign.NEGATIVE
        val expectedPredicate: Function1<Byte?, Boolean> = { true }
        val size = 23

        var capturedSign: PublicApi.Sign? = null
        var capturedPredicate: Function1<Byte?, Boolean>? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Byte, Byte>()

        val expectedValue = 42.toByte()
        val expected = ByteArray(size) { expectedValue }

        auxiliaryGenerator.generateWithSign = { givenSign, givenPredicate ->
            capturedSign = givenSign
            capturedPredicate = givenPredicate

            expectedValue
        }

        // When
        val generator = ByteArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(expectedSign, size, expectedPredicate)

        // Then
        assertEquals(
            actual = capturedSign,
            expected = expectedSign,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
        assertTrue(
            expected.contentEquals(result),
        )
    }
}

private data class ByteRange(
    override val start: Byte,
    override val endInclusive: Byte,
) : ClosedRange<Byte>
