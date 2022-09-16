/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.random.Random
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.ktx.datetime.KtxDateTimeContract
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

internal class LocalDateTimeGenerator(
    private val instantGenerator: PublicApi.Generator<Instant>,
    private val timeZoneGenerator: PublicApi.Generator<TimeZone>,
) : KtxDateTimeContract.LocalizedDateTimeGenerator<LocalDateTime> {
    override fun generate(): LocalDateTime = generate(null, null)

    override fun generate(
        instantGenerator: Function0<Instant>?,
        timeZoneGenerator: Function0<TimeZone>?,
    ): LocalDateTime {
        val timeZone = timeZoneGenerator?.invoke() ?: this.timeZoneGenerator.generate()

        return (instantGenerator?.invoke() ?: this.instantGenerator.generate())
            .toLocalDateTime(timeZone)
    }

    companion object : PublicApi.DependentGeneratorFactory<LocalDateTime> {
        @Suppress("UNCHECKED_CAST")
        override fun getInstance(
            random: Random,
            generators: Map<String, PublicApi.Generator<out Any>>,
        ): PublicApi.Generator<LocalDateTime> {
            val instantId = resolveGeneratorId(Instant::class)
            val timeZoneId = resolveGeneratorId(TimeZone::class)

            return LocalDateTimeGenerator(
                instantGenerator = generators[instantId] as PublicApi.Generator<Instant>,
                timeZoneGenerator = generators[timeZoneId] as PublicApi.Generator<TimeZone>,
            )
        }
    }
}
