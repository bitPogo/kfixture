/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class CharArrayGenerator(
    random: Random,
    charGenerator: PublicApi.RangedGenerator<Char, Char>,
) : PublicApi.RangedArrayGenerator<Char, CharArray>,
    RangedArrayNumberGenerator<Char, CharArray>(random, charGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> Char,
    ): CharArray = CharArray(size, onEach)

    override fun generate(from: Char, to: Char, predicate: (Char) -> Boolean): CharArray {
        TODO("Not yet implemented")
    }
}
