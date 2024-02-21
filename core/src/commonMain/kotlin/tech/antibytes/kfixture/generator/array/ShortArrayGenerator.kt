/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class ShortArrayGenerator(
    random: Random,
    shortGenerator: PublicApi.SignedNumberGenerator<Short, Short>,
) : PublicApi.SignedNumericArrayGenerator<Short, ShortArray>,
    SignedArrayNumberGenerator<Short, ShortArray>(random, shortGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> Short,
    ): ShortArray = ShortArray(size, onEach)
}
