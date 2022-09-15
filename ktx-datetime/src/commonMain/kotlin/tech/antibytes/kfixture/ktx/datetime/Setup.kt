/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime

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
import tech.antibytes.kfixture.PublicApi
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

private val dependentGenerators = listOf(
    Instant::class to InstantGenerator,
    UtcOffset::class to UtcOffsetGenerator,
    DayOfWeek::class to DayOfWeekGenerator,
    Month::class to MonthGenerator,
    FixedOffsetTimeZone::class to FixedOffsetTimeZoneGenerator,
    DatePeriod::class to DatePeriodGenerator,
    DateTimePeriod::class to DateTimePeriodGenerator,
    LocalDate::class to LocalDateGenerator,
    LocalTime::class to LocalTimeGenerator,
    LocalDateTime::class to LocalDateTimeGenerator,
)

private fun PublicApi.Configuration.addDependentGenerators() {
    dependentGenerators.forEach {
        val (klass, generator) = it

        this.addGenerator(
            clazz = klass,
            factory = generator,
            qualifier = null,
        )
    }
}

public fun PublicApi.Configuration.useDateTimeWithoutTimeZones() {
    addGenerator(
        clazz = TimeZone::class,
        factory = ShallowTimeZoneGenerator,
        qualifier = null,
    )
    addDependentGenerators()
}

public fun PublicApi.Configuration.useDateTimeWithTimeZones() {
    addGenerator(
        clazz = TimeZone::class,
        factory = TimeZoneGenerator,
        qualifier = null,
    )
    addDependentGenerators()
}
