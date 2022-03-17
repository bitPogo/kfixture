/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.test.fixture.generator.array

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.util.test.fixture.PublicApi
import kotlin.random.Random

internal class ByteArrayGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<ByteArray> {
    override fun generate(): ByteArray {
        val size = random.access { it.nextInt(1, 100) }
        return random.access { it.nextBytes(size) }
    }
}
