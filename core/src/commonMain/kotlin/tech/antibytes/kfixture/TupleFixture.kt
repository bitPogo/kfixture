/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.jvm.JvmName
import kotlin.reflect.KClass

/**
 * Creates a Pair of values for given types.
 * @param First - the type which is supposed to be created for the first value.
 * @param Second - the type which is supposed to be created for the second value.
 * @param firstQualifier - a optional qualifier for a special flavour of a type of the first value.
 * @param secondQualifier - a optional qualifier for a special flavour of a type of the second value.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified First, reified Second> PublicApi.Fixture.pairFixture(
    firstQualifier: PublicApi.Qualifier? = null,
    secondQualifier: PublicApi.Qualifier? = null,
): Pair<First, Second> = fixture<First>(firstQualifier) to fixture(secondQualifier)

@Suppress("UNUSED_PARAMETER")
@JvmName("pairFixtureAlias")
/**
 * Creates a Pair of values for given types.
 * @param First - the type which is supposed to be created for the first value.
 * @param Second - the type which is supposed to be created for the second value.
 * @param C the enclosing Pair.
 * @param type the identifying type of the generic.
 * @param firstQualifier - a optional qualifier for a special flavour of a type of the first value.
 * @param secondQualifier - a optional qualifier for a special flavour of a type of the second value.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Pair<First, Second>, reified First, reified Second> PublicApi.Fixture.fixture(
    type: KClass<Pair<*, *>>,
    firstQualifier: PublicApi.Qualifier? = null,
    secondQualifier: PublicApi.Qualifier? = null,
): C = pairFixture<First, Second>(
    firstQualifier = firstQualifier,
    secondQualifier = secondQualifier,
) as C

/**
 * Creates a Triple of values for given types.
 * @param First the type which is supposed to be created for the first value.
 * @param Second the type which is supposed to be created for the second value.
 * @param Third the type which is supposed to be created for the third value.
 * @param firstQualifier a optional qualifier for a special flavour of a type of the first value.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified First, reified Second, reified Third> PublicApi.Fixture.tripleFixture(
    firstQualifier: PublicApi.Qualifier? = null,
    secondQualifier: PublicApi.Qualifier? = null,
    thirdQualifier: PublicApi.Qualifier? = null,
): Triple<First, Second, Third> {
    return Triple(
        fixture(firstQualifier),
        fixture(secondQualifier),
        fixture(thirdQualifier),
    )
}

@Suppress("UNUSED_PARAMETER")
@JvmName("tripleFixtureAlias")
/**
 * Creates a Triple of values for given types.
 * @param First the type which is supposed to be created for the first value.
 * @param Second the type which is supposed to be created for the second value.
 * @param Third the type which is supposed to be created for the third value.
 * @param C the enclosing Triple.
 * @param type the identifying type of the generic.
 * @param firstQualifier a optional qualifier for a special flavour of a type of the first value.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Triple<First, Second, Third>, reified First, reified Second, reified Third> PublicApi.Fixture.fixture(
    type: KClass<Triple<*, *, *>>,
    firstQualifier: PublicApi.Qualifier? = null,
    secondQualifier: PublicApi.Qualifier? = null,
    thirdQualifier: PublicApi.Qualifier? = null,
): C = tripleFixture<First, Second, Third>(
    firstQualifier = firstQualifier,
    secondQualifier = secondQualifier,
    thirdQualifier = thirdQualifier,
) as C
