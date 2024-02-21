/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.jvm.JvmName
import kotlin.reflect.KClass
import tech.antibytes.kfixture.PublicApi.Qualifier

/**
 * Creates a Pair of values for given types.
 * @param First - the type which is supposed to be created for the first value.
 * @param Second - the type which is supposed to be created for the second value.
 * @param firstQualifier - a optional qualifier for a special flavour of a type of the first value.
 * @param firstGenerator a generator (like fixture) which can be delegated for customization.
 * @param secondQualifier - a optional qualifier for a special flavour of a type of the second value.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified First, reified Second> PublicApi.Fixture.pairFixture(
    firstQualifier: Qualifier? = null,
    firstGenerator: Function1<Qualifier?, First>,
    secondQualifier: Qualifier? = null,
): Pair<First, Second> = firstGenerator.invoke(firstQualifier) to fixture(secondQualifier)

/**
 * Creates a Pair of values for given types.
 * @param First - the type which is supposed to be created for the first value.
 * @param Second - the type which is supposed to be created for the second value.
 * @param firstQualifier - a optional qualifier for a special flavour of a type of the first value.
 * @param secondQualifier - a optional qualifier for a special flavour of a type of the second value.
 * @param secondGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified First, reified Second> PublicApi.Fixture.pairFixture(
    firstQualifier: Qualifier? = null,
    secondQualifier: Qualifier? = null,
    secondGenerator: Function1<Qualifier?, Second>,
): Pair<First, Second> = fixture<First>(firstQualifier) to secondGenerator.invoke(secondQualifier)

/**
 * Creates a Pair of values for given types.
 * @param First - the type which is supposed to be created for the first value.
 * @param Second - the type which is supposed to be created for the second value.
 * @param firstQualifier - a optional qualifier for a special flavour of a type of the first value.
 * @param firstGenerator a generator (like fixture) which can be delegated for customization.
 * @param secondQualifier - a optional qualifier for a special flavour of a type of the second value.
 * @param secondGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified First, reified Second> PublicApi.Fixture.pairFixture(
    firstQualifier: Qualifier? = null,
    firstGenerator: Function1<Qualifier?, First>,
    secondQualifier: Qualifier? = null,
    secondGenerator: Function1<Qualifier?, Second>,
): Pair<First, Second> = firstGenerator.invoke(firstQualifier) to secondGenerator.invoke(secondQualifier)

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
    firstQualifier: Qualifier? = null,
    secondQualifier: Qualifier? = null,
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
 * @param firstGenerator a generator (like fixture) which can be delegated for customization.
 * @param secondQualifier - a optional qualifier for a special flavour of a type of the second value.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Pair<First, Second>, reified First, reified Second> PublicApi.Fixture.fixture(type: KClass<Pair<*, *>>, firstQualifier: Qualifier? = null, firstGenerator: Function1<Qualifier?, First>, secondQualifier: Qualifier? = null): C = pairFixture<First, Second>(
    firstQualifier = firstQualifier,
    firstGenerator = firstGenerator,
    secondQualifier = secondQualifier,
) as C

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
 * @param secondGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Pair<First, Second>, reified First, reified Second> PublicApi.Fixture.fixture(type: KClass<Pair<*, *>>, firstQualifier: Qualifier? = null, secondQualifier: Qualifier? = null, secondGenerator: Function1<Qualifier?, Second>): C = pairFixture<First, Second>(
    firstQualifier = firstQualifier,
    secondQualifier = secondQualifier,
    secondGenerator = secondGenerator,
) as C

@Suppress("UNUSED_PARAMETER")
@JvmName("pairFixtureAlias")
/**
 * Creates a Pair of values for given types.
 * @param First - the type which is supposed to be created for the first value.
 * @param Second - the type which is supposed to be created for the second value.
 * @param C the enclosing Pair.
 * @param type the identifying type of the generic.
 * @param firstQualifier - a optional qualifier for a special flavour of a type of the first value.
 * @param firstGenerator a generator (like fixture) which can be delegated for customization.
 * @param secondQualifier - a optional qualifier for a special flavour of a type of the second value.
 * @param secondGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Pair<First, Second>, reified First, reified Second> PublicApi.Fixture.fixture(type: KClass<Pair<*, *>>, firstQualifier: Qualifier? = null, firstGenerator: Function1<Qualifier?, First>, secondQualifier: Qualifier? = null, secondGenerator: Function1<Qualifier?, Second>): C = pairFixture(
    firstQualifier = firstQualifier,
    firstGenerator = firstGenerator,
    secondQualifier = secondQualifier,
    secondGenerator = secondGenerator,
) as C

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
public inline fun <reified C : Pair<First, Second>, reified First, reified Second> PublicApi.Fixture.fixture(type: KClass<Pair<*, *>>, firstQualifier: Qualifier? = null, secondQualifier: Qualifier? = null): C = pairFixture<First, Second>(
    firstQualifier = firstQualifier,
    secondQualifier = secondQualifier,
) as C

/**
 * Creates a Triple of values for given types.
 * @param First the type which is supposed to be created for the first value.
 * @param Second the type which is supposed to be created for the second value.
 * @param Third the type which is supposed to be created for the third value.
 * @param firstQualifier a optional qualifier for a special flavour of a type of the first value.
 * @param firstGenerator a generator (like fixture) which can be delegated for customization.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified First, reified Second, reified Third> PublicApi.Fixture.tripleFixture(
    firstQualifier: Qualifier? = null,
    firstGenerator: Function1<Qualifier?, First>,
    secondQualifier: Qualifier? = null,
    thirdQualifier: Qualifier? = null,
): Triple<First, Second, Third> {
    return Triple(
        firstGenerator.invoke(firstQualifier),
        fixture(secondQualifier),
        fixture(thirdQualifier),
    )
}

/**
 * Creates a Triple of values for given types.
 * @param First the type which is supposed to be created for the first value.
 * @param Second the type which is supposed to be created for the second value.
 * @param Third the type which is supposed to be created for the third value.
 * @param firstQualifier a optional qualifier for a special flavour of a type of the first value.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param secondGenerator a generator (like fixture) which can be delegated for customization.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified First, reified Second, reified Third> PublicApi.Fixture.tripleFixture(
    firstQualifier: Qualifier? = null,
    secondQualifier: Qualifier? = null,
    secondGenerator: Function1<Qualifier?, Second>,
    thirdQualifier: Qualifier? = null,
): Triple<First, Second, Third> {
    return Triple(
        fixture(firstQualifier),
        secondGenerator.invoke(secondQualifier),
        fixture(thirdQualifier),
    )
}

/**
 * Creates a Triple of values for given types.
 * @param First the type which is supposed to be created for the first value.
 * @param Second the type which is supposed to be created for the second value.
 * @param Third the type which is supposed to be created for the third value.
 * @param firstQualifier a optional qualifier for a special flavour of a type of the first value.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @param thirdGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified First, reified Second, reified Third> PublicApi.Fixture.tripleFixture(
    firstQualifier: Qualifier? = null,
    secondQualifier: Qualifier? = null,
    thirdQualifier: Qualifier? = null,
    thirdGenerator: Function1<Qualifier?, Third>,
): Triple<First, Second, Third> {
    return Triple(
        fixture(firstQualifier),
        fixture(secondQualifier),
        thirdGenerator.invoke(thirdQualifier),
    )
}

/**
 * Creates a Triple of values for given types.
 * @param First the type which is supposed to be created for the first value.
 * @param Second the type which is supposed to be created for the second value.
 * @param Third the type which is supposed to be created for the third value.
 * @param firstQualifier a optional qualifier for a special flavour of a type of the first value.
 * @param firstGenerator a generator (like fixture) which can be delegated for customization.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param secondGenerator a generator (like fixture) which can be delegated for customization.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified First, reified Second, reified Third> PublicApi.Fixture.tripleFixture(
    firstQualifier: Qualifier? = null,
    firstGenerator: Function1<Qualifier?, First>,
    secondQualifier: Qualifier? = null,
    secondGenerator: Function1<Qualifier?, Second>,
    thirdQualifier: Qualifier? = null,
): Triple<First, Second, Third> {
    return Triple(
        firstGenerator.invoke(firstQualifier),
        secondGenerator.invoke(secondQualifier),
        fixture(thirdQualifier),
    )
}

/**
 * Creates a Triple of values for given types.
 * @param First the type which is supposed to be created for the first value.
 * @param Second the type which is supposed to be created for the second value.
 * @param Third the type which is supposed to be created for the third value.
 * @param firstQualifier a optional qualifier for a special flavour of a type of the first value.
 * @param firstGenerator a generator (like fixture) which can be delegated for customization.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @param thirdGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified First, reified Second, reified Third> PublicApi.Fixture.tripleFixture(
    firstQualifier: Qualifier? = null,
    firstGenerator: Function1<Qualifier?, First>,
    secondQualifier: Qualifier? = null,
    thirdQualifier: Qualifier? = null,
    thirdGenerator: Function1<Qualifier?, Third>,
): Triple<First, Second, Third> {
    return Triple(
        firstGenerator.invoke(firstQualifier),
        fixture(secondQualifier),
        thirdGenerator.invoke(thirdQualifier),
    )
}

/**
 * Creates a Triple of values for given types.
 * @param First the type which is supposed to be created for the first value.
 * @param Second the type which is supposed to be created for the second value.
 * @param Third the type which is supposed to be created for the third value.
 * @param firstQualifier a optional qualifier for a special flavour of a type of the first value.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param secondGenerator a generator (like fixture) which can be delegated for customization.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @param thirdGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified First, reified Second, reified Third> PublicApi.Fixture.tripleFixture(
    firstQualifier: Qualifier? = null,
    secondQualifier: Qualifier? = null,
    secondGenerator: Function1<Qualifier?, Second>,
    thirdQualifier: Qualifier? = null,
    thirdGenerator: Function1<Qualifier?, Third>,
): Triple<First, Second, Third> {
    return Triple(
        fixture(firstQualifier),
        secondGenerator.invoke(secondQualifier),
        thirdGenerator.invoke(thirdQualifier),
    )
}

/**
 * Creates a Triple of values for given types.
 * @param First the type which is supposed to be created for the first value.
 * @param Second the type which is supposed to be created for the second value.
 * @param Third the type which is supposed to be created for the third value.
 * @param firstQualifier a optional qualifier for a special flavour of a type of the first value.
 * @param firstGenerator a generator (like fixture) which can be delegated for customization.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param secondGenerator a generator (like fixture) which can be delegated for customization.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @param thirdGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified First, reified Second, reified Third> PublicApi.Fixture.tripleFixture(
    firstQualifier: Qualifier? = null,
    firstGenerator: Function1<Qualifier?, First>,
    secondQualifier: Qualifier? = null,
    secondGenerator: Function1<Qualifier?, Second>,
    thirdQualifier: Qualifier? = null,
    thirdGenerator: Function1<Qualifier?, Third>,
): Triple<First, Second, Third> {
    return Triple(
        firstGenerator.invoke(firstQualifier),
        secondGenerator.invoke(secondQualifier),
        thirdGenerator.invoke(thirdQualifier),
    )
}

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
    firstQualifier: Qualifier? = null,
    secondQualifier: Qualifier? = null,
    thirdQualifier: Qualifier? = null,
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
 * @param firstGenerator a generator (like fixture) which can be delegated for customization.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Triple<First, Second, Third>, reified First, reified Second, reified Third> PublicApi.Fixture.fixture(type: KClass<Triple<*, *, *>>, firstQualifier: Qualifier? = null, firstGenerator: Function1<Qualifier?, First>, secondQualifier: Qualifier? = null, thirdQualifier: Qualifier? = null): C = tripleFixture<First, Second, Third>(
    firstQualifier = firstQualifier,
    firstGenerator = firstGenerator,
    secondQualifier = secondQualifier,
    thirdQualifier = thirdQualifier,
) as C

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
 * @param secondGenerator a generator (like fixture) which can be delegated for customization.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Triple<First, Second, Third>, reified First, reified Second, reified Third> PublicApi.Fixture.fixture(type: KClass<Triple<*, *, *>>, firstQualifier: Qualifier? = null, secondQualifier: Qualifier? = null, secondGenerator: Function1<Qualifier?, Second>, thirdQualifier: Qualifier? = null): C = tripleFixture<First, Second, Third>(
    firstQualifier = firstQualifier,
    secondQualifier = secondQualifier,
    secondGenerator = secondGenerator,
    thirdQualifier = thirdQualifier,
) as C

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
 * @param thirdGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Triple<First, Second, Third>, reified First, reified Second, reified Third> PublicApi.Fixture.fixture(type: KClass<Triple<*, *, *>>, firstQualifier: Qualifier? = null, secondQualifier: Qualifier? = null, thirdQualifier: Qualifier? = null, thirdGenerator: Function1<Qualifier?, Third>): C = tripleFixture<First, Second, Third>(
    firstQualifier = firstQualifier,
    secondQualifier = secondQualifier,
    thirdQualifier = thirdQualifier,
    thirdGenerator = thirdGenerator,
) as C

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
 * @param firstGenerator a generator (like fixture) which can be delegated for customization.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param secondGenerator a generator (like fixture) which can be delegated for customization.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Triple<First, Second, Third>, reified First, reified Second, reified Third> PublicApi.Fixture.fixture(type: KClass<Triple<*, *, *>>, firstQualifier: Qualifier? = null, firstGenerator: Function1<Qualifier?, First>, secondQualifier: Qualifier? = null, secondGenerator: Function1<Qualifier?, Second>, thirdQualifier: Qualifier? = null): C = tripleFixture<First, Second, Third>(
    firstQualifier = firstQualifier,
    firstGenerator = firstGenerator,
    secondQualifier = secondQualifier,
    secondGenerator = secondGenerator,
    thirdQualifier = thirdQualifier,
) as C

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
 * @param firstGenerator a generator (like fixture) which can be delegated for customization.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @param thirdGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Triple<First, Second, Third>, reified First, reified Second, reified Third> PublicApi.Fixture.fixture(type: KClass<Triple<*, *, *>>, firstQualifier: Qualifier? = null, firstGenerator: Function1<Qualifier?, First>, secondQualifier: Qualifier? = null, thirdQualifier: Qualifier? = null, thirdGenerator: Function1<Qualifier?, Third>): C = tripleFixture<First, Second, Third>(
    firstQualifier = firstQualifier,
    firstGenerator = firstGenerator,
    secondQualifier = secondQualifier,
    thirdQualifier = thirdQualifier,
    thirdGenerator = thirdGenerator,
) as C

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
 * @param secondGenerator a generator (like fixture) which can be delegated for customization.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @param thirdGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Triple<First, Second, Third>, reified First, reified Second, reified Third> PublicApi.Fixture.fixture(type: KClass<Triple<*, *, *>>, firstQualifier: Qualifier? = null, secondQualifier: Qualifier? = null, secondGenerator: Function1<Qualifier?, Second>, thirdQualifier: Qualifier? = null, thirdGenerator: Function1<Qualifier?, Third>): C = tripleFixture<First, Second, Third>(
    firstQualifier = firstQualifier,
    secondQualifier = secondQualifier,
    secondGenerator = secondGenerator,
    thirdQualifier = thirdQualifier,
    thirdGenerator = thirdGenerator,
) as C

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
 * @param firstGenerator a generator (like fixture) which can be delegated for customization.
 * @param secondQualifier a optional qualifier for a special flavour of a type of the second value.
 * @param secondGenerator a generator (like fixture) which can be delegated for customization.
 * @param thirdQualifier a optional qualifier for a special flavour of a type of the third value.
 * @param thirdGenerator a generator (like fixture) which can be delegated for customization.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified C : Triple<First, Second, Third>, reified First, reified Second, reified Third> PublicApi.Fixture.fixture(type: KClass<Triple<*, *, *>>, firstQualifier: Qualifier? = null, firstGenerator: Function1<Qualifier?, First>, secondQualifier: Qualifier? = null, secondGenerator: Function1<Qualifier?, Second>, thirdQualifier: Qualifier? = null, thirdGenerator: Function1<Qualifier?, Third>): C = tripleFixture(
    firstQualifier = firstQualifier,
    firstGenerator = firstGenerator,
    secondQualifier = secondQualifier,
    secondGenerator = secondGenerator,
    thirdQualifier = thirdQualifier,
    thirdGenerator = thirdGenerator,
) as C

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
public inline fun <reified C : Triple<First, Second, Third>, reified First, reified Second, reified Third> PublicApi.Fixture.fixture(type: KClass<Triple<*, *, *>>, firstQualifier: Qualifier? = null, secondQualifier: Qualifier? = null, thirdQualifier: Qualifier? = null): C = tripleFixture<First, Second, Third>(
    firstQualifier = firstQualifier,
    secondQualifier = secondQualifier,
    thirdQualifier = thirdQualifier,
) as C
