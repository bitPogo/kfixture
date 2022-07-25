/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class StringGenerator(
    random: Random,
    charGenerator: PublicApi.RangedGenerator<Char, Char>,
) : PublicApi.RangedArrayGenerator<Char, String>,
    RangedArrayNumberGenerator<Char, String>(random, charGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> Char,
    ): String = CharArray(size, onEach).concatToString()
}
