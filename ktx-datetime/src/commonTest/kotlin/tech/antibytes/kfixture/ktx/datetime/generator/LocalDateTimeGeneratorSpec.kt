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
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.ktx.datetime.KtxDateTimeContract
import tech.antibytes.kfixture.mock.GeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

class LocalDateTimeGeneratorSpec {
    private val instantDependencyGenerator = GeneratorStub<Instant>()
    private val timeZoneDependencyGenerator = GeneratorStub<TimeZone>()

    @AfterTest
    fun tearDown() {
        instantDependencyGenerator.clear()
        timeZoneDependencyGenerator.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils DependentGenerator`() {
        val generator: Any = LocalDateTimeGenerator

        assertTrue(generator is PublicApi.DependentGeneratorFactory<*>)
    }

    @Test
    @JsName("fn1")
    fun `It fulfils Generator`() {
        val instantId = resolveGeneratorId(Instant::class)
        val timeZoneId = resolveGeneratorId(TimeZone::class)
        val generator: Any = LocalDateTimeGenerator.getInstance(
            RandomStub(),
            mapOf(
                instantId to instantDependencyGenerator,
                timeZoneId to timeZoneDependencyGenerator,
            ),
        )

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Test
    @JsName("fn1a")
    fun `It fulfils LocalizedDateTimeGenerator`() {
        val instantId = resolveGeneratorId(Instant::class)
        val timeZoneId = resolveGeneratorId(TimeZone::class)
        val generator: Any = LocalDateTimeGenerator.getInstance(
            RandomStub(),
            mapOf(
                instantId to instantDependencyGenerator,
                timeZoneId to timeZoneDependencyGenerator,
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

        instantDependencyGenerator.generate = { expectedInstant }
        timeZoneDependencyGenerator.generate = { expectedTimeZone }

        // When
        val actual = LocalDateTimeGenerator(
            instantGenerator = instantDependencyGenerator,
            timeZoneGenerator = timeZoneDependencyGenerator,
        ).generate()

        // Then
        assertEquals(
            actual = actual.toInstant(expectedTimeZone),
            expected = expectedInstant,
        )
    }

    @Test
    @JsName("fn3")
    fun `Given generate is called with a InstantGenerator it returns a LocalDateTime`() {
        // Given
        val expectedInstant = Instant.fromEpochMilliseconds(502131)
        val expectedTimeZone = TimeZone.of("Europe/Berlin")

        instantDependencyGenerator.generate = { expectedInstant }
        timeZoneDependencyGenerator.generate = { expectedTimeZone }

        // When
        val actual = LocalDateTimeGenerator(
            instantGenerator = GeneratorStub(),
            timeZoneGenerator = timeZoneDependencyGenerator,
        ).generate(
            instantGenerator = instantDependencyGenerator::generate,
        )

        // Then
        assertEquals(
            actual = actual.toInstant(expectedTimeZone),
            expected = expectedInstant,
        )
    }

    @Test
    @JsName("fn4")
    fun `Given generate is called with a TimeZoneGenerator it returns a LocalDateTime`() {
        // Given
        val expectedInstant = Instant.fromEpochMilliseconds(502131)
        val expectedTimeZone = TimeZone.of("Europe/Berlin")

        instantDependencyGenerator.generate = { expectedInstant }
        timeZoneDependencyGenerator.generate = { expectedTimeZone }

        // When
        val actual = LocalDateTimeGenerator(
            instantGenerator = instantDependencyGenerator,
            timeZoneGenerator = GeneratorStub(),
        ).generate(
            timeZoneGenerator = timeZoneDependencyGenerator::generate,
        )

        // Then
        assertEquals(
            actual = actual.toInstant(expectedTimeZone),
            expected = expectedInstant,
        )
    }

    @Test
    @JsName("fn5")
    fun `Given generate is called with a InstantGenerator and TimeZoneGenerator it returns a LocalDateTime`() {
        // Given
        val expectedInstant = Instant.fromEpochMilliseconds(502131)
        val expectedTimeZone = TimeZone.of("Europe/Berlin")

        instantDependencyGenerator.generate = { expectedInstant }
        timeZoneDependencyGenerator.generate = { expectedTimeZone }

        // When
        val actual = LocalDateTimeGenerator(
            instantGenerator = GeneratorStub(),
            timeZoneGenerator = GeneratorStub(),
        ).generate(
            instantGenerator = instantDependencyGenerator::generate,
            timeZoneGenerator = timeZoneDependencyGenerator::generate,
        )

        // Then
        assertEquals(
            actual = actual.toInstant(expectedTimeZone),
            expected = expectedInstant,
        )
    }
}
