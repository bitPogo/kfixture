/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import kotlin.random.nextUBytes
import tech.antibytes.kfixture.FixtureContract.ARRAY_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.ARRAY_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi

internal class UIntArrayGenerator(
    private val random: Random,
) : PublicApi.Generator<UIntArray> {
    private fun generateUIntArray(size: Int): UIntArray {
        val raw = random.nextUBytes(size)
        val fixture = UIntArray(size)

        repeat(size) { idx ->
            fixture[idx] = raw[idx].toUInt()
        }

        return fixture
    }

    override fun generate(): UIntArray {
        val size = random.nextInt(ARRAY_LOWER_BOUND, ARRAY_UPPER_BOUND)

        return generateUIntArray(size)
    }
}
