/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.random.Random
import kotlinx.datetime.TimeZone
import tech.antibytes.kfixture.PublicApi

internal class TimeZoneGenerator(
    private val random: Random,
) : FilterableGenerator<TimeZone>() {
    private val zones = TimeZone.availableZoneIds.toList()

    override fun generate(): TimeZone {
        val zone = random.nextInt(
            from = 0,
            until = zones.size,
        )

        return TimeZone.of(zones[zone])
    }

    companion object : PublicApi.GeneratorFactory<TimeZone> {
        override fun getInstance(
            random: Random,
        ): PublicApi.Generator<TimeZone> = TimeZoneGenerator(random)
    }
}
