/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi
import kotlin.random.nextInt

internal class IntegerGenerator(
    private val random: Random,
) : PublicApi.SignedNumberGenerator<Int> {
    override fun generate(): Int = random.nextInt()

    override fun generate(from: Int, to: Int): Int = random.nextInt(IntRange(from, to))

    private fun resolveBoundary(sign: PublicApi.Sign): Pair<Int, Int> {
        return if (sign == PublicApi.Sign.POSITIVE) {
            ZERO to Int.MAX_VALUE
        } else {
            Int.MIN_VALUE to ZERO
        }
    }

    override fun generate(sign: PublicApi.Sign): Int {
        val (from, to) = resolveBoundary(sign)
        return generate(from, to)
    }

    private companion object {
        const val ZERO = 0
    }
}
