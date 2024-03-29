/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

internal abstract class SignedArrayNumberGenerator<T, R>(
    random: Random,
    private val numberGenerator: PublicApi.SignedNumberGenerator<T, T>,
) : PublicApi.SignedNumericArrayGenerator<T, R>,
    RangedArrayNumberGenerator<T, R>(random, numberGenerator)
    where T : Any, T : Comparable<T>, R : Any {
    override fun generate(
        sign: PublicApi.Sign,
        size: Int,
        predicate: (T?) -> Boolean,
    ): R = arrayBuilder(size) { numberGenerator.generate(sign, predicate) }

    override fun generate(
        sign: PublicApi.Sign,
        predicate: (T?) -> Boolean,
    ): R = generate(sign, chooseSize(), predicate)
}
