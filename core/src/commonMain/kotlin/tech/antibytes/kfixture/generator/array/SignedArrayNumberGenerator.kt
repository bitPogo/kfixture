/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import tech.antibytes.kfixture.FixtureContract.ARRAY_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.ARRAY_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal abstract class SignedArrayNumberGenerator<T, R>(
    private val random: Random,
    private val numberGenerator: PublicApi.SignedNumberGenerator<T, T>
) : PublicApi.SignedNumericArrayGenerator<T, R> where T : Any, T : Comparable<T>, R : Any {
    protected abstract fun arrayBuilder(size: Int, onEach: (idx: Int) -> T): R

    private fun chooseSize(): Int = random.nextInt(ARRAY_LOWER_BOUND, ARRAY_UPPER_BOUND)

    private fun unpackRange(
        ranges: Array<out ClosedRange<T>>
    ): Pair<T, T> {
        val range = random.nextInt(ARRAY_LOWER_BOUND, ranges.size)

        return ranges[range].start to ranges[range].endInclusive
    }

    override fun generate(size: Int): R = arrayBuilder(size) { numberGenerator.generate() }

    override fun generate(): R = generate(chooseSize())

    override fun generate(
        from: T,
        to: T,
        size: Int
    ): R = arrayBuilder(size) { numberGenerator.generate(from = from, to = to) }

    override fun generate(
        from: T,
        to: T
    ): R = generate(from = from, to = to, size = chooseSize())

    override fun generate(
        vararg ranges: ClosedRange<T>,
        size: Int
    ): R {
        return arrayBuilder(size) {
            val (from, to) = unpackRange(ranges)

            numberGenerator.generate(from = from, to = to)
        }
    }

    override fun generate(
        vararg ranges: ClosedRange<T>
    ): R = generate(ranges = ranges, size = chooseSize())

    override fun generate(
        sign: PublicApi.Sign,
        size: Int
    ): R = arrayBuilder(size) { numberGenerator.generate(sign) }

    override fun generate(sign: PublicApi.Sign): R = generate(sign, chooseSize())
}
