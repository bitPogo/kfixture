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
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.datetime.DayOfWeek
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.RangedGeneratorStub
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

class DayOfWeekGeneratorSpec {
    private val dependencyGenerator = RangedGeneratorStub<Int, Int>()

    @AfterTest
    fun tearDown() {
        dependencyGenerator.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils DependentGeneratorFactory`() {
        val generator: Any = DayOfWeekGenerator

        assertTrue(generator is PublicApi.DependentGeneratorFactory<*>)
    }

    @Test
    @JsName("fn1")
    fun `Given getInstance is called it returns an FilterableGenerator`() {
        val intId = resolveGeneratorId(Int::class)
        val generator: Any = DayOfWeekGenerator.getInstance(RandomStub(), mapOf(intId to dependencyGenerator))

        assertTrue(generator is PublicApi.FilterableGenerator<*, *>)
    }

    @Test
    @JsName("fn2")
    fun `Given generate is called it returns a DayOfWeek`() {
        // Given
        val expected = 5

        var capturedFrom: Int? = null
        var capturedTo: Int? = null

        dependencyGenerator.generateWithRange = { from, to, _ ->
            capturedFrom = from
            capturedTo = to

            expected
        }

        val generator = DayOfWeekGenerator(
            dayGenerator = dependencyGenerator,
        )

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = DayOfWeek.FRIDAY,
        )
        assertEquals(
            actual = capturedFrom,
            expected = 1,
        )
        assertEquals(
            actual = capturedTo,
            expected = 7,
        )
    }

    @Test
    @JsName("fn3")
    fun `Given generate is called with a predicate it returns a DayOfWeek`() {
        // Given
        val expected = 4
        val expectedPredicate: Function1<Int?, Boolean> = { true }

        var capturedFrom: Int? = null
        var capturedTo: Int? = null
        var capturedPredicate: Function1<Int?, Boolean>? = null

        dependencyGenerator.generateWithRange = { from, to, predicate ->
            capturedFrom = from
            capturedTo = to
            capturedPredicate = predicate

            expected
        }

        val generator = DayOfWeekGenerator(
            dayGenerator = dependencyGenerator,
        )

        // When
        val result = generator.generate(predicate = expectedPredicate)

        // Then
        assertEquals(
            actual = result,
            expected = DayOfWeek.THURSDAY,
        )
        assertEquals(
            actual = capturedFrom,
            expected = 1,
        )
        assertEquals(
            actual = capturedTo,
            expected = 7,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }
}
