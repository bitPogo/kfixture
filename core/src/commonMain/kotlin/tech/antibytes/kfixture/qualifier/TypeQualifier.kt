/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.qualifier

import kotlin.reflect.KClass
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.resolveClassName

internal class TypeQualifier(
    private val type: KClass<out Any>,
) : PublicApi.Qualifier {
    override val value: String
        get() = resolveClassName(type)
}
