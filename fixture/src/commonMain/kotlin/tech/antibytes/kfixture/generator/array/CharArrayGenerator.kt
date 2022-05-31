/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class CharArrayGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<CharArray> {
    private fun generateCharArray(size: Int): CharArray {
        val fixture = CharArray(size)

        repeat(size) { idx ->
            fixture[idx] = random.access { it.nextInt(33, 126).toChar() }
        }

        return fixture
    }

    override fun generate(): CharArray {
        val size = random.access { it.nextInt(1, 100) }

        return generateCharArray(size)
    }
}
