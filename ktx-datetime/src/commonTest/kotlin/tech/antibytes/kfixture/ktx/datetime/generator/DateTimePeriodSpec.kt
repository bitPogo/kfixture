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
import kotlinx.datetime.DateTimePeriod
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.RangedGeneratorStub
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

class DateTimePeriodSpec {
    private val longDependencyGenerator = RangedGeneratorStub<Long, Long>()
    private val intDependencyGenerator = RangedGeneratorStub<Int, Int>()

    @AfterTest
    fun tearDown() {
        longDependencyGenerator.clear()
        intDependencyGenerator.clear()
    }

    @Test
    @JsName("fn9")
    fun `It fulfils DependentGenerator`() {
        val generator: Any = DateTimePeriodGenerator

        assertTrue(generator is PublicApi.DependentGeneratorFactory<*>)
    }

    @Test
    @JsName("fn1")
    fun `It fulfils FilterableGenerator`() {
        val longId = resolveGeneratorId(Long::class)
        val intId = resolveGeneratorId(Int::class)
        val generator: Any = DateTimePeriodGenerator.getInstance(
            RandomStub(),
            mapOf(
                longId to longDependencyGenerator,
                intId to intDependencyGenerator,
            ),
        )

        assertTrue(generator is PublicApi.FilterableGenerator<*, *>)
    }

    @Test
    @JsName("fn2")
    fun `Given generate is called it returns a DateTimePeriod`() {
        // Given
        val expectedMacro = mutableListOf(
            2929,
            5,
            3,
            7,
            45,
            13,
        )

        val expectedMicro = 42L

        val capturedMacroFrom = mutableListOf<Int>()
        var capturedMicroFrom: Long? = null

        val capturedMacroTo = mutableListOf<Int>()
        var capturedMicroTo: Long? = null

        intDependencyGenerator.generateWithRange = { from, to, _ ->
            capturedMacroFrom.add(from)
            capturedMacroTo.add(to)

            expectedMacro.removeFirst()
        }

        longDependencyGenerator.generateWithRange = { from, to, _ ->
            capturedMicroFrom = from
            capturedMicroTo = to

            expectedMicro
        }

        // When
        val actual = DateTimePeriodGenerator(
            intDependencyGenerator,
            longDependencyGenerator,
        ).generate()

        // Then
        assertEquals(
            actual = actual,
            expected = DateTimePeriod(
                years = 2929,
                months = 5,
                days = 3,
                hours = 7,
                minutes = 45,
                seconds = 13,
                nanoseconds = 42L,
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
                59,
                59,
            ),
        )

        assertEquals(
            actual = capturedMicroFrom,
            expected = 0,
        )
        assertEquals(
            actual = capturedMicroTo,
            expected = 999999999L,
        )
    }

    @Test
    @JsName("fn3")
    fun `Given generate is called with a predicate it returns a DateTimePeriod`() {
        // Given
        val expectedMacro = mutableListOf(
            2929, 5, 3, 7, 45, 13,
            2020, 5, 3, 7, 45, 13,
        )

        val expectedMicro = 42L

        val capturedMacroFrom = mutableListOf<Int>()
        var capturedMicroFrom: Long? = null

        val capturedMacroTo = mutableListOf<Int>()
        var capturedMicroTo: Long? = null

        intDependencyGenerator.generateWithRange = { from, to, _ ->
            capturedMacroFrom.add(from)
            capturedMacroTo.add(to)

            expectedMacro.removeFirst()
        }

        longDependencyGenerator.generateWithRange = { from, to, _ ->
            capturedMicroFrom = from
            capturedMicroTo = to

            expectedMicro
        }

        // When
        val actual = DateTimePeriodGenerator(
            intDependencyGenerator,
            longDependencyGenerator,
        ).generate { dateTimePeriod ->
            dateTimePeriod != DateTimePeriod(
                years = 2929,
                months = 5,
                days = 3,
                hours = 7,
                minutes = 45,
                seconds = 13,
                nanoseconds = 42L,
            )
        }

        // Then
        assertEquals(
            actual = actual,
            expected = DateTimePeriod(
                years = 2020,
                months = 5,
                days = 3,
                hours = 7,
                minutes = 45,
                seconds = 13,
                nanoseconds = 42L,
            ),
        )

        assertEquals(
            actual = capturedMacroFrom,
            expected = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        )
        assertEquals(
            actual = capturedMacroTo,
            expected = listOf(
                Int.MAX_VALUE,
                11,
                Int.MAX_VALUE,
                Int.MAX_VALUE,
                59,
                59,
                Int.MAX_VALUE,
                11,
                Int.MAX_VALUE,
                Int.MAX_VALUE,
                59,
                59,
            ),
        )

        assertEquals(
            actual = capturedMicroFrom,
            expected = 0,
        )
        assertEquals(
            actual = capturedMicroTo,
            expected = 999999999L,
        )
    }
}
