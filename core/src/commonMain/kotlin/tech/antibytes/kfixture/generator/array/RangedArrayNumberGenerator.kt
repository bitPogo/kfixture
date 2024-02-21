/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("OPT_IN_USAGE")

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.FixtureContract.ARRAY_LOWER_BOUND
import tech.antibytes.kfixture.PublicApi

internal abstract class RangedArrayNumberGenerator<T, R>(
    private val random: Random,
    private val numberGenerator: PublicApi.RangedGenerator<T, T>,
) : PublicApi.RangedArrayGenerator<T, R>, FilterableArrayGenerator<T, R>(random, numberGenerator)
    where T : Any, T : Comparable<T>, R : Any {
    private fun unpackRange(
        ranges: Array<out ClosedRange<T>>,
    ): Pair<T, T> {
        val range = random.nextInt(ARRAY_LOWER_BOUND, ranges.size)

        return ranges[range].start to ranges[range].endInclusive
    }

    override fun generate(
        from: T,
        to: T,
        size: Int,
        predicate: (T?) -> Boolean,
    ): R = arrayBuilder(size) { numberGenerator.generate(from = from, to = to, predicate = predicate) }

    override fun generate(
        from: T,
        to: T,
        predicate: (T?) -> Boolean,
    ): R = generate(from = from, to = to, size = chooseSize(), predicate = predicate)

    override fun generate(
        vararg ranges: ClosedRange<T>,
        size: Int?,
        predicate: (T?) -> Boolean,
    ): R {
        val actualSize = size ?: chooseSize()

        return arrayBuilder(actualSize) {
            val (from, to) = unpackRange(ranges)

            numberGenerator.generate(from = from, to = to, predicate = predicate)
        }
    }
}
