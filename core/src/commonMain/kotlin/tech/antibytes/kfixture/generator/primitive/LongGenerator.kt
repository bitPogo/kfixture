/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import kotlin.random.nextLong
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.generator.Generator

internal class LongGenerator(
    private val random: Random,
) : PublicApi.SignedNumberGenerator<Long, Long>, Generator<Long>() {
    override fun generate(): Long = random.nextLong()

    override fun generate(predicate: (Long) -> Boolean): Long = returnFilteredValue(predicate, ::generate)

    override fun generate(
        from: Long,
        to: Long,
        predicate: (Long?) -> Boolean,
    ): Long = returnFilteredValue(predicate) { random.nextLong(LongRange(from, to)) }

    private fun resolveBoundary(sign: PublicApi.Sign): Pair<Long, Long> {
        return if (sign == PublicApi.Sign.POSITIVE) {
            ZERO to Long.MAX_VALUE
        } else {
            Long.MIN_VALUE to ZERO
        }
    }

    override fun generate(sign: PublicApi.Sign, predicate: (Long?) -> Boolean): Long {
        val (from, to) = resolveBoundary(sign)
        return generate(from, to, predicate)
    }

    private companion object {
        const val ZERO = 0L
    }
}
