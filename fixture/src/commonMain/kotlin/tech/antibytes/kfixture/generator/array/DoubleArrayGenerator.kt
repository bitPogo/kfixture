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

internal class DoubleArrayGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<DoubleArray> {
    private fun generateDoubleArray(size: Int): DoubleArray {
        val raw = random.access { it.nextBytes(size) }
        val fixture = DoubleArray(size)

        repeat(size) { idx ->
            fixture[idx] = raw[idx].toInt() + random.access { it.nextDouble() }
        }

        return fixture
    }

    override fun generate(): DoubleArray {
        val size = random.access { it.nextInt(ARRAY_LOWER_BOUND, ARRAY_UPPER_BOUND) }

        return generateDoubleArray(size)
    }
}
