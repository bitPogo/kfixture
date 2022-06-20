/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import tech.antibytes.kfixture.FixtureContract.ARRAY_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.ARRAY_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class ShortArrayGenerator(
    private val random: Random
) : PublicApi.Generator<ShortArray> {
    private fun generateShortArray(size: Int): ShortArray {
        val raw = random.nextBytes(size)
        val fixture = ShortArray(size)

        repeat(size) { idx ->
            fixture[idx] = raw[idx].toShort()
        }

        return fixture
    }

    override fun generate(): ShortArray {
        val size = random.nextInt(ARRAY_LOWER_BOUND, ARRAY_UPPER_BOUND)

        return generateShortArray(size)
    }
}
