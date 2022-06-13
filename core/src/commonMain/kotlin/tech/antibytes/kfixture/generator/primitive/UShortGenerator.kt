/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class UShortGenerator(
    val random: Random
) : PublicApi.Generator<UShort> {
    override fun generate(): UShort = random.nextInt().toUShort()
}
