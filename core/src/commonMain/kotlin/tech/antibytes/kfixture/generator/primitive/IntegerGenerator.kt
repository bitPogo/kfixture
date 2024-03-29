/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import kotlin.random.nextInt
import tech.antibytes.kfixture.PublicApi

internal class IntegerGenerator(
    private val random: Random,
) : PublicApi.SignedNumberGenerator<Int, Int>, Generator<Int>() {
    override fun generate(): Int = random.nextInt()

    override fun generate(
        predicate: (Int?) -> Boolean,
    ): Int = returnFilteredValue(predicate, ::generate)

    override fun generate(
        from: Int,
        to: Int,
        predicate: (Int?) -> Boolean,
    ): Int = returnFilteredValue(predicate) { random.nextInt(IntRange(from, to)) }

    private fun resolveBoundary(sign: PublicApi.Sign): Pair<Int, Int> {
        return if (sign == PublicApi.Sign.POSITIVE) {
            ZERO to Int.MAX_VALUE
        } else {
            Int.MIN_VALUE to ZERO
        }
    }

    override fun generate(
        sign: PublicApi.Sign,
        predicate: (Int?) -> Boolean,
    ): Int {
        val (from, to) = resolveBoundary(sign)
        return generate(from, to, predicate)
    }

    private companion object {
        const val ZERO = 0
    }
}
