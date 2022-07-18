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

internal class DoubleArrayGenerator(
    private val random: Random,
) : PublicApi.Generator<DoubleArray> {
    private fun generateDoubleArray(size: Int): DoubleArray {
        val raw = random.nextBytes(size)
        val fixture = DoubleArray(size)

        repeat(size) { idx ->
            fixture[idx] = raw[idx].toInt() + random.nextDouble()
        }

        return fixture
    }

    override fun generate(): DoubleArray {
        val size = random.nextInt(ARRAY_LOWER_BOUND, ARRAY_UPPER_BOUND)

        return generateDoubleArray(size)
    }
}
