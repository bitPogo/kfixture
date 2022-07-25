/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.FixtureContract.ARRAY_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.ARRAY_UPPER_BOUND
import tech.antibytes.kfixture.PublicApi

internal abstract class ArrayGenerator<T : Any, R : Any>(
    private val random: Random,
    private val generator: PublicApi.Generator<T>,
) : PublicApi.ArrayGenerator<R> {
    protected abstract fun arrayBuilder(size: Int, onEach: (idx: Int) -> T): R

    protected fun chooseSize(): Int = random.nextInt(ARRAY_LOWER_BOUND, ARRAY_UPPER_BOUND)

    override fun generate(size: Int): R = arrayBuilder(size) { generator.generate() }

    override fun generate(): R = generate(chooseSize())
}
