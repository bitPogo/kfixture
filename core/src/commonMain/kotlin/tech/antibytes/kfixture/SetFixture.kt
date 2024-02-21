/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("OPT_IN_USAGE")

package tech.antibytes.kfixture

import kotlin.jvm.JvmName
import kotlin.reflect.KClass
import tech.antibytes.kfixture.PublicApi.Qualifier

/**
 * Creates a MutableSet of values for given types.
 * @param T the type which is supposed to be created for the value.
 * @param qualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Set.
 * @param nestedGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.mutableSetFixture(
    qualifier: Qualifier? = null,
    size: Int? = null,
    nestedGenerator: Function1<Qualifier?, T>,
): MutableSet<T> {
    val actualSize = determineCollectionSize(size)

    val set = mutableSetOf<T>()

    repeat(actualSize) {
        set.add(nestedGenerator.invoke(qualifier))
    }

    return set
}

/**
 * Creates a MutableSet of values for given types.
 * @param T the type which is supposed to be created for the value.
 * @param qualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Set.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.mutableSetFixture(
    qualifier: Qualifier? = null,
    size: Int? = null,
): MutableSet<T> {
    val actualSize = determineCollectionSize(size)

    val set = mutableSetOf<T>()

    repeat(actualSize) {
        set.add(fixture(qualifier))
    }

    return set
}

@Suppress("UNUSED_PARAMETER")
@JvmName("mutableSetFixtureAlias")
/**
 * Creates a MutableSet of values for given types.
 * @param T the type which is supposed to be created for the value.
 * @param C the enclosing Set.
 * @param type the identifying type of the generic.
 * @param qualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Set.
 * @param nestedGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : MutableSet<T>, reified T> PublicApi.Fixture.fixture(type: KClass<MutableSet<*>>, qualifier: Qualifier? = null, size: Int? = null, nestedGenerator: Function1<Qualifier?, T>): C = mutableSetFixture(
    qualifier = qualifier,
    size = size,
    nestedGenerator = nestedGenerator,
) as C

@Suppress("UNUSED_PARAMETER")
@JvmName("mutableSetFixtureAlias")
/**
 * Creates a MutableSet of values for given types.
 * @param T the type which is supposed to be created for the value.
 * @param C the enclosing Set.
 * @param type the identifying type of the generic.
 * @param qualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Set.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : MutableSet<T>, reified T> PublicApi.Fixture.fixture(type: KClass<MutableSet<*>>, qualifier: Qualifier? = null, size: Int? = null): C = mutableSetFixture<T>(
    qualifier = qualifier,
    size = size,
) as C

/**
 * Creates a Set of values for given types.
 * @param T the type which is supposed to be created for the value.
 * @param qualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Set.
 * @param nestedGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.setFixture(
    qualifier: Qualifier? = null,
    size: Int? = null,
    nestedGenerator: Function1<Qualifier?, T>,
): Set<T> = mutableSetFixture(
    qualifier = qualifier,
    size = size,
    nestedGenerator = nestedGenerator,
)

/**
 * Creates a Set of values for given types.
 * @param T the type which is supposed to be created for the value.
 * @param qualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Set.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.setFixture(
    qualifier: Qualifier? = null,
    size: Int? = null,
): Set<T> = mutableSetFixture(
    qualifier = qualifier,
    size = size,
)

@Suppress("UNUSED_PARAMETER")
@JvmName("setFixtureAlias")
/**
 * Creates a Set of values for given types.
 * @param T the type which is supposed to be created for the value.
 * @param C the enclosing Set.
 * @param type the identifying type of the generic.
 * @param qualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Set.
 * @param nestedGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Set<T>, reified T> PublicApi.Fixture.fixture(type: KClass<Set<*>>, qualifier: Qualifier? = null, size: Int? = null, nestedGenerator: Function1<Qualifier?, T>): C = setFixture(
    qualifier = qualifier,
    size = size,
    nestedGenerator = nestedGenerator,
) as C

@Suppress("UNUSED_PARAMETER")
@JvmName("setFixtureAlias")
/**
 * Creates a Set of values for given types.
 * @param T the type which is supposed to be created for the value.
 * @param C the enclosing Set.
 * @param type the identifying type of the generic.
 * @param qualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Set.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Set<T>, reified T> PublicApi.Fixture.fixture(type: KClass<Set<*>>, qualifier: Qualifier? = null, size: Int? = null): C = setFixture<T>(
    qualifier = qualifier,
    size = size,
) as C
