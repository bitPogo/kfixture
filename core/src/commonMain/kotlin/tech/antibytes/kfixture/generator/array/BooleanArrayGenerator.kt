/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class BooleanArrayGenerator(
    random: Random,
    booleanGenerator: PublicApi.Generator<Boolean>,
) : ArrayGenerator<Boolean, BooleanArray>(random, booleanGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> Boolean,
    ): BooleanArray = BooleanArray(size, onEach)
}
