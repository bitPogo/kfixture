/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.test.fixture.generator.primitive

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.util.test.fixture.PublicApi
import kotlin.random.Random

class LongGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<Long> {
    override fun generate(): Long = random.access { it.nextLong() }
}
