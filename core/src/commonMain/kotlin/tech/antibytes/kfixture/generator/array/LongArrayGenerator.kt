/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class LongArrayGenerator(
    random: Random,
    longGenerator: PublicApi.SignedNumberGenerator<Long, Long>,
) : PublicApi.SignedNumericArrayGenerator<Long, LongArray>,
    SignedArrayNumberGenerator<Long, LongArray>(random, longGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> Long,
    ): LongArray = LongArray(size, onEach)
}
