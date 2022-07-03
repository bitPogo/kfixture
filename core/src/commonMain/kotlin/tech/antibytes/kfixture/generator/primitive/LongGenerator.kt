/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class LongGenerator(
    private val random: Random,
) : PublicApi.Generator<Long> {
    override fun generate(): Long = random.nextLong()
}
