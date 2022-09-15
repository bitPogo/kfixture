/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.random.Random
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.ktx.datetime.DateTimeContract.LocalizedDateTimeGenerator
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

internal class LocalDateGenerator(
    private val dateTimeGenerator: LocalizedDateTimeGenerator<LocalDateTime>,
) : LocalizedDateTimeGenerator<LocalDate> {
    override fun generate(): LocalDate = generate(null, null)

    override fun generate(
        instantGenerator: Function0<Instant>?,
        timeZoneGenerator: Function0<TimeZone>?,
    ): LocalDate {
        return dateTimeGenerator.generate(
            instantGenerator = instantGenerator,
            timeZoneGenerator = timeZoneGenerator,
        ).date
    }

    companion object : PublicApi.DependentGeneratorFactory<LocalDate> {
        @Suppress("UNCHECKED_CAST")
        override fun getInstance(
            random: Random,
            generators: Map<String, PublicApi.Generator<out Any>>,
        ): PublicApi.Generator<LocalDate> {
            val localDateTimeId = resolveGeneratorId(LocalDateTime::class)

            return LocalDateGenerator(
                dateTimeGenerator = generators[localDateTimeId] as LocalizedDateTimeGenerator<LocalDateTime>,
            )
        }
    }
}
