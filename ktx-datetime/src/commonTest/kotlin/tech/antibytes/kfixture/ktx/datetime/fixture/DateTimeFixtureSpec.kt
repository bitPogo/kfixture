/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.fixture

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import tech.antibytes.kfixture.mock.Fixture
import tech.antibytes.kfixture.mock.LocalizedDateTimeGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

class DateTimeFixtureSpec {
    private val random = RandomStub()
    private var capturedInstantGenerator: Function0<Instant>? = null
    private var capturedTimeZoneGenerator: Function0<TimeZone>? = null

    @AfterTest
    fun tearDown() {
        random.clear()
        capturedInstantGenerator = null
        capturedTimeZoneGenerator = null
    }

    @Test
    @JsName("fn1")
    fun `Given fixture is called without a timeZoneGenerator and instantGenerator it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = Any()
        val generator = LocalizedDateTimeGeneratorStub<Any>()
        generator.generate = { -> expected }

        // Ensure stable names since reified is in play
        resolveGeneratorId(Any::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Any>(
                instantGenerator = null,
                timeZoneGenerator = null,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID (${resolveGeneratorId(Any::class)}).",
        )
    }

    @Test
    @JsName("fn2")
    fun `Given fixture is called with a timeZoneGenerator and instantGenerator it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = Any()
        val generator = LocalizedDateTimeGeneratorStub<Any>()
        generator.generate = { -> expected }

        // Ensure stable names since reified is in play
        resolveGeneratorId(Any::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Any>(
                instantGenerator = { Instant.fromEpochMilliseconds(123) },
                timeZoneGenerator = { TimeZone.UTC },
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID (${resolveGeneratorId(Any::class)}).",
        )
    }

    @Test
    @JsName("fn4")
    fun `Given fixture is called with a nulled timeZoneGenerator and instantGenerator bound it returns a Fixture for the derived Type`() {
        // Given
        val expected = Any()
        val expectedInstantGenerator = null
        val expectedTimeZoneGenerator = null
        val generator = LocalizedDateTimeGeneratorStub<Any>()

        var capturedInstantGenerator: Function0<Instant>? = { Instant.fromEpochMilliseconds(123) }
        var capturedTimeZoneGenerator: Function0<TimeZone>? = { TimeZone.UTC }

        generator.generateWithGenerators = { givenInstantGenerator, givenTimeZoneGenerator ->
            capturedInstantGenerator = givenInstantGenerator
            capturedTimeZoneGenerator = givenTimeZoneGenerator

            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(Any::class)

        val fixture = Fixture(random, mapOf(resolveGeneratorId(Any::class) to generator))

        // When
        val result: Any = fixture.fixture(
            instantGenerator = expectedInstantGenerator,
            timeZoneGenerator = expectedTimeZoneGenerator,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertNull(capturedInstantGenerator)
        assertNull(capturedTimeZoneGenerator)
    }

    @Test
    @JsName("fn5")
    fun `Given fixture is called with a timeZoneGenerator and instantGenerator bound it returns a Fixture for the derived Type`() {
        // Given
        val expected = Any()
        val expectedInstantGenerator = { Instant.fromEpochMilliseconds(123) }
        val expectedTimeZoneGenerator = { TimeZone.UTC }
        val generator = LocalizedDateTimeGeneratorStub<Any>()

        var capturedInstantGenerator: Function0<Instant>? = null
        var capturedTimeZoneGenerator: Function0<TimeZone>? = null

        generator.generateWithGenerators = { givenInstantGenerator, givenTimeZoneGenerator ->
            capturedInstantGenerator = givenInstantGenerator
            capturedTimeZoneGenerator = givenTimeZoneGenerator

            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(Any::class)

        val fixture = Fixture(random, mapOf(resolveGeneratorId(Any::class) to generator))

        // When
        val result: Any = fixture.fixture(
            instantGenerator = expectedInstantGenerator,
            timeZoneGenerator = expectedTimeZoneGenerator,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedInstantGenerator,
            expected = expectedInstantGenerator,
        )
        assertEquals(
            actual = capturedTimeZoneGenerator,
            expected = expectedTimeZoneGenerator,
        )
    }
}
