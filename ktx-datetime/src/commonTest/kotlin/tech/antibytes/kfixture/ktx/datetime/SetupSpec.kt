/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime

import kotlin.js.JsName
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import tech.antibytes.kfixture.PublicApi.DependentGeneratorFactory
import tech.antibytes.kfixture.PublicApi.GeneratorFactory
import tech.antibytes.kfixture.ktx.datetime.generator.DatePeriodGenerator
import tech.antibytes.kfixture.ktx.datetime.generator.DateTimePeriodGenerator
import tech.antibytes.kfixture.ktx.datetime.generator.DayOfWeekGenerator
import tech.antibytes.kfixture.ktx.datetime.generator.FixedOffsetTimeZoneGenerator
import tech.antibytes.kfixture.ktx.datetime.generator.InstantGenerator
import tech.antibytes.kfixture.ktx.datetime.generator.LocalDateGenerator
import tech.antibytes.kfixture.ktx.datetime.generator.LocalDateTimeGenerator
import tech.antibytes.kfixture.ktx.datetime.generator.LocalTimeGenerator
import tech.antibytes.kfixture.ktx.datetime.generator.MonthGenerator
import tech.antibytes.kfixture.ktx.datetime.generator.ShallowTimeZoneGenerator
import tech.antibytes.kfixture.ktx.datetime.generator.TimeZoneGenerator
import tech.antibytes.kfixture.ktx.datetime.generator.UtcOffsetGenerator
import tech.antibytes.kfixture.mock.ConfigurationStub

class SetupSpec {
    @Test
    @JsName("fn0")
    fun `Given useDateTimeWithoutTimeZones it adds the KTX DateTime Generators`() {
        // Given
        val configuration = ConfigurationStub()

        val capturedTimeZoneGenerator: MutableList<Pair<KClass<out Any>, GeneratorFactory<out Any>>> = mutableListOf()
        val capturedDependentGenerators: MutableList<Pair<KClass<out Any>, DependentGeneratorFactory<out Any>>> = mutableListOf()

        configuration.addGenerator = { klass, factory, _ ->
            capturedTimeZoneGenerator.add(klass to factory)
        }
        configuration.addDependentGenerator = { klass, factory, _ ->
            capturedDependentGenerators.add(klass to factory)
        }

        // When
        configuration.useDateTimeWithOnlyUtc()

        // Then
        assertEquals(
            expected = listOf<Pair<KClass<out Any>, GeneratorFactory<out Any>>>(
                TimeZone::class to ShallowTimeZoneGenerator,
            ),
            actual = capturedTimeZoneGenerator,
        )
        assertEquals(
            expected = listOf(
                Instant::class to InstantGenerator,
                UtcOffset::class to UtcOffsetGenerator,
                DayOfWeek::class to DayOfWeekGenerator,
                Month::class to MonthGenerator,
                FixedOffsetTimeZone::class to FixedOffsetTimeZoneGenerator,
                DatePeriod::class to DatePeriodGenerator,
                DateTimePeriod::class to DateTimePeriodGenerator,
                LocalDateTime::class to LocalDateTimeGenerator,
                LocalDate::class to LocalDateGenerator,
                LocalTime::class to LocalTimeGenerator,
            ),
            actual = capturedDependentGenerators,
        )
    }

    @Test
    @JsName("fn1")
    fun `Given useDateTimeWithTimeZones it adds the KTX DateTime Generators`() {
        // Given
        val configuration = ConfigurationStub()

        val capturedTimeZoneGenerator: MutableList<Pair<KClass<out Any>, GeneratorFactory<out Any>>> = mutableListOf()
        val capturedDependentGenerators: MutableList<Pair<KClass<out Any>, DependentGeneratorFactory<out Any>>> = mutableListOf()

        configuration.addGenerator = { klass, factory, _ ->
            capturedTimeZoneGenerator.add(klass to factory)
        }
        configuration.addDependentGenerator = { klass, factory, _ ->
            capturedDependentGenerators.add(klass to factory)
        }

        // When
        configuration.useDateTimeWithTimeZones()

        // Then
        assertEquals(
            expected = listOf<Pair<KClass<out Any>, GeneratorFactory<out Any>>>(
                TimeZone::class to TimeZoneGenerator,
            ),
            actual = capturedTimeZoneGenerator,
        )
        assertEquals(
            expected = listOf(
                Instant::class to InstantGenerator,
                UtcOffset::class to UtcOffsetGenerator,
                DayOfWeek::class to DayOfWeekGenerator,
                Month::class to MonthGenerator,
                FixedOffsetTimeZone::class to FixedOffsetTimeZoneGenerator,
                DatePeriod::class to DatePeriodGenerator,
                DateTimePeriod::class to DateTimePeriodGenerator,
                LocalDateTime::class to LocalDateTimeGenerator,
                LocalDate::class to LocalDateGenerator,
                LocalTime::class to LocalTimeGenerator,
            ),
            actual = capturedDependentGenerators,
        )
    }
}
