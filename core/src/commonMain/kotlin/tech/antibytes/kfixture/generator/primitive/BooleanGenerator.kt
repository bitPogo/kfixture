/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class BooleanGenerator(
    private val random: Random,
) : PublicApi.Generator<Boolean> {
    override fun generate(): Boolean = random.nextBoolean()
}
