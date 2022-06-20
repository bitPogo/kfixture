/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class UByteGenerator(
    private val random: Random
) : PublicApi.Generator<UByte> {
    override fun generate(): UByte = random.nextInt().toUByte()
}
