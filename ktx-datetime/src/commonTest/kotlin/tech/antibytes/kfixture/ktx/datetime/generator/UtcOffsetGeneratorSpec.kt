/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.datetime.UtcOffset
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.RangedGeneratorStub
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

class UtcOffsetGeneratorSpec {
    private val dependencyGenerator = RangedGeneratorStub<Int, Int>()
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        dependencyGenerator.clear()
        random.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils DependentGenerator`() {
        val generator: Any = UtcOffsetGenerator

        assertTrue(generator is PublicApi.DependentGeneratorFactory<*>)
    }

    @Test
    @JsName("fn1")
    fun `It fulfils FilterableGenerator`() {
        val intId = resolveGeneratorId(Int::class)
        val generator: Any = UtcOffsetGenerator.getInstance(
            random,
            mapOf(
                intId to dependencyGenerator,
            ),
        )

        assertTrue(generator is PublicApi.FilterableGenerator<*, *>)
    }

    @Test
    @JsName("fn2")
    fun `Given generate is called it returns a UtcOffset with null`() {
        // Given
        random.nextBoolean = { false }

        val generator = UtcOffsetGenerator(
            random = random,
            offsetGenerator = dependencyGenerator,
        )

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = UtcOffset(
                hours = null,
                minutes = null,
                seconds = null,
            ),
        )
    }

    @Test
    @JsName("fn3")
    fun `Given generate is called it returns a UtcOffset for positive integers`() {
        // Given
        val expected = mutableListOf(
            17,
            59,
            21,
        )

        val capturedFrom: MutableList<Int> = mutableListOf()
        val capturedTo: MutableList<Int> = mutableListOf()

        random.nextBoolean = { true }

        dependencyGenerator.generateWithRange = { from, to, _ ->
            capturedFrom.add(from)
            capturedTo.add(to)

            expected.removeFirst()
        }

        val generator = UtcOffsetGenerator(
            random = random,
            offsetGenerator = dependencyGenerator,
        )

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = UtcOffset(
                hours = 17,
                minutes = 59,
                seconds = 21,
            ),
        )

        assertEquals(
            actual = capturedFrom,
            expected = listOf(0, 0, 0),
        )

        assertEquals(
            actual = capturedTo,
            expected = listOf(18, 59, 59),
        )
    }

    @Test
    @JsName("fn4")
    fun `Given generate is called it returns a UtcOffset for negative integers`() {
        // Given
        val expected = mutableListOf(
            17,
            59,
            21,
        )

        var next = false

        val capturedFrom: MutableList<Int> = mutableListOf()
        val capturedTo: MutableList<Int> = mutableListOf()

        random.nextBoolean = {
            next.also {
                next = true
            }
        }

        dependencyGenerator.generateWithRange = { from, to, _ ->
            capturedFrom.add(from)
            capturedTo.add(to)

            expected.removeFirst()
        }

        val generator = UtcOffsetGenerator(
            random = random,
            offsetGenerator = dependencyGenerator,
        )

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = UtcOffset(
                hours = -17,
                minutes = -59,
                seconds = -21,
            ),
        )

        assertEquals(
            actual = capturedFrom,
            expected = listOf(0, 0, 0),
        )

        assertEquals(
            actual = capturedTo,
            expected = listOf(18, 59, 59),
        )
    }

    @Test
    @JsName("fn5")
    fun `Given generate is called it returns a UtcOffset while respecting the boundaries`() {
        // Given
        val expected = mutableListOf(
            18,
            59,
            21,
        )

        val capturedFrom: MutableList<Int> = mutableListOf()
        val capturedTo: MutableList<Int> = mutableListOf()

        random.nextBoolean = { true }

        dependencyGenerator.generateWithRange = { from, to, _ ->
            capturedFrom.add(from)
            capturedTo.add(to)

            expected.removeFirst()
        }

        val generator = UtcOffsetGenerator(
            random = random,
            offsetGenerator = dependencyGenerator,
        )

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = UtcOffset(
                hours = 18,
                minutes = 0,
                seconds = 0,
            ),
        )

        assertEquals(
            actual = capturedFrom,
            expected = listOf(0),
        )

        assertEquals(
            actual = capturedTo,
            expected = listOf(18),
        )
    }

    @Test
    @JsName("fn6")
    fun `Given generate is called with a predicate it returns a UtcOffset`() {
        // Given
        val expected = mutableListOf(
            18,
            17,
            59,
            21,
        )

        random.nextBoolean = { true }

        dependencyGenerator.generateWithRange = { _, _, _ ->
            expected.removeFirst()
        }

        val generator = UtcOffsetGenerator(
            random = random,
            offsetGenerator = dependencyGenerator,
        )

        // When
        val result = generator.generate { offset ->
            offset!!.totalSeconds != 64800
        }

        // Then
        assertEquals(
            actual = result,
            expected = UtcOffset(
                hours = 17,
                minutes = 59,
                seconds = 21,
            ),
        )
    }
}
