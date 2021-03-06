/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.FixtureContract.ARRAY_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.ARRAY_UPPER_BOUND
import tech.antibytes.kfixture.FixtureContract.CHAR_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.CHAR_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi

internal class CharArrayGenerator(
    private val random: Random,
) : PublicApi.Generator<CharArray> {
    private fun generateCharArray(size: Int): CharArray {
        val fixture = CharArray(size)

        repeat(size) { idx ->
            fixture[idx] = random.nextInt(CHAR_LOWER_BOUND.code, CHAR_UPPER_BOUND.code).toChar()
        }

        return fixture
    }

    override fun generate(): CharArray {
        val size = random.nextInt(ARRAY_LOWER_BOUND, ARRAY_UPPER_BOUND)

        return generateCharArray(size)
    }
}
