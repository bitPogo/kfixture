/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.random.Random
import kotlinx.datetime.DateTimePeriod
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.ktx.datetime.DateTimeContract.MAX_MINUTES_SECONDS
import tech.antibytes.kfixture.ktx.datetime.DateTimeContract.MAX_MONTH
import tech.antibytes.kfixture.ktx.datetime.DateTimeContract.MAX_NANOSECONDS
import tech.antibytes.kfixture.ktx.datetime.DateTimeContract.MIN_TIME_PERIOD
import tech.antibytes.kfixture.ktx.datetime.DateTimeContract.MIN_TIME_PERIOD_L
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

internal class DateTimePeriodGenerator(
    private val macroTimeGenerator: PublicApi.RangedGenerator<Int, Int>,
    private val microTimeGenerator: PublicApi.RangedGenerator<Long, Long>,
) : FilterableGenerator<DateTimePeriod>() {
    private fun selectYears(): Int = macroTimeGenerator.generate(from = MIN_TIME_PERIOD, to = Int.MAX_VALUE)
    private fun selectMonths(): Int = macroTimeGenerator.generate(from = MIN_TIME_PERIOD, to = MAX_MONTH)
    private fun selectDays(): Int = macroTimeGenerator.generate(from = MIN_TIME_PERIOD, to = Int.MAX_VALUE)
    private fun selectHours(): Int = macroTimeGenerator.generate(from = MIN_TIME_PERIOD, to = Int.MAX_VALUE)
    private fun selectMinutes(): Int = macroTimeGenerator.generate(from = MIN_TIME_PERIOD, to = MAX_MINUTES_SECONDS)
    private fun selectSeconds(): Int = macroTimeGenerator.generate(from = MIN_TIME_PERIOD, to = MAX_MINUTES_SECONDS)
    private fun selectNanoSeconds(): Long = microTimeGenerator.generate(from = MIN_TIME_PERIOD_L, to = MAX_NANOSECONDS)

    override fun generate(): DateTimePeriod {
        return DateTimePeriod(
            years = selectYears(),
            months = selectMonths(),
            days = selectDays(),
            hours = selectHours(),
            minutes = selectMinutes(),
            seconds = selectSeconds(),
            nanoseconds = selectNanoSeconds(),
        )
    }

    companion object : PublicApi.DependentGeneratorFactory<DateTimePeriod> {
        @Suppress("UNCHECKED_CAST")
        override fun getInstance(
            random: Random,
            generators: Map<String, PublicApi.Generator<out Any>>,
        ): PublicApi.Generator<DateTimePeriod> {
            val macroId = resolveGeneratorId(Int::class)
            val microId = resolveGeneratorId(Long::class)

            return DateTimePeriodGenerator(
                microTimeGenerator = generators[microId] as PublicApi.RangedGenerator<Long, Long>,
                macroTimeGenerator = generators[macroId] as PublicApi.RangedGenerator<Int, Int>,
            )
        }
    }
}
