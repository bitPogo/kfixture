/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class DoubleGenerator(
    private val random: Random,
) : PublicApi.SignedNumberGenerator<Double, Double> {
    override fun generate(): Double = generate(Double.MIN_VALUE, Double.MAX_VALUE)

    private fun fill(lowerBound: Double, limit: Double): Double {
        return if (random.nextBoolean()) {
            limit
        } else {
            lowerBound
        }
    }

    override fun generate(from: Double, to: Double): Double {
        val number = random.nextDouble(from, to)
        val difference = to - number

        return if (difference < 1.0) {
            fill(number, to)
        } else {
            number
        }
    }

    private fun resolveBoundary(sign: PublicApi.Sign): Pair<Double, Double> {
        return if (sign == PublicApi.Sign.POSITIVE) {
            ZERO to Double.MAX_VALUE
        } else {
            Double.MIN_VALUE to ZERO
        }
    }

    override fun generate(sign: PublicApi.Sign): Double {
        val (from, to) = resolveBoundary(sign)
        return generate(from, to)
    }

    private companion object {
        const val ZERO = 0.0
    }
}
