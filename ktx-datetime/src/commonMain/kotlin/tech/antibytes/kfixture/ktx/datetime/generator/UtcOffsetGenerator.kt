/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.math.abs
import kotlin.random.Random
import kotlinx.datetime.UtcOffset
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.ktx.datetime.DateTimeContract.MAX_HOUR_OFFSET
import tech.antibytes.kfixture.ktx.datetime.DateTimeContract.MAX_MINUTES_SECONDS
import tech.antibytes.kfixture.ktx.datetime.DateTimeContract.MIN_TIME_OFFSET
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

internal class UtcOffsetGenerator(
    private val random: Random,
    private val offsetGenerator: PublicApi.RangedGenerator<Int, Int>,
) : FilterableGenerator<UtcOffset>() {
    private fun selectHours(): Int = offsetGenerator.generate(from = MIN_TIME_OFFSET, to = MAX_HOUR_OFFSET)
    private fun selectMinutes(): Int = offsetGenerator.generate(from = MIN_TIME_OFFSET, to = MAX_MINUTES_SECONDS)
    private fun selectSeconds(): Int = offsetGenerator.generate(from = MIN_TIME_OFFSET, to = MAX_MINUTES_SECONDS)

    private fun getNullableTime(generate: Function0<Int>): Int? {
        return if (random.nextBoolean()) {
            generate()
        } else {
            null
        }
    }

    private fun determineSign(): Int {
        return if (random.nextBoolean()) {
            1
        } else {
            -1
        }
    }

    private fun Int?.isMaxHour(): Boolean = abs(this ?: 0) >= MAX_HOUR_OFFSET

    override fun generate(): UtcOffset {
        val sign = determineSign()
        val hour = getNullableTime { selectHours() * sign }

        return if (hour.isMaxHour()) {
            UtcOffset(hours = hour)
        } else {
            UtcOffset(
                hours = hour,
                minutes = getNullableTime { selectMinutes() * sign },
                seconds = getNullableTime { selectSeconds() * sign },
            )
        }
    }

    companion object : PublicApi.DependentGeneratorFactory<UtcOffset> {
        @Suppress("UNCHECKED_CAST")
        override fun getInstance(
            random: Random,
            generators: Map<String, PublicApi.Generator<out Any>>,
        ): PublicApi.Generator<UtcOffset> {
            val offsetGeneratorId = resolveGeneratorId(Int::class)

            return UtcOffsetGenerator(
                random = random,
                offsetGenerator = generators[offsetGeneratorId] as PublicApi.RangedGenerator<Int, Int>,
            )
        }
    }
}
