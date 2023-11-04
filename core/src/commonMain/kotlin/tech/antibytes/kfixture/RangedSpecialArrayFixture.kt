/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("FINAL_UPPER_BOUND", "OPT_IN_USAGE", "ktlint:standard:filename")

package tech.antibytes.kfixture

import kotlin.jvm.JvmName

@JvmName("rangedCharFixture")
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
public inline fun <reified RangeType : Char, reified FixtureType> PublicApi.Fixture.fixture(
    from: RangeType,
    to: RangeType,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@JvmName("rangedByteFixture")
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
public inline fun <reified RangeType : Byte, reified FixtureType> PublicApi.Fixture.fixture(
    from: RangeType,
    to: RangeType,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@JvmName("rangedShortFixture")
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
public inline fun <reified RangeType : Short, reified FixtureType> PublicApi.Fixture.fixture(
    from: RangeType,
    to: RangeType,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@JvmName("rangedIntFixture")
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
public inline fun <reified RangeType : Int, reified FixtureType> PublicApi.Fixture.fixture(
    from: RangeType,
    to: RangeType,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@JvmName("rangedFloatFixture")
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
public inline fun <reified RangeType : Float, reified FixtureType> PublicApi.Fixture.fixture(
    from: RangeType,
    to: RangeType,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@JvmName("rangedLongFixture")
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
public inline fun <reified RangeType : Long, reified FixtureType> PublicApi.Fixture.fixture(
    from: RangeType,
    to: RangeType,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@JvmName("rangedDoubleFixture")
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
public inline fun <reified RangeType : Double, reified FixtureType> PublicApi.Fixture.fixture(
    from: RangeType,
    to: RangeType,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@JvmName("rangedUByteFixture")
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
public inline fun <reified RangeType : UByte, reified FixtureType> PublicApi.Fixture.fixture(
    from: RangeType,
    to: RangeType,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@JvmName("rangedUShortFixture")
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
public inline fun <reified RangeType : UShort, reified FixtureType> PublicApi.Fixture.fixture(
    from: RangeType,
    to: RangeType,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@JvmName("rangedUIntFixture")
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
public inline fun <reified RangeType : UInt, reified FixtureType> PublicApi.Fixture.fixture(
    from: RangeType,
    to: RangeType,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@JvmName("rangedULongFixture")
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
public inline fun <reified RangeType : ULong, reified FixtureType> PublicApi.Fixture.fixture(
    from: RangeType,
    to: RangeType,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@JvmName("closedRangedCharFixture")
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
public inline fun <reified RangeType : Char, reified FixtureType> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)

@JvmName("closedRangedByteFixture")
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
public inline fun <reified RangeType : Byte, reified FixtureType> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)

@JvmName("closedRangedShortFixture")
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
public inline fun <reified RangeType : Short, reified FixtureType> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)

@JvmName("closedRangedIntFixture")
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
public inline fun <reified RangeType : Int, reified FixtureType> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)

@JvmName("closedRangedFloatFixture")
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
public inline fun <reified RangeType : Float, reified FixtureType> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)

@JvmName("closedRangedLongFixture")
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
public inline fun <reified RangeType : Long, reified FixtureType> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)

@JvmName("closedRangedDoubleFixture")
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
public inline fun <reified RangeType : Double, reified FixtureType> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)

@JvmName("closedRangedUByteFixture")
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
public inline fun <reified RangeType : UByte, reified FixtureType> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)

@JvmName("closedRangedUShortFixture")
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
public inline fun <reified RangeType : UShort, reified FixtureType> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)

@JvmName("closedRangedUIntFixture")
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
public inline fun <reified RangeType : UInt, reified FixtureType> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)

@JvmName("closedRangedULongFixture")
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
public inline fun <reified RangeType : ULong, reified FixtureType> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<RangeType?, Boolean> = ::defaultPredicate,
): FixtureType = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)
