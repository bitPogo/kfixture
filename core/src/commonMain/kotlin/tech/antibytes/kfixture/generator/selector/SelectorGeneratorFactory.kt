/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.selector

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

public class SelectorGeneratorFactory<T : Any>(
    private val options: Array<T>,
) : PublicApi.GeneratorFactory<T> {
    init {
        if (options.isEmpty()) {
            throw IllegalArgumentException("Missing selectable items!")
        }
    }

    override fun getInstance(
        random: Random,
    ): PublicApi.Generator<T> = SelectorGenerator(random, options)
}
