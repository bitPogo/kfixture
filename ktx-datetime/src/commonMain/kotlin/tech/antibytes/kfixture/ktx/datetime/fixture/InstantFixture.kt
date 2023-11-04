/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:filename")

package tech.antibytes.kfixture.ktx.datetime.fixture

import kotlin.jvm.JvmName
import kotlinx.datetime.Instant
import tech.antibytes.kfixture.InternalAPI
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.rangedFixture

@OptIn(InternalAPI::class)
@JvmName("rangedInstantFixture")
@Suppress("FINAL_UPPER_BOUND")
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
public inline fun <reified RangeType : Long, reified FixtureType : Instant?> PublicApi.Fixture.fixture(
    from: RangeType,
    to: RangeType,
    qualifier: PublicApi.Qualifier?,
    noinline predicate: Function1<RangeType?, Boolean>,
): FixtureType = rangedFixture(
    from = from,
    to = to,
    qualifier = qualifier,
    predicate = predicate,
)

@JvmName("closedRangedInstantFixture")
@Suppress("FINAL_UPPER_BOUND")
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
public inline fun <reified RangeType : Long, reified FixtureType : Instant?> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier?,
    noinline predicate: Function1<RangeType?, Boolean>,
): FixtureType = fixture(
    from = range.start,
    to = range.endInclusive,
    qualifier = qualifier,
    predicate = predicate,
)
