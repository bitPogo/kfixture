/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.random.Random
import kotlinx.atomicfu.atomic

internal class Fixture(
    override val random: Random,
    generators: Map<String, PublicApi.Generator<out Any>>,
) : PublicApi.Fixture {
    private val _generators = atomic(generators)

    override val generators: Map<String, PublicApi.Generator<out Any>> by _generators
}

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
 * @param T the type which is supposed to be created.
 * @param options - an iterable with values where to pick from.
 */
public fun <T> PublicApi.Fixture.fixture(options: Iterable<T>): T {
    val values = options.toList()

    return values[pickAnIndex(values.size)]
}

/**
 * Creates a value for a given type, excluding generics like List or Array.
 * @param T the type which is supposed to be created.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.fixture(
    qualifier: PublicApi.Qualifier? = null,
): T {
    val returnNull = random.returnNull<T>()
    val id = resolveIdentifier<T>(qualifier)

    return when {
        !generators.containsKey(id) -> throw IllegalStateException("Missing Generator for ClassID ($id).")
        returnNull -> null as T
        else -> generators[id]!!.generate() as T
    }
}

/**
 * Creates a value for a given type, excluding generics like List or Array.
 * @param T the type which is supposed to be created.
 * @param size determines amount of items.
 * @param qualifier a optional qualifier for a special flavour of a type.
 * @throws IllegalStateException if the no matching Generator was found for the given type.
 */
@Throws(IllegalStateException::class)
public inline fun <reified T> PublicApi.Fixture.fixture(
    size: Int,
    qualifier: PublicApi.Qualifier? = null,
): T {
    val returnNull = random.returnNull<T>()
    val id = resolveIdentifier<T>(qualifier)
    val generator = generators[id]

    return when {
        generator !is PublicApi.ArrayGenerator<*> -> throw IllegalStateException("Missing Generator for ClassID ($id).")
        returnNull -> null as T
        else -> generator.generate(size) as T
    }
}
