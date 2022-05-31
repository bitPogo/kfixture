/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.PublicApi
import kotlin.random.Random

internal class StringGenerator(
    val random: IsolateState<Random>
) : PublicApi.Generator<String> {
    override fun generate(): String {
        val length = random.access { it.nextInt(1, 10) }
        val chars = ByteArray(length)

        for (idx in 0 until length) {
            chars[idx] = random.access { it.nextInt(33, 126).toByte() }
        }

        return chars.decodeToString()
    }
}
