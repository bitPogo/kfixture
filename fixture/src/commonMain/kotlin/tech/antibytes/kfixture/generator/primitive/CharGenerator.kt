/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import tech.antibytes.kfixture.FixtureContract.CHAR_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.CHAR_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class CharGenerator(
    val random: Random
) : PublicApi.Generator<Char> {
    override fun generate(): Char = random.nextInt(CHAR_LOWER_BOUND, CHAR_UPPER_BOUND).toChar()
}
