/* ktlint-disable filename */
/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

@Throws(IllegalStateException::class)
/**
 * Creates a value for a given type in given boundaries, excluding generics like List or Array.
 * @param FixtureType the type which is supposed to be created.
 * @param RangeType the type which is supposed to be created and is bounded to its boundaries.
 * @param from the lower boundary of the value.
 * @param to the upper boundary of the value.
 * @param size determines amount of items.
 * @param predicate which filters non matching values.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
public inline fun <reified RangeType, reified FixtureType : RangeType?> PublicApi.Fixture.fixture(
    from: RangeType & Any,
    to: RangeType & Any,
    size: Int,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType {
    val returnNull = random.returnNull<FixtureType>()
    val id = resolveIdentifier<FixtureType>(qualifier)
    val generator = generators[id]

    @Suppress("UNCHECKED_CAST")
    return when {
        generator !is PublicApi.RangedArrayGenerator<*, *> -> {
            throw IllegalStateException("Missing Generator for ClassID ($id).")
        }
        returnNull -> null as FixtureType
        else -> (generator as PublicApi.RangedArrayGenerator<Comparable<Any>, *>).generate(
            from = from as Comparable<Any>,
            to = to as Comparable<Any>,
            size = size,
            predicate = predicate as Function1<Any?, Boolean>,
        ) as FixtureType
    }
}

@InternalAPI
@PublishedApi
internal inline fun <reified RangeType, reified FixtureType : RangeType?> PublicApi.Fixture.guardRangedReturn(
    qualifier: PublicApi.Qualifier? = null,
    resolve: PublicApi.RangedArrayGenerator<RangeType, *>.() -> FixtureType,
): FixtureType where RangeType : Comparable<RangeType> {
    val returnNull = random.returnNull<FixtureType>()
    val id = resolveIdentifier<FixtureType>(qualifier)
    val generator = generators[id]

    @Suppress("UNCHECKED_CAST")
    return when {
        generator !is PublicApi.RangedArrayGenerator<*, *> -> {
            throw IllegalStateException("Missing Generator for ClassID ($id).")
        }
        returnNull -> null as FixtureType
        else -> (generator as PublicApi.RangedArrayGenerator<RangeType, *>).resolve()
    }
}

@Suppress("UNCHECKED_CAST")
@Throws(IllegalStateException::class)
/**
 * Creates a value for a given type in a given range, excluding generics like List or Array.
 * @param FixtureType the type which is supposed to be created.
 * @param RangeType the type which is supposed to be created and is bounded to its boundaries.
 * @param ranges the lower boundary of the value.
 * @param size determines amount of items.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param predicate which filters non matching values.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
public inline fun <reified RangeType, reified FixtureType : RangeType?> PublicApi.Fixture.fixture(
    vararg ranges: ClosedRange<RangeType>,
    size: Int,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType where RangeType : Comparable<RangeType> = guardRangedReturn<RangeType, FixtureType>(qualifier) {
    this.generate(
        ranges = ranges,
        size = size,
        predicate = predicate as Function1<Any?, Boolean>,
    ) as FixtureType
}

@Suppress("UNCHECKED_CAST")
@Throws(IllegalStateException::class)
/**
 * Creates a value for a given type in a given range, excluding generics like List or Array.
 * @param FixtureType the type which is supposed to be created.
 * @param RangeType the type which is supposed to be created and is bounded to its boundaries.
 * @param ranges the lower boundary of the value.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param predicate which filters non matching values.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
public inline fun <reified RangeType, reified FixtureType : RangeType?> PublicApi.Fixture.fixture(
    vararg ranges: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType where RangeType : Comparable<RangeType> = guardRangedReturn<RangeType, FixtureType>(qualifier) {
    this.generate(
        ranges = ranges,
        predicate = predicate as Function1<Any?, Boolean>,
    ) as FixtureType
}
