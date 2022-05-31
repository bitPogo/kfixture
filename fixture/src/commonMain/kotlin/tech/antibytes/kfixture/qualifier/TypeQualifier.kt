/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.qualifier

import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.resolveClassName
import kotlin.reflect.KClass

internal class TypeQualifier(
    private val type: KClass<out Any>
) : PublicApi.Qualifier {
    override val value: String
        get() = resolveClassName(type)
}
