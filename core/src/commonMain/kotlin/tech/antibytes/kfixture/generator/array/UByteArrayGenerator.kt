/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class UByteArrayGenerator(
    random: Random,
    uByteArrayGenerator: PublicApi.RangedGenerator<UByte, UByte>,
) : RangedArrayNumberGenerator<UByte, UByteArray>(random, uByteArrayGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> UByte,
    ): UByteArray = UByteArray(size, onEach)
}
