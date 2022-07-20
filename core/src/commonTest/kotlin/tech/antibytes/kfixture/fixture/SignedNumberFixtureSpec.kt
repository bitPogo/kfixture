/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.fixture

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.atomicfu.atomic
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.int
import tech.antibytes.kfixture.mock.GeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.SignedNumberGeneratorStub
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

class SignedNumberFixtureSpec {
    private val random = RandomStub()
    private val capturedMinimum = atomic(-1)
    private val capturedMaximum = atomic(-1)

    @AfterTest
    fun tearDown() {
        random.clear()
        capturedMinimum.getAndSet(-1)
        capturedMaximum.getAndSet(-1)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils Fixture`() {
        val fixture: Any = Fixture(random, emptyMap())

        assertTrue(fixture is PublicApi.Fixture)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given fixture is called with a upper and lower bound it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = SignedNumberGeneratorStub<Int>()
        generator.generateWithSign = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int>(sign = PublicApi.Sign.POSITIVE)
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given fixture is called with a upper and lower bound it fails the corresponding Generator is not a RangedGenerator`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int>(sign = PublicApi.Sign.POSITIVE)
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given fixture is called with a upper and lower bound it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedSign = PublicApi.Sign.NEGATIVE
        val generator = SignedNumberGeneratorStub<Int>()

        var capturedSign: PublicApi.Sign? = null

        generator.generateWithSign = { givenType ->
            capturedSign = givenType

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture(sign = expectedSign)

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedSign,
            expected = expectedSign,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given fixture is called with a upper and lower bound it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val expectedSign = PublicApi.Sign.NEGATIVE
        val generator = SignedNumberGeneratorStub<Int>()

        var capturedSign: PublicApi.Sign? = null

        generator.generateWithSign = { givenType ->
            capturedSign = givenType

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture(sign = expectedSign)

        // Then
        assertNull(result)
        assertNull(capturedSign)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given fixture is called  with a upper and lower bound and a qualifier it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedSign = PublicApi.Sign.NEGATIVE
        val qualifier = "test"
        val generator = SignedNumberGeneratorStub<Int>()

        var capturedSign: PublicApi.Sign? = null

        generator.generateWithSign = { givenType ->
            capturedSign = givenType

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$int" to generator),
        )

        // When
        val result: Int = fixture.fixture(
            sign = expectedSign,
            qualifier = StringQualifier(qualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedSign,
            expected = expectedSign,
        )
    }
}
