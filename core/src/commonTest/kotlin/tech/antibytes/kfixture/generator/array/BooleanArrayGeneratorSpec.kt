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
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub

class BooleanArrayGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils FilterableArrayGenerator`() {
        val generator: Any = BooleanArrayGenerator(random, FilterableGeneratorStub<Boolean, Boolean>())

        assertTrue(generator is PublicApi.ArrayGenerator<*>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a BooleanArray`() {
        // Given
        val size = 23
        val expectedValue = false
        val expected = BooleanArray(size) { expectedValue }
        val auxiliaryGenerator = FilterableGeneratorStub<Boolean, Boolean>()

        var range: Pair<Int, Int>? = null

        auxiliaryGenerator.generate = { expectedValue }
        random.nextIntRanged = { from, to ->
            range = Pair(from, to)
            size
        }

        // When
        val generator = BooleanArrayGenerator(random, auxiliaryGenerator)
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
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate is called with a size it returns a BooleanArray in the given size`() {
        // Given
        val size = 12
        val expectedValue = true
        val expected = BooleanArray(size) { expectedValue }
        val auxiliaryGenerator = FilterableGeneratorStub<Boolean, Boolean>()

        auxiliaryGenerator.generate = { expectedValue }

        // When
        val generator = BooleanArrayGenerator(random, auxiliaryGenerator)
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
}
