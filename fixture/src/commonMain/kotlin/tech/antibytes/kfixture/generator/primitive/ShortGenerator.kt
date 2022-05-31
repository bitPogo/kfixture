/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class ShortGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<Short> {
    override fun generate(): Short = random.access { it.nextInt().toShort() }
}
