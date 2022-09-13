/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import tech.antibytes.kfixture.PublicApi
import kotlinx.datetime.DayOfWeek
import tech.antibytes.kfixture.ktx.datetime.defaultPredicate
import tech.antibytes.kfixture.qualifier.resolveGeneratorId
import kotlin.random.Random

internal class DayOfWeekGenerator(
    private val dayGenerator: PublicApi.RangedGenerator<Int, Int>
) : PublicApi.FilterableGenerator<Int, DayOfWeek> {
    private val daysOfWeek = DayOfWeek.values()
    override fun generate(): DayOfWeek = generate(predicate = ::defaultPredicate)

    override fun generate(predicate: (Int?) -> Boolean): DayOfWeek {
        // Note: It uses the ISO Days like the DayOfWeek method
        val day = dayGenerator.generate(
            from = 1,
            to = 7,
            predicate = predicate
        )

        return daysOfWeek[day - 1]
    }

    companion object : PublicApi.DependentGeneratorFactory<DayOfWeek> {
        @Suppress("UNCHECKED_CAST")
        override fun getInstance(
            random: Random,
            generators: Map<String, PublicApi.Generator<out Any>>
        ): PublicApi.Generator<DayOfWeek> {
            val id = resolveGeneratorId(Int::class)

            return DayOfWeekGenerator(
                dayGenerator = generators[id] as PublicApi.RangedGenerator<Int, Int>,
            )
        }
    }
}
