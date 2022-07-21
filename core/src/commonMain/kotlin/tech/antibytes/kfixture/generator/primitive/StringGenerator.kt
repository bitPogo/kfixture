/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import tech.antibytes.kfixture.FixtureContract.CHAR_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.CHAR_UPPER_BOUND
import tech.antibytes.kfixture.FixtureContract.STRING_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.STRING_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi

internal class StringGenerator(
    private val random: Random,
) : PublicApi.Generator<String> {
    override fun generate(): String {
        val length = random.nextInt(STRING_LOWER_BOUND, STRING_UPPER_BOUND)
        val chars = ByteArray(length)

        for (idx in 0 until length) {
            chars[idx] = random.nextInt(CHAR_LOWER_BOUND.code, CHAR_UPPER_BOUND.code).toByte()
        }

        return chars.decodeToString()
    }
}
