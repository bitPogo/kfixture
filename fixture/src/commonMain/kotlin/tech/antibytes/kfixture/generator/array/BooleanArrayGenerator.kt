/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class BooleanArrayGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<BooleanArray> {
    override fun generate(): BooleanArray {
        val size = random.access { it.nextInt(1, 100) }
        val array = BooleanArray(size)

        repeat(size) { idx ->
            array[idx] = random.access { it.nextBoolean() }
        }

        return array
    }
}
