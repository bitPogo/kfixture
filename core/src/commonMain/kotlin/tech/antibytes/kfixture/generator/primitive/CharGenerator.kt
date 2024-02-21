/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("OPT_IN_USAGE")

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import tech.antibytes.kfixture.FixtureContract.CHAR_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.CHAR_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi

internal class CharGenerator(
    private val random: Random,
) : PublicApi.RangedGenerator<Char, Char>, Generator<Char>() {
    override fun generate(): Char = generate(CHAR_LOWER_BOUND, CHAR_UPPER_BOUND)

    override fun generate(predicate: (Char?) -> Boolean): Char = returnFilteredValue(predicate, ::generate)

    override fun generate(
        from: Char,
        to: Char,
        predicate: (Char?) -> Boolean,
    ): Char = returnFilteredValue(predicate) { random.nextInt(from.code, to.code + 1).toChar() }
}
