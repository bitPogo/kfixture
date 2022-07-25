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
 * @param predicate which filters the the generated instances.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
public inline fun <reified RangeType, reified FixtureType : RangeType?> PublicApi.Fixture.fixture(
    from: RangeType & Any,
    to: RangeType & Any,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: (FixtureType) -> Boolean = { true },
): FixtureType {
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
            predicate = predicate as (Any?) -> Boolean,
        ) as FixtureType
    }
}

@Throws(IllegalStateException::class)
/**
 * Creates a value for a given type in a given range, excluding generics like List or Array.
 * @param FixtureType the type which is supposed to be created.
 * @param RangeType the type which is supposed to be created and is bounded to its boundaries.
 * @param range the lower boundary of the value.
 * @param predicate which filters the the generated instances.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
public inline fun <reified RangeType, reified FixtureType : RangeType?> PublicApi.Fixture.fixture(
    range: ClosedRange<RangeType>,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: (FixtureType) -> Boolean = { true },
): FixtureType where RangeType : Comparable<RangeType> = fixture(
    from = range.start,
    to = range.endInclusive,
    predicate = predicate,
    qualifier = qualifier,
)
