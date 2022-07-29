/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.random.Random
import kotlin.reflect.KClass
import tech.antibytes.kfixture.FixtureContract.COLLECTION_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.COLLECTION_UPPER_BOUND
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

@InternalAPI
@PublishedApi
internal val numberTypes: List<KClass<*>> = listOf(
    Byte::class,
    Short::class,
    Int::class,
    Float::class,
    Long::class,
    Double::class,
)

@InternalAPI
@PublishedApi
internal const val ITERABLE_LOWER_BOUND: Int = 0

@InternalAPI
@PublishedApi
internal inline fun <reified T> isNullable(): Boolean = null is T

@InternalAPI
@PublishedApi
internal inline fun <reified T> Random.returnNull(): Boolean {
    return if (isNullable<T>()) {
        nextBoolean()
    } else {
        false
    }
}

@InternalAPI
@PublishedApi
internal fun PublicApi.Fixture.pickAnIndex(
    upperBound: Int,
): Int = random.nextInt(
    from = ITERABLE_LOWER_BOUND,
    until = upperBound,
)

@InternalAPI
@PublishedApi
internal fun PublicApi.Fixture.determineCollectionSize(
    size: Int?,
): Int = size ?: random.nextInt(COLLECTION_LOWER_BOUND, COLLECTION_UPPER_BOUND)

@InternalAPI
@PublishedApi
internal fun PublicApi.Fixture.chooseNumberType(
    qualifier: PublicApi.Qualifier?,
): String {
    val typeIdx = pickAnIndex(numberTypes.size)

    return resolveGeneratorId(
        numberTypes[typeIdx],
        qualifier,
    )
}

@InternalAPI
@PublishedApi
internal inline fun <reified T> PublicApi.Fixture.resolveIdentifier(
    qualifier: PublicApi.Qualifier?,
): String {
    return if (T::class == Number::class) {
        chooseNumberType(qualifier)
    } else {
        resolveGeneratorId(
            T::class as KClass<*>,
            qualifier,
        )
    }
}
