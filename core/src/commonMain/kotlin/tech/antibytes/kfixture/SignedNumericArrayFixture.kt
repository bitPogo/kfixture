/* ktlint-disable filename */
/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import tech.antibytes.kfixture.PublicApi.SignedNumericArrayGenerator

@Throws(IllegalStateException::class)
/**
 * Creates a value for a given Array Number type, excluding generics like List or Array.
 * @param T the type which is supposed to be created.
 * @param sign determines if the resulting number is strict positive or negative
 * @param size determines amount of items.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @param predicate which filters non matching values.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
public inline fun <reified T> PublicApi.Fixture.fixture(
    sign: PublicApi.Sign,
    size: Int,
    qualifier: PublicApi.Qualifier? = null,
    noinline predicate: Function1<T?, Boolean> = ::defaultPredicate,
): T where T : Number? {
    val returnNull = random.returnNull<T>()
    val id = resolveIdentifier<T>(qualifier)
    val generator = generators[id]
    val actualSize = determineCollectionSize(size)

    @Suppress("UNCHECKED_CAST")
    return when {
        generator !is SignedNumericArrayGenerator<*, *> -> {
            throw IllegalStateException("Missing Generator for ClassID ($id).")
        }
        returnNull -> null as T
        else -> (generator as SignedNumericArrayGenerator<Comparable<Any>, *>)
            .generate(
                sign = sign,
                size = actualSize,
                predicate = predicate as Function1<Any?, Boolean>,
            ) as T
    }
}
