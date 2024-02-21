/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.random.Random
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.ktx.datetime.KtxDateTimeContract.LocalizedDateTimeGenerator
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

internal class LocalTimeGenerator(
    private val dateTimeGenerator: LocalizedDateTimeGenerator<LocalDateTime>,
) : LocalizedDateTimeGenerator<LocalTime> {
    override fun generate(): LocalTime = generate(null, null)

    override fun generate(
        instantGenerator: Function0<Instant>?,
        timeZoneGenerator: Function0<TimeZone>?,
    ): LocalTime {
        return dateTimeGenerator.generate(
            instantGenerator = instantGenerator,
            timeZoneGenerator = timeZoneGenerator,
        ).time
    }

    companion object : PublicApi.DependentGeneratorFactory<LocalTime> {
        @Suppress("UNCHECKED_CAST")
        override fun getInstance(
            random: Random,
            generators: Map<String, PublicApi.Generator<out Any>>,
        ): PublicApi.Generator<LocalTime> {
            val localDateTimeId = resolveGeneratorId(LocalDateTime::class)

            return LocalTimeGenerator(
                dateTimeGenerator = generators[localDateTimeId] as LocalizedDateTimeGenerator<LocalDateTime>,
            )
        }
    }
}
