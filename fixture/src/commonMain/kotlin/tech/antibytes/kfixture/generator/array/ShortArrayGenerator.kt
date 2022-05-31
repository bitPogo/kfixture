/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class ShortArrayGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<ShortArray> {
    private fun generateIntArray(size: Int): ShortArray {
        val raw = random.access { it.nextBytes(size) }
        val fixture = ShortArray(size)

        repeat(size) { idx ->
            fixture[idx] = raw[idx].toShort()
        }

        return fixture
    }

    override fun generate(): ShortArray {
        val size = random.access { it.nextInt(1, 100) }
        
        return generateIntArray(size)
    }
}
