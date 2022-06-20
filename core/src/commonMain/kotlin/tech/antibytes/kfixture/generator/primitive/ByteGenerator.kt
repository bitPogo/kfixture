/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class ByteGenerator(
    private val random: Random
) : PublicApi.Generator<Byte> {
    override fun generate(): Byte = random.nextInt().toByte()
}
