/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class LongArrayGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<LongArray> {
    private fun generateLongArray(size: Int): LongArray {
        val raw = random.access { it.nextBytes(size) }
        val fixture = LongArray(size)

        repeat(size) { idx ->
            fixture[idx] = raw[idx].toLong()
        }

        return fixture
    }

    override fun generate(): LongArray {
        val size = random.access { it.nextInt(1, 100) }

        return generateLongArray(size)
    }
}
