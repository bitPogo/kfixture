/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
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
) : PublicApi.SignedNumberGenerator<Byte, Byte>, Generator<Byte>() {
    override fun generate(): Byte = generate(MIN_VALUE, MAX_VALUE)

    override fun generate(
        predicate: (Byte?) -> Boolean,
    ): Byte = returnFilteredValue(predicate, ::generate)

    override fun generate(
        from: Byte,
        to: Byte,
        predicate: (Byte?) -> Boolean,
    ): Byte = returnFilteredValue(predicate) {
        random.nextInt(
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

    override fun generate(
        sign: PublicApi.Sign,
        predicate: (Byte?) -> Boolean,
    ): Byte {
        val (from, to) = resolveBoundary(sign)
        return generate(from, to, predicate)
    }

    private companion object {
        const val ZERO = 0.toByte()
    }
}
