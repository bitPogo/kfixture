/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("OPT_IN_USAGE", "ktlint:standard:filename")

package tech.antibytes.kfixture

import tech.antibytes.kfixture.PublicApi.FilterableArrayGenerator
import tech.antibytes.kfixture.PublicApi.FilterableGenerator

/**
 * Creates a value for a given type, excluding generics like List or Array.
 * @param FixtureType the type which is supposed to be created.
 * @param FilterType the type which used for filtering or partial results.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Suppress("UNCHECKED_CAST")
@Throws(IllegalStateException::class)
public inline fun <reified FixtureType, reified FilterType : Any> PublicApi.Fixture.fixture(
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<FilterType?, Boolean>,
): FixtureType {
    val returnNull = random.returnNull<FixtureType>()
    val id = resolveIdentifier<FixtureType>(qualifier)
    val generator = generators[id]

    return when {
        generator !is FilterableGenerator<*, *> -> throw IllegalStateException("Missing Generator for ClassID ($id).")
        returnNull -> null as FixtureType
        else -> (generators[id] as FilterableGenerator<FilterType, Any>).generate(predicate) as FixtureType
    }
}

/**
 * Creates a value for a given type, excluding generics like List or Array.
 * @param FixtureType the type which is supposed to be created.
 * @param FilterType the type which used for filtering partial results.
 * @param size of the resulting Array
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Suppress("UNCHECKED_CAST")
@Throws(IllegalStateException::class)
public inline fun <reified FixtureType, reified FilterType : Any> PublicApi.Fixture.fixture(
    size: Int,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<FilterType?, Boolean>,
): FixtureType {
    val returnNull = random.returnNull<FixtureType>()
    val id = resolveIdentifier<FixtureType>(qualifier)
    val generator = generators[id]

    return when {
        generator !is FilterableArrayGenerator<*, *> -> {
            throw IllegalStateException("Missing Generator for ClassID ($id).")
        }
        returnNull -> null as FixtureType
        else -> (generators[id] as FilterableArrayGenerator<FilterType, Any>)
            .generate(size, predicate) as FixtureType
    }
}
