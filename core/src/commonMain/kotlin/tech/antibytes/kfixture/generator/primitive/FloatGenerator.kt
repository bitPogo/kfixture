/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class FloatGenerator(
    private val random: Random,
) : PublicApi.Generator<Float> {
    override fun generate(): Float = random.nextFloat() + random.nextInt()
}
