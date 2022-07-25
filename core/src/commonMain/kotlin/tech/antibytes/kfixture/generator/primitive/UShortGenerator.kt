/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.generator.Generator

internal class UShortGenerator(
    private val random: Random,
) : PublicApi.RangedGenerator<UShort, UShort>, Generator<UShort>() {
    override fun generate(): UShort = generate(
        from = UShort.MIN_VALUE,
        to = UShort.MAX_VALUE,
    )

    override fun generate(predicate: (UShort) -> Boolean): UShort = returnFilteredValue(predicate, ::generate)

    override fun generate(
        from: UShort,
        to: UShort,
        predicate: (UShort?) -> Boolean,
    ): UShort = returnFilteredValue(predicate) {
        random.nextInt(
            from = from.toInt(),
            until = to.toInt() + 1,
        ).toUShort()
    }
}
