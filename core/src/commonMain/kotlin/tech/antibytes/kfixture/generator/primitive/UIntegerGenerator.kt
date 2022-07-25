/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import kotlin.random.nextUInt
import tech.antibytes.kfixture.PublicApi

internal class UIntegerGenerator(
    private val random: Random,
) : PublicApi.RangedGenerator<UInt, UInt> {
    override fun generate(): UInt = random.nextUInt()

    override fun generate(from: UInt, to: UInt): UInt = random.nextUInt(UIntRange(from, to))
}
