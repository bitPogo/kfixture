/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.jvm.JvmName
import kotlin.reflect.KClass
import tech.antibytes.kfixture.PublicApi.Qualifier

/**
 * Creates a Array of values for a given type.
 * @param T the type which is supposed to be created.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Array.
 * @param nestedGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.arrayFixture(
    qualifier: Qualifier? = null,
    size: Int? = null,
    nestedGenerator: Function1<Qualifier?, T>,
): Array<T> = mutableListFixture(
    qualifier = qualifier,
    size = size,
    nestedGenerator = nestedGenerator,
).toTypedArray()

/**
 * Creates a Array of values for a given type.
 * @param T the type which is supposed to be created.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Array.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.arrayFixture(
    qualifier: Qualifier? = null,
    size: Int? = null,
): Array<T> = mutableListFixture<T>(
    qualifier = qualifier,
    size = size,
).toTypedArray()

@Suppress("UNUSED_PARAMETER")
@JvmName("arrayListFixtureAlias")
/**
 * Creates a Array of values for a given type.
 * @param T the type which is supposed to be created.
 * @param type the identifying type of the generic.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Array.
 * @param nestedGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.fixture(
    type: KClass<Array<*>>,
    qualifier: Qualifier? = null,
    size: Int? = null,
    nestedGenerator: Function1<Qualifier?, T>,
): Array<T> = arrayFixture(
    qualifier = qualifier,
    size = size,
    nestedGenerator = nestedGenerator,
)

@Suppress("UNUSED_PARAMETER")
@JvmName("arrayListFixtureAlias")
/**
 * Creates a Array of values for a given type.
 * @param T the type which is supposed to be created.
 * @param type the identifying type of the generic.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Array.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.fixture(
    type: KClass<Array<*>>,
    qualifier: Qualifier? = null,
    size: Int? = null,
): Array<T> = arrayFixture(
    qualifier = qualifier,
    size = size,
)
