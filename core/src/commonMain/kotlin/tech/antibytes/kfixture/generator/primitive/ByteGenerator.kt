/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.Byte.Companion.MAX_VALUE
import kotlin.Byte.Companion.MIN_VALUE
import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class ByteGenerator(
    private val random: Random,
) : PublicApi.SignedNumberGenerator<Byte, Byte> {
    override fun generate(): Byte = generate(MIN_VALUE, MAX_VALUE)

    override fun generate(from: Byte, to: Byte): Byte {
        return random.nextInt(
            from = from.toInt(),
            until = to.toInt() + 1,
        ).toByte()
    }

    private fun resolveBoundary(sign: PublicApi.Sign): Pair<Byte, Byte> {
        return if (sign == PublicApi.Sign.POSITIVE) {
            ZERO to MAX_VALUE
        } else {
            MIN_VALUE to ZERO
        }
    }

    override fun generate(sign: PublicApi.Sign): Byte {
        val (from, to) = resolveBoundary(sign)
        return generate(from, to)
    }

    private companion object {
        const val ZERO = 0.toByte()
    }
}
