/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("OPT_IN_USAGE")

package tech.antibytes.kfixture

import kotlin.reflect.KClass

@PublishedApi
@InternalAPI
internal inline fun <reified T : Enum<T>> PublicApi.Fixture.selectEnumItem(): T {
    val values = enumValues<T>()

    return values[pickAnIndex(values.size)]
}

/**
 * Selects a value for a given Enum.
 * @param T the enum.
 * @param predicate which filters non matching values.
 */
public inline fun <reified T : Enum<T>> PublicApi.Fixture.enumFixture(
    predicate: Function1<T, Boolean> = ::defaultPredicate,
): T {
    var returnValue: T

    do {
        returnValue = selectEnumItem()
    } while (!predicate(returnValue))

    return returnValue
}

@Suppress("UNUSED_PARAMETER")
/**
 * Selects a value for a given Enum.
 * Alias of enumFixture.
 *
 * @param T the enum.
 * @param type the identifying type of the generic.
 * @param predicate which filters non matching values.
 */
public inline fun <reified T : Enum<T>> PublicApi.Fixture.fixture(
    type: KClass<Enum<*>>,
    predicate: Function1<T, Boolean> = ::defaultPredicate,
): T = enumFixture(predicate)
