/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.test.fixture

import co.touchlab.stately.isolate.IsolateState
import kotlinx.atomicfu.atomic
import tech.antibytes.util.test.fixture.qualifier.resolveId
import kotlin.random.Random
import kotlin.reflect.KClass

internal class Fixture(
    override val random: IsolateState<Random>,
    generators: Map<String, PublicApi.Generator<out Any>>
) : PublicApi.Fixture {
    private val _generators = atomic(generators)

    override val generators: Map<String, PublicApi.Generator<out Any>> by _generators
}

@InternalAPI
@PublishedApi
internal inline fun <reified T> isNullable(): Boolean = null is T

@InternalAPI
@PublishedApi
internal inline fun <reified T> returnNull(random: IsolateState<Random>): Boolean {
    return if (isNullable<T>()) {
        random.access { it.nextBoolean() }
    } else {
        false
    }
}

inline fun <reified T> PublicApi.Fixture.fixture(
    qualifier: PublicApi.Qualifier? = null
): T {
    val returnNull = returnNull<T>(random)

    val id = resolveId(
        T::class as KClass<*>,
        qualifier
    )

    return when {
        !generators.containsKey(id) -> throw RuntimeException("Missing Generator for ClassID ($id).")
        returnNull -> null as T
        else -> generators[id]!!.generate() as T
    }
}

inline fun <reified T> PublicApi.Fixture.listFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): List<T> {
    val actualSize = size ?: random.access { it.nextInt(1, 10) }

    val list = mutableListOf<T>()

    for (idx in 0 until actualSize) {
        list.add(fixture(qualifier))
    }

    return list
}

inline fun <reified First, reified Second> PublicApi.Fixture.pairFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
): Pair<First, Second> {
    return Pair(
        fixture(keyQualifier),
        fixture(valueQualifier)
    )
}

inline fun <reified Key, reified Value> PublicApi.Fixture.mapFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): Map<Key, Value> {
    val actualSize = size ?: random.access { it.nextInt(1, 10) }

    val list = mutableListOf<Pair<Key, Value>>()

    for (idx in 0 until actualSize) {
        list.add(pairFixture(keyQualifier, valueQualifier))
    }

    return list.toMap()
}
