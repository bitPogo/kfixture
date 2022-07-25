/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class FloatArrayGenerator(
    random: Random,
    floatGenerator: PublicApi.RangedGenerator<Float, Float>,
) : RangedArrayNumberGenerator<Float, FloatArray>(random, floatGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> Float,
    ): FloatArray = FloatArray(size, onEach)

    override fun generate(from: Float, to: Float, predicate: (Float) -> Boolean): FloatArray {
        TODO("Not yet implemented")
    }
}
