/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.math.abs
import kotlin.random.Random
import kotlin.random.nextInt
import tech.antibytes.kfixture.PublicApi

internal class FloatGenerator(
    private val random: Random,
) : PublicApi.RangedGenerator<Float, Float>, Generator<Float>() {
    override fun generate(): Float = random.nextFloat() + random.nextInt()

    override fun generate(
        predicate: (Float?) -> Boolean,
    ): Float = returnFilteredValue(predicate, ::generate)

    private fun determineSign(from: Float, to: Float): Int {
        return if (to - abs(from) < 0) {
            -1
        } else {
            1
        }
    }

    private fun Random.nextSignedFloat(lowerBound: Float, upperBound: Float): Float {
        return nextFloat() * determineSign(lowerBound, upperBound)
    }

    private fun Int.assembleFloat(
        from: Float,
        to: Float,
    ): Float = this + random.nextSignedFloat(from, to)

    private fun generate(from: Float, to: Float): Float {
        val base = random.nextInt(IntRange(from.toInt(), to.toInt()))
        val float = base.assembleFloat(from, to)

        return if (float < from || float > to) {
            base.toFloat()
        } else {
            float
        }
    }

    override fun generate(
        from: Float,
        to: Float,
        predicate: (Float?) -> Boolean,
    ): Float = returnFilteredValue(predicate) { generate(from, to) }
}
