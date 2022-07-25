/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class IntArrayGenerator(
    random: Random,
    intGenerator: PublicApi.SignedNumberGenerator<Int, Int>,
) : PublicApi.SignedNumericArrayGenerator<Int, IntArray>,
    SignedArrayNumberGenerator<Int, IntArray>(random, intGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> Int,
    ): IntArray = IntArray(size, onEach)
}
