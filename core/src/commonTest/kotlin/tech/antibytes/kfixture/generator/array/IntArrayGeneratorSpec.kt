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
import tech.antibytes.kfixture.mock.SignedNumberGeneratorStub

class IntArrayGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils SignedNumericArrayGenerator`() {
        val generator: Any = IntArrayGenerator(random, SignedNumberGeneratorStub())

        assertTrue(generator is PublicApi.SignedNumericArrayGenerator<*, *>)
    }

    @Test
    @JsName("fn1")
    fun `Given generate is called it returns a IntArray`() {
        // Given
        val size = 23
        val expectedValue = 23
        val expected = IntArray(size) { expectedValue }
        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        var range: Pair<Int, Int>? = null

        auxiliaryGenerator.generate = { expectedValue }
        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with a predicate it returns a IntArray`() {
        // Given
        val size = 23
        val expectedValue = 23
        val expected = IntArray(size) { expectedValue }
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

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
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
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
    @JsName("fn2a")
    fun `Given generate is called with a size and a predicate it returns a IntArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23
        val expected = IntArray(size) { expectedValue }
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with boundaries it returns a IntArray`() {
        // Given
        val size = 3
        val expectedMin = 0
        val expectedMax = 42

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23,
            7,
            39,
        )
        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin = givenMin
            capturedMax = givenMax

            consumableItem.removeFirst()
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax)

        // Then
        assertEquals(
            actual = Pair(1, 11),
            expected = range,
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
    @JsName("fn3a")
    fun `Given generate is called with boundaries with a predicate it returns a IntArray`() {
        // Given
        val size = 3
        val expectedMin = 0
        val expectedMax = 42
        val expectedPredicate: Function1<Int?, Boolean> = { true }

        var capturedMin: Int? = null
        var capturedMax: Int? = null
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23,
            7,
            39,
        )
        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin = givenMin
            capturedMax = givenMax
            capturedPredicate = givenPredicate

            consumableItem.removeFirst()
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax, predicate = expectedPredicate)

        // Then
        assertEquals(
            actual = Pair(1, 11),
            expected = range,
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
            expected.toIntArray().contentEquals(result),
        )
    }

    @Test
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
        val consumableItem = expected.toMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
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
    @JsName("fn4a")
    fun `Given generate is called with boundaries and a size and a preditcate it returns a IntArray`() {
        // Given
        val size = 3
        val expectedMin = 0
        val expectedMax = 42
        val expectedPredicate: Function1<Int?, Boolean> = { true }

        var capturedMin: Int? = null
        var capturedMax: Int? = null
        var capturedPredicate: Function<Boolean>? = null
        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        val expected = listOf(
            23,
            7,
            39,
        )
        val consumableItem = expected.toMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin = givenMin
            capturedMax = givenMax
            capturedPredicate = givenPredicate

            consumableItem.removeFirst()
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
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
            expected.toIntArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn5")
    fun `Given generate is called with ranges it returns a IntArray`() {
        // Given
        val expectedMin1 = 0
        val expectedMax1 = 42
        val expectedMin2 = 3
        val expectedMax2 = 41

        val capturedMin: MutableList<Int> = mutableListOf()
        val capturedMax: MutableList<Int> = mutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23,
            7,
            39,
        )
        val ranges = mutableListOf(3, 1, 0, 1)

        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
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
            actual = range,
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
    @JsName("fn5a")
    fun `Given generate is called with ranges and a predicate it returns a IntArray`() {
        // Given
        val expectedMin1 = 0
        val expectedMax1 = 42
        val expectedMin2 = 3
        val expectedMax2 = 41
        val expectedPredicate: Function1<Int?, Boolean> = { true }

        val capturedMin: MutableList<Int> = mutableListOf()
        val capturedMax: MutableList<Int> = mutableListOf()
        val capturedPredicate: MutableList<Function<Boolean>> = mutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23,
            7,
            39,
        )
        val ranges = mutableListOf(3, 1, 0, 1)

        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin.add(givenMin)
            capturedMax.add(givenMax)
            capturedPredicate.add(givenPredicate)

            consumableItem.removeFirst()
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            IntRange(expectedMin1, expectedMax1),
            IntRange(expectedMin2, expectedMax2),
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = range,
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
            expected.toIntArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn6")
    fun `Given generate is called with ranges it returns a IntArray with a given Size`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0
        val expectedMax1 = 42
        val expectedMin2 = 3
        val expectedMax2 = 41

        val capturedMin: MutableList<Int> = mutableListOf()
        val capturedMax: MutableList<Int> = mutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23,
            7,
            39,
        )
        val ranges = mutableListOf(1, 0, 1)

        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
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
            actual = range,
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
    @JsName("fn6a")
    fun `Given generate is called with ranges and a size and a predicate it returns a IntArray`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0
        val expectedMax1 = 42
        val expectedMin2 = 3
        val expectedMax2 = 41
        val expectedPredicate: Function1<Int?, Boolean> = { true }

        val capturedMin: MutableList<Int> = mutableListOf()
        val capturedMax: MutableList<Int> = mutableListOf()
        val capturedPredicate: MutableList<Function<Boolean>> = mutableListOf()

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23,
            7,
            39,
        )
        val ranges = mutableListOf(1, 0, 1)

        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin.add(givenMin)
            capturedMax.add(givenMax)
            capturedPredicate.add(givenPredicate)

            consumableItem.removeFirst()
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            IntRange(expectedMin1, expectedMax1),
            IntRange(expectedMin2, expectedMax2),
            size = expectedSize,
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = range,
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
            expected.toIntArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn7")
    fun `Given generate is called with a Sign it returns a IntArray`() {
        // Given
        val expectedSign = PublicApi.Sign.NEGATIVE
        val size = 23

        var capturedSign: PublicApi.Sign? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        var range: Pair<Int, Int>? = null

        val expectedValue = 42
        val expected = IntArray(size) { expectedValue }

        auxiliaryGenerator.generateWithSign = { givenSign, _ ->
            capturedSign = givenSign

            expectedValue
        }
        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(expectedSign)

        // Then
        assertEquals(
            actual = Pair(1, 11),
            expected = range,
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
    @JsName("fn7a")
    fun `Given generate is called with a Sign and a Predicate it returns a IntArray`() {
        // Given
        val expectedSign = PublicApi.Sign.NEGATIVE
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val size = 23

        var capturedSign: PublicApi.Sign? = null
        var capturedPredicate: Function1<Int?, Boolean>? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        var range: Pair<Int, Int>? = null

        val expectedValue = 42
        val expected = IntArray(size) { expectedValue }

        auxiliaryGenerator.generateWithSign = { givenSign, givenPredicate ->
            capturedSign = givenSign
            capturedPredicate = givenPredicate

            expectedValue
        }
        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(expectedSign, expectedPredicate)

        // Then
        assertEquals(
            actual = Pair(1, 11),
            expected = range,
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
    @JsName("fn8")
    fun `Given generate is called with a Sign and Size it returns a IntArray`() {
        // Given
        val expectedSign = PublicApi.Sign.NEGATIVE
        val size = 23

        var capturedSign: PublicApi.Sign? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        val expectedValue = 42
        val expected = IntArray(size) { expectedValue }

        auxiliaryGenerator.generateWithSign = { givenSign, _ ->
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

    @Test
    @JsName("fn8a")
    fun `Given generate is called with a Sign and Size and a Predicate it returns a IntArray`() {
        // Given
        val expectedSign = PublicApi.Sign.NEGATIVE
        val expectedPredicate: Function1<Int?, Boolean> = { true }
        val size = 23

        var capturedSign: PublicApi.Sign? = null
        var capturedPredicate: Function1<Int?, Boolean>? = null

        val auxiliaryGenerator = SignedNumberGeneratorStub<Int, Int>()

        val expectedValue = 42
        val expected = IntArray(size) { expectedValue }

        auxiliaryGenerator.generateWithSign = { givenSign, givenPredicate ->
            capturedSign = givenSign
            capturedPredicate = givenPredicate

            expectedValue
        }

        // When
        val generator = IntArrayGenerator(random, auxiliaryGenerator)
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
