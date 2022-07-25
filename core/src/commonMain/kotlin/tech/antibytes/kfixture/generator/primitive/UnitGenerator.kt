/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import tech.antibytes.kfixture.PublicApi

internal object UnitGenerator : PublicApi.Generator<Unit> {
    override fun generate() = Unit
    override fun generate(predicate: (Unit) -> Boolean) {
        throw IllegalStateException("Unit cannot be filtered!")
    }
}
