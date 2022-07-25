/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import tech.antibytes.kfixture.FixtureContract.CHAR_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.CHAR_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi

internal class CharGenerator(
    private val random: Random,
) : PublicApi.RangedGenerator<Char, Char> {
    override fun generate(): Char = generate(CHAR_LOWER_BOUND, CHAR_UPPER_BOUND)
    override fun generate(predicate: (Char) -> Boolean): Char {
        TODO("Not yet implemented")
    }

    override fun generate(from: Char, to: Char, predicate: (Char) -> Boolean): Char = random.nextInt(from.code, to.code).toChar()
}
