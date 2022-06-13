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

/**
 * Creates a value for a given type, excluding generics like List or Array.
 * @param T - the type which is supposed to be created.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
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

/**
 * Creates a MutableList of values for a given type.
 * @param T - the type which is supposed to be created.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @param size - the size of the List.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
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
/**
 * Creates a MutableList of values for a given type.
 * @param T - the type which is supposed to be created.
 * @param C - the enclosing List.
 * @param type - the identifying type of the generic.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @param size - the size of the List.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified C : MutableList<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<MutableList<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = mutableListFixture<T>(
    qualifier = qualifier,
    size = size
) as C

/**
 * Creates a List of values for a given type.
 * @param T - the type which is supposed to be created.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @param size - the size of the List.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified T> PublicApi.Fixture.listFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): List<T> = mutableListFixture(
    qualifier = qualifier,
    size = size,
)

@Suppress("UNUSED_PARAMETER")
@JvmName("listFixtureAlias")
/**
 * Creates a List of values for a given type.
 * @param T - the type which is supposed to be created.
 * @param C - the enclosing List.
 * @param type - the identifying type of the generic.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @param size - the size of the List.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified C : List<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<List<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = listFixture<T>(
    qualifier = qualifier,
    size = size
) as C

/**
 * Creates a MutableCollection of values for a given type.
 * @param T - the type which is supposed to be created.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @param size - the size of the Collection.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified T> PublicApi.Fixture.mutableCollectionFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): MutableCollection<T> = mutableListFixture(
    qualifier = qualifier,
    size = size
)

@Suppress("UNUSED_PARAMETER")
@JvmName("mutableCollectionFixtureAlias")
/**
 * Creates a MutableCollection of values for a given type.
 * @param T - the type which is supposed to be created.
 * @param C - the enclosing Collection.
 * @param type - the identifying type of the generic.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @param size - the size of the Collection.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified C : MutableCollection<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<MutableCollection<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = mutableCollectionFixture<T>(
    qualifier = qualifier,
    size = size
) as C

/**
 * Creates an Collection of values for a given type.
 * @param T - the type which is supposed to be created.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @param size - the size of the Collection.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified T> PublicApi.Fixture.collectionFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): MutableCollection<T> = mutableListFixture(
    qualifier = qualifier,
    size = size
)

@Suppress("UNUSED_PARAMETER")
@JvmName("collectionFixtureAlias")
/**
 * Creates an Collection of values for a given type.
 * @param T - the type which is supposed to be created.
 * @param C - the enclosing Collection.
 * @param type - the identifying type of the generic.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @param size - the size of the Collection.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified C : Collection<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<Collection<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = collectionFixture<T>(
    qualifier = qualifier,
    size = size
) as C

/**
 * Creates a Array of values for a given type.
 * @param T - the type which is supposed to be created.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @param size - the size of the Array.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified T> PublicApi.Fixture.arrayFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): Array<T> = mutableListFixture<T>(
    qualifier = qualifier,
    size = size
).toTypedArray()

@Suppress("UNUSED_PARAMETER")
@JvmName("arrayListFixtureAlias")
/**
 * Creates a Array of values for a given type.
 * @param T - the type which is supposed to be created.
 * @param type - the identifying type of the generic.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @param size - the size of the Array.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified T> PublicApi.Fixture.fixture(
    type: KClass<Array<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): Array<T> = arrayFixture(
    qualifier = qualifier,
    size = size
)

/**
 * Creates a Sequence of values for a given type.
 * @param T - the type which is supposed to be created.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @param size - the size of the Sequence.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
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
/**
 * Creates a Sequence of values for a given type.
 * @param T - the type which is supposed to be created.
 * @param C - the enclosing Sequence.
 * @param type - the identifying type of the generic.
 * @param qualifier - a optional qualifier for a special flavour of a type.
 * @param size - the size of the Sequence.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified C : Sequence<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<Sequence<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = sequenceFixture<T>(
    qualifier = qualifier,
    size = size
) as C

/**
 * Creates a Pair of values for given types.
 * @param First - the type which is supposed to be created for the first value.
 * @param Second - the type which is supposed to be created for the second value.
 * @param firstQualifier - a optional qualifier for a special flavour of a type of the first value.
 * @param secondQualifier - a optional qualifier for a special flavour of a type of the second value.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
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
 * @param C - the enclosing Pair.
 * @param type - the identifying type of the generic.
 * @param firstQualifier - a optional qualifier for a special flavour of a type of the first value.
 * @param secondQualifier - a optional qualifier for a special flavour of a type of the second value.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
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
 * @param First - the type which is supposed to be created for the first value.
 * @param Second - the type which is supposed to be created for the second value.
 * @param Third - the type which is supposed to be created for the third value.
 * @param firstQualifier - a optional qualifier for a special flavour of a type of the first value.
 * @param secondQualifier - a optional qualifier for a special flavour of a type of the second value.
 * @param thirdQualifier - a optional qualifier for a special flavour of a type of the third value.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
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
 * @param First - the type which is supposed to be created for the first value.
 * @param Second - the type which is supposed to be created for the second value.
 * @param Third - the type which is supposed to be created for the third value.
 * @param C - the enclosing Triple.
 * @param type - the identifying type of the generic.
 * @param firstQualifier - a optional qualifier for a special flavour of a type of the first value.
 * @param secondQualifier - a optional qualifier for a special flavour of a type of the second value.
 * @param thirdQualifier - a optional qualifier for a special flavour of a type of the third value.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
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

/**
 * Creates a Map of values for given types.
 * @param Key - the type which is supposed to be created for the key value.
 * @param Value - the type which is supposed to be created for the value.
 * @param keyQualifier - a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier - a optional qualifier for a special flavour of a type of the value.
 * @param size - the size of the Map.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
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
/**
 * Creates a Map of values for given types.
 * @param Key - the type which is supposed to be created for the key value.
 * @param Value - the type which is supposed to be created for the value.
 * @param C - the enclosing Map.
 * @param type - the identifying type of the generic.
 * @param keyQualifier - a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier - a optional qualifier for a special flavour of a type of the value.
 * @param size - the size of the Map.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
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

/**
 * Creates a MutableMap of values for given types.
 * @param Key - the type which is supposed to be created for the key value.
 * @param Value - the type which is supposed to be created for the value.
 * @param keyQualifier - a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier - a optional qualifier for a special flavour of a type of the value.
 * @param size - the size of the Map.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
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
/**
 * Creates a MutableMap of values for given types.
 * @param Key - the type which is supposed to be created for the key value.
 * @param Value - the type which is supposed to be created for the value.
 * @param C - the enclosing Map.
 * @param type - the identifying type of the generic.
 * @param keyQualifier - a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier - a optional qualifier for a special flavour of a type of the value.
 * @param size - the size of the Map.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
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

/**
 * Creates a MutableSet of values for given types.
 * @param T - the type which is supposed to be created for the value.
 * @param qualifier - a optional qualifier for a special flavour of a type of the value.
 * @param size - the size of the Set.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
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
/**
 * Creates a MutableSet of values for given types.
 * @param T - the type which is supposed to be created for the value.
 * @param C - the enclosing Set.
 * @param type - the identifying type of the generic.
 * @param qualifier - a optional qualifier for a special flavour of a type of the value.
 * @param size - the size of the Set.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified C : MutableSet<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<MutableSet<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = mutableSetFixture<T>(
    qualifier = qualifier,
    size = size
) as C

/**
 * Creates a Set of values for given types.
 * @param T - the type which is supposed to be created for the value.
 * @param qualifier - a optional qualifier for a special flavour of a type of the value.
 * @param size - the size of the Set.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified T> PublicApi.Fixture.setFixture(
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null
): Set<T> = mutableSetFixture(qualifier, size)

@Suppress("UNUSED_PARAMETER")
@JvmName("setFixtureAlias")
/**
 * Creates a Set of values for given types.
 * @param T - the type which is supposed to be created for the value.
 * @param C - the enclosing Set.
 * @param type - the identifying type of the generic.
 * @param qualifier - a optional qualifier for a special flavour of a type of the value.
 * @param size - the size of the Set.
 * @throws IllegalStateException - if the no matching Generator was found for the given type.
 */
public inline fun <reified C : Set<T>, reified T> PublicApi.Fixture.fixture(
    type: KClass<Set<*>>,
    qualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = setFixture<T>(
    qualifier = qualifier,
    size = size
) as C
