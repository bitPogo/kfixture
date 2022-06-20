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

internal class BooleanArrayGenerator(
    private val random: Random
) : PublicApi.Generator<BooleanArray> {
    override fun generate(): BooleanArray {
        val size = random.nextInt(ARRAY_LOWER_BOUND, ARRAY_UPPER_BOUND)
        val array = BooleanArray(size)

        repeat(size) { idx ->
            array[idx] = random.nextBoolean()
        }

        return array
    }
}
