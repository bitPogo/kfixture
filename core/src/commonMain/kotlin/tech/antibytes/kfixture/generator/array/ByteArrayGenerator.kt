/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi.SignedNumberGenerator
import tech.antibytes.kfixture.PublicApi.SignedNumericArrayGenerator

internal class ByteArrayGenerator(
    random: Random,
    byteGenerator: SignedNumberGenerator<Byte, Byte>,
) : SignedNumericArrayGenerator<Byte, ByteArray>, SignedArrayNumberGenerator<Byte, ByteArray>(random, byteGenerator) {
    override fun arrayBuilder(
        size: Int,
        onEach: (idx: Int) -> Byte,
    ): ByteArray = ByteArray(size, onEach)

    override fun generate(from: Byte, to: Byte, predicate: (Byte) -> Boolean): ByteArray {
        TODO("Not yet implemented")
    }

    override fun generate(sign: PublicApi.Sign, predicate: (Byte) -> Boolean): ByteArray {
        TODO("Not yet implemented")
    }
}
