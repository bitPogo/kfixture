/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class UByteGenerator(
    private val random: Random,
) : PublicApi.RangedGenerator<UByte, UByte> {
    override fun generate(): UByte = generate(
        from = UByte.MIN_VALUE,
        to = UByte.MAX_VALUE,
    )

    override fun generate(predicate: (UByte) -> Boolean): UByte {
        TODO("Not yet implemented")
    }

    override fun generate(from: UByte, to: UByte, predicate: (UByte) -> Boolean): UByte {
        return random.nextInt(
            from = from.toInt(),
            until = to.toInt() + 1,
        ).toUByte()
    }
}
