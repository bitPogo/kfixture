/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.ktx.datetime.KtxDateTimeContract
import tech.antibytes.kfixture.mock.DateTimeGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

class LocalTimeGeneratorSpec {
    private val dateTimeDependencyGenerator = DateTimeGeneratorStub<LocalDateTime>()

    @AfterTest
    fun tearDown() {
        dateTimeDependencyGenerator.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils DependentGenerator`() {
        val generator: Any = LocalTimeGenerator

        assertTrue(generator is PublicApi.DependentGeneratorFactory<*>)
    }

    @Test
    @JsName("fn1")
    fun `It fulfils Generator`() {
        val dateTimeId = resolveGeneratorId(LocalDateTime::class)
        val generator: Any = LocalTimeGenerator.getInstance(
            RandomStub(),
            mapOf(
                dateTimeId to dateTimeDependencyGenerator,
            ),
        )

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Test
    @JsName("fn1a")
    fun `It fulfils LocalizedDateTimeGenerator`() {
        val dateTimeId = resolveGeneratorId(LocalDateTime::class)
        val generator: Any = LocalTimeGenerator.getInstance(
            RandomStub(),
            mapOf(
                dateTimeId to dateTimeDependencyGenerator,
            ),
        )

        assertTrue(generator is KtxDateTimeContract.LocalizedDateTimeGenerator<*>)
    }

    @Test
    @JsName("fn2")
    fun `Given generate is called it returns a LocalDateTime`() {
        // Given
        val expectedInstant = Instant.fromEpochMilliseconds(502131)
        val expectedTimeZone = TimeZone.of("Europe/Berlin")

        var capturedInstantGenerator: Function0<Instant>? = null
        var capturedTimeZoneGenerator: Function0<TimeZone>? = null

        dateTimeDependencyGenerator.generateWithGenerators = { givenInstantGenerator, givenTimZoneGenerator ->
            capturedInstantGenerator = givenInstantGenerator
            capturedTimeZoneGenerator = givenTimZoneGenerator

            expectedInstant.toLocalDateTime(expectedTimeZone)
        }

        // When
        val actual = LocalTimeGenerator(
            dateTimeGenerator = dateTimeDependencyGenerator,
        ).generate()

        // Then
        assertEquals(
            actual = actual,
            expected = expectedInstant.toLocalDateTime(expectedTimeZone).time,
        )
        assertNull(capturedInstantGenerator)
        assertNull(capturedTimeZoneGenerator)
    }

    @Test
    @JsName("fn3")
    fun `Given generate is called with a InstantGenerator and a TimeZoneGenerate it delegates them and returns a LocalDateTime`() {
        // Given
        val expectedInstant = Instant.fromEpochMilliseconds(502131)
        val expectedTimeZone = TimeZone.of("Europe/Berlin")

        val instantGenerator = { expectedInstant }
        val timeZoneGenerator = { expectedTimeZone }

        var capturedInstantGenerator: Function0<Instant>? = null
        var capturedTimeZoneGenerator: Function0<TimeZone>? = null

        dateTimeDependencyGenerator.generateWithGenerators = { givenInstantGenerator, givenTimZoneGenerator ->
            capturedInstantGenerator = givenInstantGenerator
            capturedTimeZoneGenerator = givenTimZoneGenerator

            expectedInstant.toLocalDateTime(expectedTimeZone)
        }

        // When
        val actual = LocalTimeGenerator(
            dateTimeGenerator = dateTimeDependencyGenerator,
        ).generate(
            instantGenerator = instantGenerator,
            timeZoneGenerator = timeZoneGenerator,
        )

        // Then
        assertEquals(
            actual = actual,
            expected = expectedInstant.toLocalDateTime(expectedTimeZone).time,
        )
        assertSame(
            actual = capturedInstantGenerator,
            expected = instantGenerator,
        )
        assertSame(
            actual = capturedTimeZoneGenerator,
            expected = timeZoneGenerator,
        )
    }
}
