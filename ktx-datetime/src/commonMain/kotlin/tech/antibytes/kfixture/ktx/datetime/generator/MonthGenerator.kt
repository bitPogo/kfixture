/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.random.Random
import kotlinx.datetime.Month
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.ktx.datetime.defaultPredicate
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

internal class MonthGenerator(
    private val monthGenerator: PublicApi.RangedGenerator<Int, Int>,
) : PublicApi.FilterableGenerator<Int, Month> {
    private val month = Month.values()
    override fun generate(): Month = generate(predicate = ::defaultPredicate)

    override fun generate(predicate: (Int?) -> Boolean): Month {
        // Note: It uses the ISO Days like the Month method
        val month = monthGenerator.generate(
            from = 1,
            to = 12,
            predicate = predicate,
        )

        return this.month[month - 1]
    }

    companion object : PublicApi.DependentGeneratorFactory<Month> {
        @Suppress("UNCHECKED_CAST")
        override fun getInstance(
            random: Random,
            generators: Map<String, PublicApi.Generator<out Any>>,
        ): PublicApi.Generator<Month> {
            val id = resolveGeneratorId(Int::class)

            return MonthGenerator(
                monthGenerator = generators[id] as PublicApi.RangedGenerator<Int, Int>,
            )
        }
    }
}
