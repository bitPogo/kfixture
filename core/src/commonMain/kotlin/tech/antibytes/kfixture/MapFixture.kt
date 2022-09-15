/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("OPT_IN_USAGE")

package tech.antibytes.kfixture

import kotlin.jvm.JvmName
import kotlin.reflect.KClass

/**
 * Creates a Map of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified Key, reified Value> PublicApi.Fixture.mapFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    keyGenerator: Function1<PublicApi.Qualifier?, Key>,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): Map<Key, Value> {
    val actualSize = determineCollectionSize(size)

    return MutableList<Pair<Key, Value>>(actualSize) {
        pairFixture(
            firstQualifier = keyQualifier,
            firstGenerator = keyGenerator,
            secondQualifier = valueQualifier,
        )
    }.toMap()
}

/**
 * Creates a Map of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified Key, reified Value> PublicApi.Fixture.mapFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    valueGenerator: Function1<PublicApi.Qualifier?, Value>,
    size: Int? = null,
): Map<Key, Value> {
    val actualSize = determineCollectionSize(size)

    return MutableList<Pair<Key, Value>>(actualSize) {
        pairFixture(
            firstQualifier = keyQualifier,
            secondQualifier = valueQualifier,
            secondGenerator = valueGenerator,
        )
    }.toMap()
}

/**
 * Creates a Map of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified Key, reified Value> PublicApi.Fixture.mapFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    keyGenerator: Function1<PublicApi.Qualifier?, Key>,
    valueQualifier: PublicApi.Qualifier? = null,
    valueGenerator: Function1<PublicApi.Qualifier?, Value>,
    size: Int? = null,
): Map<Key, Value> {
    val actualSize = determineCollectionSize(size)

    return MutableList(actualSize) {
        pairFixture(
            firstQualifier = keyQualifier,
            firstGenerator = keyGenerator,
            secondQualifier = valueQualifier,
            secondGenerator = valueGenerator,
        )
    }.toMap()
}

/**
 * Creates a Map of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified Key, reified Value> PublicApi.Fixture.mapFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
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
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param C the enclosing Map.
 * @param type the identifying type of the generic.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Map<Key, Value>, reified Key, reified Value> PublicApi.Fixture.fixture(
    type: KClass<Map<*, *>>,
    keyQualifier: PublicApi.Qualifier? = null,
    keyGenerator: Function1<PublicApi.Qualifier?, Key>,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = mapFixture<Key, Value>(
    keyQualifier = keyQualifier,
    keyGenerator = keyGenerator,
    valueQualifier = valueQualifier,
    size = size,
) as C

@Suppress("UNUSED_PARAMETER")
@JvmName("mapFixtureAlias")
/**
 * Creates a Map of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param C the enclosing Map.
 * @param type the identifying type of the generic.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Map<Key, Value>, reified Key, reified Value> PublicApi.Fixture.fixture(
    type: KClass<Map<*, *>>,
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    valueGenerator: Function1<PublicApi.Qualifier?, Value>,
    size: Int? = null,
): C = mapFixture<Key, Value>(
    keyQualifier = keyQualifier,
    valueQualifier = valueQualifier,
    valueGenerator = valueGenerator,
    size = size,
) as C

@Suppress("UNUSED_PARAMETER")
@JvmName("mapFixtureAlias")
/**
 * Creates a Map of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param C the enclosing Map.
 * @param type the identifying type of the generic.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Map<Key, Value>, reified Key, reified Value> PublicApi.Fixture.fixture(
    type: KClass<Map<*, *>>,
    keyQualifier: PublicApi.Qualifier? = null,
    keyGenerator: Function1<PublicApi.Qualifier?, Key>,
    valueQualifier: PublicApi.Qualifier? = null,
    valueGenerator: Function1<PublicApi.Qualifier?, Value>,
    size: Int? = null,
): C = mapFixture(
    keyQualifier = keyQualifier,
    keyGenerator = keyGenerator,
    valueQualifier = valueQualifier,
    valueGenerator = valueGenerator,
    size = size,
) as C

@Suppress("UNUSED_PARAMETER")
@JvmName("mapFixtureAlias")
/**
 * Creates a Map of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param C the enclosing Map.
 * @param type the identifying type of the generic.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Map<Key, Value>, reified Key, reified Value> PublicApi.Fixture.fixture(
    type: KClass<Map<*, *>>,
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = mapFixture<Key, Value>(
    keyQualifier = keyQualifier,
    valueQualifier = valueQualifier,
    size = size,
) as C

/**
 * Creates a MutableMap of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified Key, reified Value> PublicApi.Fixture.mutableMapFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    keyGenerator: Function1<PublicApi.Qualifier?, Key>,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): MutableMap<Key, Value> = mapFixture<Key, Value>(
    keyQualifier = keyQualifier,
    keyGenerator = keyGenerator,
    valueQualifier = valueQualifier,
    size = size,
).toMutableMap()

/**
 * Creates a MutableMap of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified Key, reified Value> PublicApi.Fixture.mutableMapFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    valueGenerator: Function1<PublicApi.Qualifier?, Value>,
    size: Int? = null,
): MutableMap<Key, Value> = mapFixture<Key, Value>(
    keyQualifier = keyQualifier,
    valueQualifier = valueQualifier,
    valueGenerator = valueGenerator,
    size = size,
).toMutableMap()

/**
 * Creates a MutableMap of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified Key, reified Value> PublicApi.Fixture.mutableMapFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    keyGenerator: Function1<PublicApi.Qualifier?, Key>,
    valueQualifier: PublicApi.Qualifier? = null,
    valueGenerator: Function1<PublicApi.Qualifier?, Value>,
    size: Int? = null,
): MutableMap<Key, Value> = mapFixture(
    keyQualifier = keyQualifier,
    keyGenerator = keyGenerator,
    valueQualifier = valueQualifier,
    valueGenerator = valueGenerator,
    size = size,
).toMutableMap()

/**
 * Creates a MutableMap of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified Key, reified Value> PublicApi.Fixture.mutableMapFixture(
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): MutableMap<Key, Value> = mapFixture<Key, Value>(
    keyQualifier = keyQualifier,
    valueQualifier = valueQualifier,
    size = size,
).toMutableMap()

@Suppress("UNUSED_PARAMETER")
@JvmName("mutableMapFixtureAlias")
/**
 * Creates a MutableMap of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param C the enclosing Map.
 * @param type the identifying type of the generic.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : MutableMap<Key, Value>, reified Key, reified Value> PublicApi.Fixture.fixture(
    type: KClass<MutableMap<*, *>>,
    keyQualifier: PublicApi.Qualifier? = null,
    keyGenerator: Function1<PublicApi.Qualifier?, Key>,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = mutableMapFixture<Key, Value>(
    keyQualifier = keyQualifier,
    keyGenerator = keyGenerator,
    valueQualifier = valueQualifier,
    size = size,
) as C

@Suppress("UNUSED_PARAMETER")
@JvmName("mutableMapFixtureAlias")
/**
 * Creates a MutableMap of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param C the enclosing Map.
 * @param type the identifying type of the generic.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : MutableMap<Key, Value>, reified Key, reified Value> PublicApi.Fixture.fixture(
    type: KClass<MutableMap<*, *>>,
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    valueGenerator: Function1<PublicApi.Qualifier?, Value>,
    size: Int? = null,
): C = mutableMapFixture<Key, Value>(
    keyQualifier = keyQualifier,
    valueQualifier = valueQualifier,
    valueGenerator = valueGenerator,
    size = size,
) as C

@Suppress("UNUSED_PARAMETER")
@JvmName("mutableMapFixtureAlias")
/**
 * Creates a MutableMap of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param C the enclosing Map.
 * @param type the identifying type of the generic.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : MutableMap<Key, Value>, reified Key, reified Value> PublicApi.Fixture.fixture(
    type: KClass<MutableMap<*, *>>,
    keyQualifier: PublicApi.Qualifier? = null,
    keyGenerator: Function1<PublicApi.Qualifier?, Key>,
    valueQualifier: PublicApi.Qualifier? = null,
    valueGenerator: Function1<PublicApi.Qualifier?, Value>,
    size: Int? = null,
): C = mutableMapFixture(
    keyQualifier = keyQualifier,
    keyGenerator = keyGenerator,
    valueQualifier = valueQualifier,
    valueGenerator = valueGenerator,
    size = size,
) as C

@Suppress("UNUSED_PARAMETER")
@JvmName("mutableMapFixtureAlias")
/**
 * Creates a MutableMap of values for given types.
 * @param Key the type which is supposed to be created for the key value.
 * @param Value the type which is supposed to be created for the value.
 * @param C the enclosing Map.
 * @param type the identifying type of the generic.
 * @param keyQualifier a optional qualifier for a special flavour of a type of the key value.
 * @param valueQualifier a optional qualifier for a special flavour of a type of the value.
 * @param size the size of the Map.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : MutableMap<Key, Value>, reified Key, reified Value> PublicApi.Fixture.fixture(
    type: KClass<MutableMap<*, *>>,
    keyQualifier: PublicApi.Qualifier? = null,
    valueQualifier: PublicApi.Qualifier? = null,
    size: Int? = null,
): C = mutableMapFixture<Key, Value>(
    keyQualifier = keyQualifier,
    valueQualifier = valueQualifier,
    size = size,
) as C
