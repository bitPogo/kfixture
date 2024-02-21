/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("OPT_IN_USAGE", "ktlint:standard:filename")

package tech.antibytes.kfixture.ktx.datetime.fixture

import kotlin.reflect.KClass
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.ktx.datetime.KtxDateTimeContract.LocalizedDateTimeGenerator
import tech.antibytes.kfixture.qualifier.resolveGeneratorId
import tech.antibytes.kfixture.returnNull

@Throws(IllegalStateException::class)
/**
 * Creates a value for a given type in given boundaries, excluding generics like List or Array.
 * @param FixtureType the type which is supposed to be created.
 * @param instantGenerator nested generator to create an Instant.
 * @param timeZoneGenerator nested generator to select an TimeZone.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
public inline fun <reified FixtureType : Any?> PublicApi.Fixture.fixture(noinline instantGenerator: Function0<Instant>? = null, noinline timeZoneGenerator: Function0<TimeZone>? = null, qualifier: PublicApi.Qualifier? = null): FixtureType {
    val returnNull = random.returnNull<FixtureType>()
    val id = resolveGeneratorId(
        clazz = FixtureType::class as KClass<*>,
        qualifier = qualifier,
    )
    val generator = generators[id]

    return when {
        generator !is LocalizedDateTimeGenerator<*> -> throw IllegalStateException("Missing Generator for ClassID ($id).")
        returnNull -> null as FixtureType
        else -> generator.generate(
            instantGenerator = instantGenerator,
            timeZoneGenerator = timeZoneGenerator,
        ) as FixtureType
    }
}
