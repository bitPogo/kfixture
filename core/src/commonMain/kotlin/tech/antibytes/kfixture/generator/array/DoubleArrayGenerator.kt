/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class DoubleArrayGenerator(
    random: Random,
    doubleGenerator: PublicApi.SignedNumberGenerator<Double, Double>,
) : SignedArrayNumberGenerator<Double, DoubleArray>(random, doubleGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> Double,
    ): DoubleArray = DoubleArray(size, onEach)
}
