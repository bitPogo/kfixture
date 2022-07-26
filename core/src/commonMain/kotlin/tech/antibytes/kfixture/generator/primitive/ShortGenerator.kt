/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class ShortGenerator(
    private val random: Random,
) : PublicApi.SignedNumberGenerator<Short, Short>, Generator<Short>() {
    override fun generate(): Short = generate(Short.MIN_VALUE, Short.MAX_VALUE)

    override fun generate(
        predicate: (Short?) -> Boolean,
    ): Short = returnFilteredValue(predicate, ::generate)

    override fun generate(
        from: Short,
        to: Short,
        predicate: (Short?) -> Boolean,
    ): Short = returnFilteredValue(predicate) {
        random.nextInt(
            from = from.toInt(),
            until = to.toInt() + 1,
        ).toShort()
    }

    private fun resolveBoundary(sign: PublicApi.Sign): Pair<Short, Short> {
        return if (sign == PublicApi.Sign.POSITIVE) {
            ZERO to Short.MAX_VALUE
        } else {
            Short.MIN_VALUE to ZERO
        }
    }

    override fun generate(sign: PublicApi.Sign, predicate: (Short?) -> Boolean): Short {
        val (from, to) = resolveBoundary(sign)
        return generate(from, to, predicate)
    }

    private companion object {
        const val ZERO = 0.toShort()
    }
}
