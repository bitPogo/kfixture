/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub

class ShallowTimeZoneGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils GeneratorFactory`() {
        val generator: Any = ShallowTimeZoneGenerator

        assertTrue(generator is PublicApi.GeneratorFactory<*>)
    }

    @Test
    @JsName("fn1")
    fun `Given getInstance is called it returns an FilterableGenerator`() {
        val generator: Any = ShallowTimeZoneGenerator.getInstance(RandomStub())

        assertTrue(generator is PublicApi.FilterableGenerator<*, *>)
    }

    @Test
    @JsName("fn2")
    fun `Given generate is called it returns a TimeZone`() {
        // When
        val actual = ShallowTimeZoneGenerator().generate()

        // Then
        assertEquals(
            actual = actual.toString(),
            expected = "Z",
        )
    }

    @Test
    @JsName("fn3")
    fun `Given generate is called with a predicate it returns a TimeZone while ignoring the predicate`() {
        // Given
        val randomInts = mutableListOf(10, 23, 42)

        random.nextIntRanged = { _, _ ->
            randomInts.removeFirst()
        }

        // When
        val actual = ShallowTimeZoneGenerator().generate { zone ->
            zone.toString().isEmpty()
        }

        // Then
        assertEquals(
            actual = actual.toString(),
            expected = "Z",
        )
    }
}
