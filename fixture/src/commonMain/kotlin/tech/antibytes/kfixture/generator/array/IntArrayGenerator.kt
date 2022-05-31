/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class IntArrayGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<IntArray> {
    private fun generateIntArray(size: Int): IntArray {
        val raw = random.access { it.nextBytes(size) }
        val fixture = IntArray(size)

        repeat(size) { idx ->
            fixture[idx] = raw[idx].toInt()
        }

        return fixture
    }

    override fun generate(): IntArray {
        val size = random.access { it.nextInt(1, 100) }

        return generateIntArray(size)
    }
}
