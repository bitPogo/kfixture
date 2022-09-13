/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.random.Random
import kotlinx.datetime.Instant
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.ktx.datetime.defaultPredicate
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

internal class InstantGenerator(
    private val longGenerator: PublicApi.RangedGenerator<Long, Long>,
) : PublicApi.RangedGenerator<Long, Instant> {
    override fun generate(): Instant = generate(predicate = ::defaultPredicate)

    override fun generate(predicate: (Long?) -> Boolean): Instant = generate(
        from = 0L,
        to = Long.MAX_VALUE,
        predicate = predicate,
    )

    private fun guardRange(
        from: Long,
        to: Long,
    ) {
        if (from < 0) {
            throw IllegalArgumentException("The lower bound of an Instant must be greater than -1!")
        }

        if (to <= from) {
            throw IllegalArgumentException("The upper bound of an Instant must be greater than lower bound!")
        }
    }

    override fun generate(from: Long, to: Long, predicate: (Long?) -> Boolean): Instant {
        guardRange(from = from, to = to)

        val milliSeconds = longGenerator.generate(
            from = from,
            to = to,
            predicate = predicate,
        )
        return Instant.fromEpochMilliseconds(milliSeconds)
    }

    companion object : PublicApi.DependentGeneratorFactory<Instant> {
        @Suppress("UNCHECKED_CAST")
        override fun getInstance(
            random: Random,
            generators: Map<String, PublicApi.Generator<out Any>>,
        ): PublicApi.Generator<Instant> {
            val id = resolveGeneratorId(Long::class)

            return InstantGenerator(
                longGenerator = generators[id] as PublicApi.RangedGenerator<Long, Long>,
            )
        }
    }
}
