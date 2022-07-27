/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.jvm.JvmName
import kotlin.reflect.KClass

/**
 * Creates a Array of values for a given type.
 * @param T the type which is supposed to be created.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Array.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.arrayFixture(
    qualifier: PublicApi.Qualifier? = null,
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
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.fixture(
    type: KClass<Array<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): Array<T> = arrayFixture(
    qualifier = qualifier,
    size = size,
)
