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

class CharArrayGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils RangedArrayGenerator`() {
        val generator: Any = CharArrayGenerator(random, RangedGeneratorStub())

        assertTrue(generator is PublicApi.RangedArrayGenerator<*, *>)
    }

    @Test
    @JsName("fn1")
    fun `Given generate is called it returns a CharArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toChar()
        val expected = CharArray(size) { expectedValue }
        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        var range: Pair<Int, Int>? = null

        auxiliaryGenerator.generate = { expectedValue }
        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with a predicate it returns a CharArray`() {
        // Given
        val size = 23
        val expectedValue = 23.toChar()
        val expectedPredicate: Function1<Char?, Boolean> = { true }

        val expected = CharArray(size) { expectedValue }
        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        var range: Pair<Int, Int>? = null
        var capturedPredicate: Function<Boolean>? = null

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }
        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
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
    @JsName("fn2a")
    fun `Given generate is called with a size and a predicate it returns a CharArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = 23.toChar()
        val expectedPredicate: Function1<Char?, Boolean> = { true }
        val expected = CharArray(size) { expectedValue }

        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()
        var capturedPredicate: Function<Boolean>? = null

        auxiliaryGenerator.generateWithPredicate = { givenPredicate ->
            capturedPredicate = givenPredicate

            expectedValue
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
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
    fun `Given generate is called with boundaries it returns a CharArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toChar()
        val expectedMax = 42.toChar()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23.toChar(),
            7.toChar(),
            39.toChar(),
        )
        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
            capturedMin = givenMin.code
            capturedMax = givenMax.code

            consumableItem.removeFirst()
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax)

        // Then
        assertEquals(
            actual = Pair(1, 11),
            expected = range,
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
    @JsName("fn3a")
    fun `Given generate is called with boundaries and a predicate it returns a CharArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toChar()
        val expectedMax = 42.toChar()
        val expectedPredicate: Function1<Char?, Boolean> = { true }

        var capturedMin: Int? = null
        var capturedMax: Int? = null
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23.toChar(),
            7.toChar(),
            39.toChar(),
        )
        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin = givenMin.code
            capturedMax = givenMax.code
            capturedPredicate = givenPredicate

            consumableItem.removeFirst()
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(from = expectedMin, to = expectedMax, predicate = expectedPredicate)

        // Then
        assertEquals(
            actual = Pair(1, 11),
            expected = range,
        )
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.code,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.code,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
        assertTrue(
            expected.toCharArray().contentEquals(result),
        )
    }

    @Test
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
        val consumableItem = expected.toMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
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
    @JsName("fn4a")
    fun `Given generate is called with boundaries and a size and a predicate it returns a CharArray`() {
        // Given
        val size = 3
        val expectedMin = 0.toChar()
        val expectedMax = 42.toChar()
        val expectedPredicate: Function1<Char?, Boolean> = { true }

        var capturedMin: Int? = null
        var capturedMax: Int? = null
        var capturedPredicate: Function<Boolean>? = null

        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        val expected = listOf(
            23.toChar(),
            7.toChar(),
            39.toChar(),
        )
        val consumableItem = expected.toMutableList()

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin = givenMin.code
            capturedMax = givenMax.code
            capturedPredicate = givenPredicate

            consumableItem.removeFirst()
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            from = expectedMin,
            to = expectedMax,
            size = size,
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = capturedMin,
            expected = expectedMin.code,
        )
        assertEquals(
            actual = capturedMax,
            expected = expectedMax.code,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
        assertTrue(
            expected.toCharArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn5")
    fun `Given generate is called with ranges it returns a CharArray`() {
        // Given
        val expectedMin1 = 0.toChar()
        val expectedMax1 = 42.toChar()
        val expectedMin2 = 3.toChar()
        val expectedMax2 = 41.toChar()

        val capturedMin: MutableList<Int> = mutableListOf()
        val capturedMax: MutableList<Int> = mutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23.toChar(),
            7.toChar(),
            39.toChar(),
        )
        val ranges = mutableListOf(3, 1, 0, 1)

        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
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
            actual = range,
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
    @JsName("fn5a")
    fun `Given generate is called with ranges and a predicate it returns a CharArray`() {
        // Given
        val expectedMin1 = 0.toChar()
        val expectedMax1 = 42.toChar()
        val expectedMin2 = 3.toChar()
        val expectedMax2 = 41.toChar()
        val expectedPredicate: Function1<Char?, Boolean> = { true }

        val capturedMin: MutableList<Int> = mutableListOf()
        val capturedMax: MutableList<Int> = mutableListOf()
        val capturedPredicate: MutableList<Function<Boolean>> = mutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23.toChar(),
            7.toChar(),
            39.toChar(),
        )
        val ranges = mutableListOf(3, 1, 0, 1)

        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin.add(givenMin.code)
            capturedMax.add(givenMax.code)
            capturedPredicate.add(givenPredicate)

            consumableItem.removeFirst()
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            CharRange(expectedMin1, expectedMax1),
            CharRange(expectedMin2, expectedMax2),
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = range,
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
        assertSame(
            actual = capturedPredicate[0],
            expected = expectedPredicate,
        )
        assertSame(
            actual = capturedPredicate[1],
            expected = expectedPredicate,
        )

        assertTrue(
            expected.toCharArray().contentEquals(result),
        )
    }

    @Test
    @JsName("fn6")
    fun `Given generate is called with ranges it returns a CharArray with a given Size`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toChar()
        val expectedMax1 = 42.toChar()
        val expectedMin2 = 3.toChar()
        val expectedMax2 = 41.toChar()

        val capturedMin: MutableList<Int> = mutableListOf()
        val capturedMax: MutableList<Int> = mutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23.toChar(),
            7.toChar(),
            39.toChar(),
        )
        val ranges = mutableListOf(1, 0, 1)

        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, _ ->
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
            actual = range,
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
    @JsName("fn6a")
    fun `Given generate is called with ranges and a size and a predicate it returns a CharArray`() {
        // Given
        val expectedSize = 3
        val expectedMin1 = 0.toChar()
        val expectedMax1 = 42.toChar()
        val expectedMin2 = 3.toChar()
        val expectedMax2 = 41.toChar()
        val expectedPredicate: Function1<Char?, Boolean> = { true }

        val capturedMin: MutableList<Int> = mutableListOf()
        val capturedMax: MutableList<Int> = mutableListOf()
        val capturedPredicate: MutableList<Function<Boolean>> = mutableListOf()

        val auxiliaryGenerator = RangedGeneratorStub<Char, Char>()

        var range: Pair<Int, Int>? = null

        val expected = listOf(
            23.toChar(),
            7.toChar(),
            39.toChar(),
        )
        val ranges = mutableListOf(1, 0, 1)

        val consumableItem = expected.toMutableList()

        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            ranges.removeFirst()
        }

        auxiliaryGenerator.generateWithRange = { givenMin, givenMax, givenPredicate ->
            capturedMin.add(givenMin.code)
            capturedMax.add(givenMax.code)
            capturedPredicate.add(givenPredicate)

            consumableItem.removeFirst()
        }

        // When
        val generator = CharArrayGenerator(random, auxiliaryGenerator)
        val result = generator.generate(
            CharRange(expectedMin1, expectedMax1),
            CharRange(expectedMin2, expectedMax2),
            size = expectedSize,
            predicate = expectedPredicate,
        )

        // Then
        assertEquals(
            actual = range,
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
        assertSame(
            actual = capturedPredicate[0],
            expected = expectedPredicate,
        )
        assertSame(
            actual = capturedPredicate[1],
            expected = expectedPredicate,
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
