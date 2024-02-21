/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:OptIn(InternalAPI::class)

package tech.antibytes.kfixture

import kotlin.random.Random

internal class Fixture(
    override val random: Random,
    override val generators: Map<String, PublicApi.Generator<out Any>>,
) : PublicApi.Fixture

/**
 * Factory to instantiate a Fixture Generator.
 * @param configurator - optional action to customize the Fixture Generator
 * @return a Fixture Generator
 */
public fun kotlinFixture(
    configurator: PublicApi.Configuration.() -> Unit = {},
): PublicApi.Fixture {
    val configuration = Configuration()

    configurator.invoke(configuration)

    return configuration.build()
}

/**
 * Picks an value from a given Iterable.
 * @param FixtureType the type which is supposed to be created.
 * @param options - an iterable with values where to pick from.
 */
public fun <FixtureType> PublicApi.Fixture.fixture(options: Iterable<FixtureType>): FixtureType {
    val values = options.toList()

    return values[pickAnIndex(values.size)]
}

/**
 * Creates a value for a given type, excluding generics like List or Array.
 * @param FixtureType the type which is supposed to be created.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified FixtureType> PublicApi.Fixture.fixture(
    qualifier: PublicApi.Qualifier? = null,
): FixtureType {
    val returnNull = random.returnNull<FixtureType>()
    val id = resolveIdentifier<FixtureType>(qualifier)

    return when {
        !generators.containsKey(id) -> throw IllegalStateException("Missing Generator for ClassID ($id).")
        returnNull -> null as FixtureType
        else -> generators[id]!!.generate() as FixtureType
    }
}

/**
 * Creates a value for a given type, excluding generics like List or Array.
 * @param FixtureType the type which is supposed to be created.
 * @param size determines amount of items.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified FixtureType> PublicApi.Fixture.fixture(
    size: Int,
    qualifier: PublicApi.Qualifier? = null,
): FixtureType {
    val returnNull = random.returnNull<FixtureType>()
    val id = resolveIdentifier<FixtureType>(qualifier)
    val generator = generators[id]

    return when {
        generator !is PublicApi.ArrayGenerator<*> -> throw IllegalStateException("Missing Generator for ClassID ($id).")
        returnNull -> null as FixtureType
        else -> generator.generate(size) as FixtureType
    }
}
