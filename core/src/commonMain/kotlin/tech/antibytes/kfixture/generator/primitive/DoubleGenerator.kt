/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal class DoubleGenerator(
    private val random: Random,
) : PublicApi.SignedNumberGenerator<Double, Double>, Generator<Double>() {
    override fun generate(): Double = generate(Double.MIN_VALUE, Double.MAX_VALUE)

    override fun generate(
        predicate: (Double?) -> Boolean,
    ): Double = returnFilteredValue(predicate, ::generate)

    private fun fill(lowerBound: Double, limit: Double): Double {
        return if (random.nextBoolean()) {
            limit
        } else {
            lowerBound
        }
    }

    private fun generate(
        from: Double,
        to: Double,
    ): Double {
        val number = random.nextDouble(from, to)
        val difference = to - number

        return if (difference < 1.0) {
            fill(number, to)
        } else {
            number
        }
    }

    override fun generate(
        from: Double,
        to: Double,
        predicate: (Double?) -> Boolean,
    ): Double = returnFilteredValue(predicate) { generate(from, to) }

    private fun resolveBoundary(sign: PublicApi.Sign): Pair<Double, Double> {
        return if (sign == PublicApi.Sign.POSITIVE) {
            ZERO to Double.MAX_VALUE
        } else {
            Double.MIN_VALUE to ZERO
        }
    }

    override fun generate(sign: PublicApi.Sign, predicate: (Double?) -> Boolean): Double {
        val (from, to) = resolveBoundary(sign)
        return generate(from, to, predicate)
    }

    private companion object {
        const val ZERO = 0.0
    }
}
