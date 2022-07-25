/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class UIntArrayGenerator(
    random: Random,
    uIntGenerator: PublicApi.RangedGenerator<UInt, UInt>,
) : RangedArrayNumberGenerator<UInt, UIntArray>(random, uIntGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> UInt,
    ): UIntArray = UIntArray(size, onEach)
}
