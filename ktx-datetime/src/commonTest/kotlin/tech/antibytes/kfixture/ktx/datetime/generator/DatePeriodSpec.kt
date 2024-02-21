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
import kotlinx.datetime.DatePeriod
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.RangedGeneratorStub
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

class DatePeriodSpec {
    private val intDependencyGenerator = RangedGeneratorStub<Int, Int>()

    @AfterTest
    fun tearDown() {
        intDependencyGenerator.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils DependentGenerator`() {
        val generator: Any = DatePeriodGenerator

        assertTrue(generator is PublicApi.DependentGeneratorFactory<*>)
    }

    @Test
    @JsName("fn1")
    fun `It fulfils FilterableGenerator`() {
        val intId = resolveGeneratorId(Int::class)
        val generator: Any = DatePeriodGenerator.getInstance(
            RandomStub(),
            mapOf(
                intId to intDependencyGenerator,
            ),
        )

        assertTrue(generator is PublicApi.FilterableGenerator<*, *>)
    }

    @Test
    @JsName("fn2")
    fun `Given generate is called it returns a DatePeriod`() {
        // Given
        val expectedMacro = mutableListOf(
            2929,
            5,
            3,
            7,
            45,
            13,
        )

        val capturedMacroFrom = mutableListOf<Int>()
        val capturedMacroTo = mutableListOf<Int>()

        intDependencyGenerator.generateWithRange = { from, to, _ ->
            capturedMacroFrom.add(from)
            capturedMacroTo.add(to)

            expectedMacro.removeFirst()
        }

        // When
        val actual = DatePeriodGenerator(
            intDependencyGenerator,
        ).generate()

        // Then
        assertEquals(
            actual = actual,
            expected = DatePeriod(
                years = 2929,
                months = 5,
                days = 3,
            ),
        )

        assertEquals(
            actual = capturedMacroFrom,
            expected = listOf(0, 0, 0),
        )
        assertEquals(
            actual = capturedMacroTo,
            expected = listOf(
                Int.MAX_VALUE,
                11,
                Int.MAX_VALUE,
            ),
        )
    }

    @Test
    @JsName("fn3")
    fun `Given generate is called with a predicate it returns a DatePeriod`() {
        // Given
        val expectedMacro = mutableListOf(
            2929,
            5,
            3,
            2020,
            5,
            3,
        )

        val capturedMacroFrom = mutableListOf<Int>()
        val capturedMacroTo = mutableListOf<Int>()

        intDependencyGenerator.generateWithRange = { from, to, _ ->
            capturedMacroFrom.add(from)
            capturedMacroTo.add(to)

            expectedMacro.removeFirst()
        }

        // When
        val actual = DatePeriodGenerator(
            intDependencyGenerator,
        ).generate { datePeriod ->
            datePeriod != DatePeriod(
                years = 2929,
                months = 5,
                days = 3,
            )
        }

        // Then
        assertEquals(
            actual = actual,
            expected = DatePeriod(
                years = 2020,
                months = 5,
                days = 3,
            ),
        )

        assertEquals(
            actual = capturedMacroFrom,
            expected = listOf(0, 0, 0, 0, 0, 0),
        )
        assertEquals(
            actual = capturedMacroTo,
            expected = listOf(
                Int.MAX_VALUE,
                11,
                Int.MAX_VALUE,
                Int.MAX_VALUE,
                11,
                Int.MAX_VALUE,
            ),
        )
    }
}
