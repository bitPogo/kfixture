/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.selector

import kotlin.random.Random
import tech.antibytes.kfixture.FixtureContract.ACCESS_LOWER_BOUND
import tech.antibytes.kfixture.PublicApi

internal class SelectorGenerator<T : Any>(
    private val random: Random,
    private val options: Array<T>,
) : PublicApi.Generator<T> {
    private val max = options.size

    override fun generate(): T {
        val pick = random.nextInt(ACCESS_LOWER_BOUND, max)

        return options[pick]
    }

    override fun generate(predicate: (T) -> Boolean): T {
        TODO("Not yet implemented")
    }
}