/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlinx.atomicfu.atomic
import tech.antibytes.kfixture.FixtureContract.COLLECTION_LOWER_BOUND
import tech.antibytes.kfixture.FixtureContract.COLLECTION_UPPER_BOUND
import tech.antibytes.kfixture.qualifier.resolveId
import kotlin.jvm.JvmName
import kotlin.random.Random
import kotlin.reflect.KClass

internal class Fixture(
    override val random: Random,
    generators: Map<String, PublicApi.Generator<out Any>>
) : PublicApi.Fixture {
    private val _generators = atomic(generators)

    override val generators: Map<String, PublicApi.Generator<out Any>> by _generators
}

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
internal const val LIST_LOWER_BOUND: Int = 0

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
internal fun PublicApi.Fixture.determineCollectionSize(
    size: Int?
): Int {
    return size ?: random.nextInt(COLLECTION_LOWER_BOUND, COLLECTION_UPPER_BOUND)
}

@InternalAPI
@PublishedApi
internal fun PublicApi.Fixture.pickAnListIndex(
    list: List<*>
): Int {
    return random.nextInt(LIST_LOWER_BOUND, list.size)
}

@InternalAPI
@PublishedApi
internal fun PublicApi.Fixture.chooseNumberType(
    qualifier: PublicApi.Qualifier?
): String {
    val typeIdx = pickAnListIndex(numberTypes)

    return resolveId(
        numberTypes[typeIdx],
        qualifier
    )
}

@InternalAPI
@PublishedApi
internal inline fun <reified T> PublicApi.Fixture.resolveIdentifier(
    qualifier: PublicApi.Qualifier?
): String {
    return if (T::class == Number::class) {
        chooseNumberType(qualifier)
    } else {
        resolveId(
            T::class as KClass<*>,
            qualifier
        )
    }
}

public fun <T> PublicApi.Fixture.fixture(
    iterable: Iterable<T>
): T {
    val values = iterable.toList()

    return values[pickAnListIndex(values)]
}

public inline fun <reified T> PublicApi.Fixture.fixture(
    qualifier: PublicApi.Qualifier? = null
): T {
    val returnNull = random.returnNull<T>()
    val id = resolveIdentifier<T>(qualifier)

    return when {
        !generators.containsKey(id) -> throw IllegalStateException("Missing Generator for ClassID ($id).")
        returnNull -> null as T
        else -> generators[id]!!.generate() as T
    }
}

public inline fun <reified T> PublicApi.Fixture.mutableListFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): MutableList<T> {
    val actualSize = determineCollectionSize(size)

    return MutableList(actualSize) {
        fixture(qualifier)
    }
}

@Suppress("UNUSED_PARAMETER")
@JvmName("mutableListFixtureAlias")
public inline fun <reified C : MutableList<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<MutableList<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = mutableListFixture<T>(
    qualifier = qualifier,
    size = size
) as C

public inline fun <reified T> PublicApi.Fixture.listFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): List<T> = mutableListFixture(
    qualifier = qualifier,
    size = size,
)

@Suppress("UNUSED_PARAMETER")
@JvmName("listFixtureAlias")
public inline fun <reified C : List<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<List<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = listFixture<T>(
    qualifier = qualifier,
    size = size
) as C

public inline fun <reified T> PublicApi.Fixture.mutableCollectionFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): MutableCollection<T> = mutableListFixture(
    qualifier = qualifier,
    size = size
)

@Suppress("UNUSED_PARAMETER")
@JvmName("mutableCollectionFixtureAlias")
public inline fun <reified C : MutableCollection<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<MutableCollection<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = mutableCollectionFixture<T>(
    qualifier = qualifier,
    size = size
) as C

public inline fun <reified T> PublicApi.Fixture.collectionFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): MutableCollection<T> = mutableListFixture(
    qualifier = qualifier,
    size = size
)

@Suppress("UNUSED_PARAMETER")
@JvmName("collectionFixtureAlias")
public inline fun <reified C : Collection<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<Collection<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = collectionFixture<T>(
    qualifier = qualifier,
    size = size
) as C

public inline fun <reified T> PublicApi.Fixture.arrayFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): Array<T> = mutableListFixture<T>(
    qualifier = qualifier,
    size = size
).toTypedArray()

@Suppress("UNUSED_PARAMETER")
@JvmName("arrayListFixtureAlias")
public inline fun <reified T> PublicApi.Fixture.fixture(
    type: KClass<Array<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): Array<T> = arrayFixture(
    qualifier = qualifier,
    size = size
)

public inline fun <reified T> PublicApi.Fixture.sequenceFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
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
public inline fun <reified C : Sequence<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<Sequence<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = sequenceFixture<T>(
    qualifier = qualifier,
    size = size
) as C

public inline fun <reified First, reified Second> PublicApi.Fixture.pairFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
): Pair<First, Second> = fixture<First>(keyQualifier) to fixture(valueQualifier)

@Suppress("UNUSED_PARAMETER")
@JvmName("pairFixtureAlias")
public inline fun <reified C : Pair<First, Second>, reified First, reified Second> PublicApi.Fixture.fixture(
    type: KClass<Pair<*, *>>,
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
): C = pairFixture<First, Second>(
    keyQualifier = keyQualifier,
    valueQualifier = valueQualifier,
) as C

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

public inline fun <reified Key, reified Value> PublicApi.Fixture.mapFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): Map<Key, Value> {
    val actualSize = determineCollectionSize(size)

    return MutableList<Pair<Key, Value>>(actualSize) {
        pairFixture(keyQualifier, valueQualifier)
    }.toMap()
}

@Suppress("UNUSED_PARAMETER")
@JvmName("mapFixtureAlias")
public inline fun <reified C : Map<Key, Value>, reified Key, reified Value> PublicApi.Fixture.fixture(
    type: KClass<Map<*, *>>,
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): C = mapFixture<Key, Value>(
    keyQualifier = keyQualifier,
    valueQualifier = valueQualifier,
    size = size,
) as C

public inline fun <reified Key, reified Value> PublicApi.Fixture.mutableMapFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): Map<Key, Value> = mapFixture<Key, Value>(
    keyQualifier = keyQualifier,
    valueQualifier = valueQualifier,
    size = size,
).toMutableMap()

@Suppress("UNUSED_PARAMETER")
@JvmName("mutableMapFixtureAlias")
public inline fun <reified C : MutableMap<Key, Value>, reified Key, reified Value> PublicApi.Fixture.fixture(
    type: KClass<MutableMap<*, *>>,
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): C = mutableMapFixture<Key, Value>(
    keyQualifier = keyQualifier,
    valueQualifier = valueQualifier,
    size = size,
) as C

public inline fun <reified T> PublicApi.Fixture.mutableSetFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
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
public inline fun <reified C : MutableSet<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<MutableSet<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = mutableSetFixture<T>(
    qualifier = qualifier,
    size = size
) as C

public inline fun <reified T> PublicApi.Fixture.setFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): Set<T> = mutableSetFixture(qualifier, size)

@Suppress("UNUSED_PARAMETER")
@JvmName("setFixtureAlias")
public inline fun <reified C : Set<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<Set<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = setFixture<T>(
    qualifier = qualifier,
    size = size
) as C
