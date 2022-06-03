/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.FixtureContract.ARRAY_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.ARRAY_UPPER_BOUND
import tech.antibytes.kfixture.FixtureContract.CHAR_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.CHAR_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class CharArrayGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<CharArray> {
    private fun generateCharArray(size: Int): CharArray {
        val fixture = CharArray(size)

        repeat(size) { idx ->
            fixture[idx] = random.access { it.nextInt(CHAR_LOWER_BOUND, CHAR_UPPER_BOUND).toChar() }
        }

        return fixture
    }

    override fun generate(): CharArray {
        val size = random.access { it.nextInt(ARRAY_LOWER_BOUND, ARRAY_UPPER_BOUND) }

        return generateCharArray(size)
    }
}
