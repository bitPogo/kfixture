/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.FixtureContract.Companion.ARRAY_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.Companion.ARRAY_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random
import kotlin.random.nextUBytes

internal class UByteArrayGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<UByteArray> {
    override fun generate(): UByteArray {
        val size = random.access { it.nextInt(ARRAY_LOWER_BOUND, ARRAY_UPPER_BOUND) }
        return random.access { it.nextUBytes(size) }
    }
}
