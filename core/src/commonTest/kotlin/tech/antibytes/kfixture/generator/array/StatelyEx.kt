/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.collections.IsoMutableList
import co.touchlab.stately.collections.sharedMutableListOf

fun <T> List<T>.toSharedMutableList(): IsoMutableList<T> {
    val transformed: IsoMutableList<T> = sharedMutableListOf()

    this.forEach { value ->
        transformed.add(value)
    }

    return transformed
}
