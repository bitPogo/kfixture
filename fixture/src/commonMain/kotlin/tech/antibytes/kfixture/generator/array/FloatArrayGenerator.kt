/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class FloatArrayGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<FloatArray> {
    private fun generateFloatArray(size: Int): FloatArray {
        val raw = random.access { it.nextBytes(size) }
        val fixture = FloatArray(size)

        repeat(size) { idx ->
            fixture[idx] = raw[idx].toInt() + random.access { it.nextFloat() }
        }

        return fixture
    }

    override fun generate(): FloatArray {
        val size = random.access { it.nextInt(1, 100) }

        return generateFloatArray(size)
    }
}
