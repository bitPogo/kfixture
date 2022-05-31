/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class UShortGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<UShort> {
    override fun generate(): UShort = random.access { it.nextInt().toUShort() }
}
