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

class TimeZoneGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils GeneratorFactory`() {
        val generator: Any = TimeZoneGenerator

        assertTrue(generator is PublicApi.GeneratorFactory<*>)
    }

    @Test
    @JsName("fn1")
    fun `Given getInstance is called it returns an FilterableGenerator`() {
        val generator: Any = TimeZoneGenerator.getInstance(RandomStub())

        assertTrue(generator is PublicApi.FilterableGenerator<*, *>)
    }

    @Test
    @JsName("fn2")
    fun `Given generate is called it returns a TimeZone`() {
        // Given
        val expected = 10

        var capturedFrom: Int? = null
        var capturedUntil: Int? = null

        random.nextIntRanged = { from, until ->
            capturedFrom = from
            capturedUntil = until

            expected
        }

        // When
        val actual = TimeZoneGenerator(random).generate()

        // Then
        assertEquals(
            actual = actual.toString(),
            expected = "Africa/Johannesburg",
        )
        assertEquals(
            actual = capturedFrom,
            expected = 0,
        )
        assertEquals(
            actual = capturedUntil,
            expected = 596,
        )
    }

    @Test
    @JsName("fn3")
    fun `Given generate is called with a predicate it returns a TimeZone`() {
        // Given
        val randomInts = mutableListOf(10, 23, 42)

        random.nextIntRanged = { _, _ ->
            randomInts.removeFirst()
        }

        // When
        val actual = TimeZoneGenerator(random).generate { zone ->
            zone.toString() == "America/Boa_Vista"
        }

        // Then
        assertEquals(
            actual = actual.toString(),
            expected = "America/Boa_Vista",
        )
    }
}
