/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:filename")

package tech.antibytes.kfixture

@InternalAPI
@Throws(IllegalStateException::class)
/**
 * Creates a value for a given type in given boundaries, excluding generics like List or Array.
 * This method is internal - usage is at your own risk.
 * @param FixtureType the type which is supposed to be created.
 * @param RangeType the type which is supposed to be created and is bounded to its boundaries.
 * @param from the lower boundary of the value (inclusive).
 * @param to the upper boundary of the value (inclusive).
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param predicate which filters non matching values.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
public inline fun <reified RangeType, reified FixtureType> PublicApi.Fixture.rangedFixture(from: RangeType & Any, to: RangeType & Any, qualifier: PublicApi.Qualifier?, noinline predicate: Function1<RangeType?, Boolean>): FixtureType {
    val returnNull = random.returnNull<FixtureType>()
    val id = resolveIdentifier<FixtureType>(qualifier)
    val generator = generators[id]

    @Suppress("UNCHECKED_CAST")
    return when {
        generator !is PublicApi.RangedGenerator<*, *> -> {
            throw IllegalStateException("Missing Generator for ClassID ($id).")
        }
        returnNull -> null as FixtureType
        else -> (generator as PublicApi.RangedGenerator<Comparable<Any>, *>).generate(
            from = from as Comparable<Any>,
            to = to as Comparable<Any>,
            predicate = predicate as Function1<Any?, Boolean>,
        ) as FixtureType
    }
}

@OptIn(InternalAPI::class)
@Throws(IllegalStateException::class)
/**
 * Creates a value for a given type in given boundaries, excluding generics like List or Array.
 * @param FixtureType the type which is supposed to be created.
 * @param RangeType the type which is supposed to be created and is bounded to its boundaries.
 * @param from the lower boundary of the value (inclusive).
 * @param to the upper boundary of the value (inclusive).
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param predicate which filters non matching values.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
public inline fun <reified RangeType, reified FixtureType : RangeType?> PublicApi.Fixture.fixture(from: RangeType & Any, to: RangeType & Any, qualifier: PublicApi.Qualifier? = null, noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@Throws(IllegalStateException::class)
/**
 * Creates a value for a given type in a given range, excluding generics like List or Array.
 * @param FixtureType the type which is supposed to be created.
 * @param RangeType the type which is supposed to be created and is bounded to its boundaries.
 * @param range the lower boundary of the value.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param predicate which filters non matching values.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
public inline fun <reified RangeType, reified FixtureType : RangeType?> PublicApi.Fixture.fixture(range: ClosedRange<RangeType>, qualifier: PublicApi.Qualifier? = null, noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate): FixtureType where RangeType : Comparable<RangeType> = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)
