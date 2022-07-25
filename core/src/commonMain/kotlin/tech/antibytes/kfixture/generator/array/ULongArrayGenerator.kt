/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class ULongArrayGenerator(
    random: Random,
    uLongGenerator: PublicApi.RangedGenerator<ULong, ULong>,
) : RangedArrayNumberGenerator<ULong, ULongArray>(random, uLongGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> ULong,
    ): ULongArray = ULongArray(size, onEach)
}
