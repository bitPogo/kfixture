/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.qualifier

import tech.antibytes.kfixture.FixtureContract.Companion.SEPARATOR
import tech.antibytes.kfixture.InternalAPI
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.resolveClassName
import kotlin.reflect.KClass

// see: https://github.com/InsertKoinIO/koin/blob/48e532798d53d02cfcb4420db86bf5bfa7c01428/core/koin-core/src/commonMain/kotlin/org/koin/core/qualifier/Qualifier.kt#L30
fun named(value: String): PublicApi.Qualifier = StringQualifier(value)

fun <E : Enum<E>> named(value: E): PublicApi.Qualifier {
    return StringQualifier(
        value.toString().lowercase()
    )
}

internal fun resolveQualifier(
    vararg qualifiers: PublicApi.Qualifier
): String = qualifiers.joinToString(SEPARATOR) { qualifier -> qualifier.value }

@InternalAPI
@PublishedApi
internal fun resolveId(
    clazz: KClass<out Any>,
    qualifier: PublicApi.Qualifier? = null
): String {
    return if (qualifier == null) {
        resolveClassName(clazz)
    } else {
        resolveQualifier(qualifier, TypeQualifier(clazz))
    }
}
