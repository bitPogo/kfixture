/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random
import kotlin.random.nextUInt

internal class UIntegerGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<UInt> {
    override fun generate(): UInt = random.access { it.nextUInt() }
}
