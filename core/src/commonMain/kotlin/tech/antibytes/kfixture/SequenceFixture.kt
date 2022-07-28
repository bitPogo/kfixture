/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.jvm.JvmName
import kotlin.reflect.KClass

/**
 * Creates a Sequence of values for a given type.
 * @param T the type which is supposed to be created.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Sequence.
 * @param nestedGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.sequenceFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
    crossinline nestedGenerator: Function1<PublicApi.Qualifier?, T>,
): Sequence<T> {
    val actualSize = determineCollectionSize(size)

    return sequence {
        repeat(actualSize) {
            yield(nestedGenerator.invoke(qualifier))
        }
    }
}

/**
 * Creates a Sequence of values for a given type.
 * @param T the type which is supposed to be created.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Sequence.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.sequenceFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): Sequence<T> {
    val actualSize = determineCollectionSize(size)

    return sequence {
        repeat(actualSize) {
            yield(fixture(qualifier))
        }
    }
}

@Suppress("UNUSED_PARAMETER")
@JvmName("sequenceFixtureAlias")
/**
 * Creates a Sequence of values for a given type.
 * @param T the type which is supposed to be created.
 * @param C the enclosing Sequence.
 * @param type the identifying type of the generic.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Sequence.
 * @param nestedGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Sequence<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<Sequence<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
    crossinline nestedGenerator: Function1<PublicApi.Qualifier?, T>,
): C = sequenceFixture(
    qualifier = qualifier,
    size = size,
    nestedGenerator = nestedGenerator,
) as C

@Suppress("UNUSED_PARAMETER")
@JvmName("sequenceFixtureAlias")
/**
 * Creates a Sequence of values for a given type.
 * @param T the type which is supposed to be created.
 * @param C the enclosing Sequence.
 * @param type the identifying type of the generic.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param size the size of the Sequence.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Sequence<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<Sequence<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = sequenceFixture<T>(
    qualifier = qualifier,
    size = size,
) as C
