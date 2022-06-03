/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.FixtureContract.ARRAY_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.ARRAY_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random
import kotlin.random.nextUBytes

internal class UIntArrayGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<UIntArray> {
    private fun generateUIntArray(size: Int): UIntArray {
        val raw = random.access { it.nextUBytes(size) }
        val fixture = UIntArray(size)

        repeat(size) { idx ->
            fixture[idx] = raw[idx].toUInt()
        }

        return fixture
    }

    override fun generate(): UIntArray {
        val size = random.access { it.nextInt(ARRAY_LOWER_BOUND, ARRAY_UPPER_BOUND) }

        return generateUIntArray(size)
    }
}
