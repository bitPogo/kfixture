/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.jvm.JvmName
import kotlin.reflect.KClass

/**
 * Creates a MutableCollection of values for a given type.
 * @param T the type which is supposed to be created.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Collection.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.mutableCollectionFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): MutableCollection<T> = mutableListFixture(
    qualifier = qualifier,
    size = size,
)

@Suppress("UNUSED_PARAMETER")
@JvmName("mutableCollectionFixtureAlias")
/**
 * Creates a MutableCollection of values for a given type.
 * @param T the type which is supposed to be created.
 * @param C the enclosing Collection.
 * @param type the identifying type of the generic.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Collection.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : MutableCollection<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<MutableCollection<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = mutableCollectionFixture<T>(
    qualifier = qualifier,
    size = size,
) as C

/**
 * Creates an Collection of values for a given type.
 * @param T the type which is supposed to be created.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Collection.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.collectionFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): MutableCollection<T> = mutableListFixture(
    qualifier = qualifier,
    size = size,
)

@Suppress("UNUSED_PARAMETER")
@JvmName("collectionFixtureAlias")
/**
 * Creates an Collection of values for a given type.
 * @param T the type which is supposed to be created.
 * @param C the enclosing Collection.
 * @param type the identifying type of the generic.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Collection.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Collection<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<Collection<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = collectionFixture<T>(
    qualifier = qualifier,
    size = size,
) as C
