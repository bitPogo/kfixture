/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class UShortArrayGenerator(
    random: Random,
    uShortGenerator: PublicApi.RangedGenerator<UShort, UShort>,
) : RangedArrayNumberGenerator<UShort, UShortArray>(random, uShortGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> UShort,
    ): UShortArray = UShortArray(size, onEach)
}