/* ktlint-disable filename */
/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

@Throws(IllegalStateException::class)
/**
 * Creates a value for a given numeric type, excluding generics like List or Array.
 * @param T the type which is supposed to be created.
 * @param sign determines if the resulting number is strict positive or negative
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
public inline fun <reified T> PublicApi.Fixture.fixture(
    sign: PublicApi.Sign,
    qualifier: PublicApi.Qualifier? = null,
): T where T : Number? {
    val returnNull = random.returnNull<T>()
    val id = resolveIdentifier<T>(qualifier)
    val generator = generators[id]

    @Suppress("UNCHECKED_CAST")
    return when {
        generator !is PublicApi.SignedNumberGenerator<*> -> {
            throw IllegalStateException("Missing Generator for ClassID ($id).")
        }
        returnNull -> null as T
        else -> (generator as PublicApi.SignedNumberGenerator<Comparable<Any>>).generate(sign) as T
    }
}
