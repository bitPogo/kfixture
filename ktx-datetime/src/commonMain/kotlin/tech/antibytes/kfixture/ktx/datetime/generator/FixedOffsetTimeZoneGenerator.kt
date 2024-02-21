/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.random.Random
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.asTimeZone
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

internal class FixedOffsetTimeZoneGenerator(
    private val offsetGenerator: PublicApi.FilterableGenerator<UtcOffset, UtcOffset>,
) : FilterableGenerator<FixedOffsetTimeZone>() {
    override fun generate(): FixedOffsetTimeZone = offsetGenerator.generate().asTimeZone()

    companion object : PublicApi.DependentGeneratorFactory<FixedOffsetTimeZone> {
        @Suppress("UNCHECKED_CAST")
        override fun getInstance(
            random: Random,
            generators: Map<String, PublicApi.Generator<out Any>>,
        ): PublicApi.Generator<FixedOffsetTimeZone> {
            val offsetId = resolveGeneratorId(UtcOffset::class)

            return FixedOffsetTimeZoneGenerator(
                generators[offsetId] as PublicApi.FilterableGenerator<UtcOffset, UtcOffset>,
            )
        }
    }
}
