/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.random.Random
import kotlinx.datetime.TimeZone
import tech.antibytes.kfixture.PublicApi

internal class ShallowTimeZoneGenerator : PublicApi.FilterableGenerator<TimeZone, TimeZone> {

    override fun generate(): TimeZone = TimeZone.UTC

    override fun generate(predicate: (TimeZone?) -> Boolean): TimeZone = generate()

    companion object : PublicApi.GeneratorFactory<TimeZone> {
        override fun getInstance(
            random: Random,
        ): PublicApi.Generator<TimeZone> = ShallowTimeZoneGenerator()
    }
}
