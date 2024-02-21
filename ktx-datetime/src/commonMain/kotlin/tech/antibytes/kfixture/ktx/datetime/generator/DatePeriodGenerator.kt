/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.random.Random
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.ktx.datetime.DateTimeContract.MAX_MONTH
import tech.antibytes.kfixture.ktx.datetime.DateTimeContract.MIN_TIME_PERIOD
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

internal class DatePeriodGenerator(
    private val macroTimeGenerator: PublicApi.RangedGenerator<Int, Int>,
) : FilterableGenerator<DateTimePeriod>() {
    private fun selectYears(): Int = macroTimeGenerator.generate(from = MIN_TIME_PERIOD, to = Int.MAX_VALUE)
    private fun selectMonths(): Int = macroTimeGenerator.generate(from = MIN_TIME_PERIOD, to = MAX_MONTH)
    private fun selectDays(): Int = macroTimeGenerator.generate(from = MIN_TIME_PERIOD, to = Int.MAX_VALUE)

    override fun generate(): DateTimePeriod {
        return DatePeriod(
            years = selectYears(),
            months = selectMonths(),
            days = selectDays(),
        )
    }

    companion object : PublicApi.DependentGeneratorFactory<DateTimePeriod> {
        @Suppress("UNCHECKED_CAST")
        override fun getInstance(
            random: Random,
            generators: Map<String, PublicApi.Generator<out Any>>,
        ): PublicApi.Generator<DateTimePeriod> {
            val macroId = resolveGeneratorId(Int::class)

            return DatePeriodGenerator(
                macroTimeGenerator = generators[macroId] as PublicApi.RangedGenerator<Int, Int>,
            )
        }
    }
}
