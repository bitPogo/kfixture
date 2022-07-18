/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.FixtureContract.ARRAY_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.ARRAY_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi

internal class LongArrayGenerator(
    private val random: Random,
) : PublicApi.Generator<LongArray> {
    private fun generateLongArray(size: Int): LongArray {
        val raw = random.nextBytes(size)
        val fixture = LongArray(size)

        repeat(size) { idx ->
            fixture[idx] = raw[idx].toLong()
        }

        return fixture
    }

    override fun generate(): LongArray {
        val size = random.nextInt(ARRAY_LOWER_BOUND, ARRAY_UPPER_BOUND)

        return generateLongArray(size)
    }
}
