/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("OPT_IN_USAGE")

package tech.antibytes.kfixture.qualifier

import kotlin.reflect.KClass
import tech.antibytes.kfixture.FixtureContract.SEPARATOR
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.resolveClassName

// see: https://github.com/InsertKoinIO/koin/blob/48e532798d53d02cfcb4420db86bf5bfa7c01428/core/koin-core/src/commonMain/kotlin/org/koin/core/qualifier/Qualifier.kt#L30
/**
 * Factory for Qualifiers based on Strings
 * @param value String which is referring to a special flavour.
 * @return Qualifier
 */
public fun qualifiedBy(value: String): PublicApi.Qualifier = StringQualifier(value)

/**
 * Factory for Qualifiers based on enum types
 * @param E the enum member it is based.
 * @param value enum member which is referring to a special flavour.
 * @return Qualifier
 */
public fun <E : Enum<E>> qualifiedBy(value: E): PublicApi.Qualifier {
    return StringQualifier(
        value.toString().lowercase(),
    )
}

internal fun resolveQualifier(
    vararg qualifiers: PublicApi.Qualifier,
): String = qualifiers.joinToString(SEPARATOR) { qualifier -> qualifier.value }

/**
 * Resolves an Id for a Generator based on a KClass with optional Qualifier.
 * @param clazz the KClass which are the primary part the Id is derived from.
 * @param qualifier optional secondary part which is used to derive an Generator Id.
 * @return String which represents a GeneratorId.
 */
public fun resolveGeneratorId(
    clazz: KClass<out Any>,
    qualifier: PublicApi.Qualifier? = null,
): String {
    return if (qualifier == null) {
        resolveClassName(clazz)
    } else {
        resolveQualifier(qualifier, TypeQualifier(clazz))
    }
}
