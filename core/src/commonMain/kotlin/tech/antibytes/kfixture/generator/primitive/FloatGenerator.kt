/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import kotlin.random.nextInt
import tech.antibytes.kfixture.PublicApi

internal class FloatGenerator(
    private val random: Random,
) : PublicApi.SignedNumberGenerator<Float, Float> {
    override fun generate(): Float = random.nextFloat() + random.nextInt()

    override fun generate(predicate: (Float) -> Boolean): Float {
        TODO("Not yet implemented")
    }

    override fun generate(from: Float, to: Float, predicate: (Float?) -> Boolean): Float {
        val limit = to.toInt()
        val base = random.nextInt(IntRange(from.toInt(), limit))

        return if (base == limit) {
            base.toFloat()
        } else {
            base + random.nextFloat()
        }
    }

    private fun resolveBoundary(sign: PublicApi.Sign): Pair<Float, Float> {
        return if (sign == PublicApi.Sign.POSITIVE) {
            ZERO to Float.MAX_VALUE
        } else {
            Float.MIN_VALUE to ZERO
        }
    }

    override fun generate(sign: PublicApi.Sign, predicate: (Float?) -> Boolean): Float {
        val (from, to) = resolveBoundary(sign)
        return generate(from, to)
    }

    private companion object {
        const val ZERO = 0F
    }
}
