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
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.SignedNumericArrayGeneratorStub
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

class SignedNumericArrayFixtureSpec {
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
    @JsName("fn0")
    fun `It fulfils Fixture`() {
        val fixture: Any = Fixture(random, emptyMap())

        assertTrue(fixture is PublicApi.Fixture)
    }

    @Test
    @JsName("fn1")
    fun `Given fixture is called with a upper and lower bound it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = SignedNumericArrayGeneratorStub<Int, Int>()
        generator.generateWithSign = { _, _ -> expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int>(sign = PublicApi.Sign.POSITIVE, size = 23)
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn2")
    fun `Given fixture is called with a upper and lower bound it fails the corresponding Generator is not a RangedGenerator`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.fixture<Int>(sign = PublicApi.Sign.POSITIVE, size = 24)
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn3")
    fun `Given fixture is called with a upper and lower bound it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedSize = 12
        val expectedSign = PublicApi.Sign.NEGATIVE
        val generator = SignedNumericArrayGeneratorStub<Int, Int>()

        var capturedSign: PublicApi.Sign? = null
        var capturedSize: Int? = null

        generator.generateWithSign = { givenType, givenSize ->
            capturedSign = givenType
            capturedSize = givenSize

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture(sign = expectedSign, size = expectedSize)

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedSign,
            expected = expectedSign,
        )
        assertEquals(
            actual = capturedSize,
            expected = expectedSize,
        )
    }

    @Test
    @JsName("fn4")
    fun `Given fixture is called with a upper and lower bound it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val expectedSign = PublicApi.Sign.NEGATIVE
        val expectedSize = 42
        val generator = SignedNumericArrayGeneratorStub<Int, Int>()

        var capturedSign: PublicApi.Sign? = null
        var capturedSize: Int? = null

        generator.generateWithSign = { givenType, givenSize ->
            capturedSign = givenType
            capturedSize = givenSize

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture(sign = expectedSign, size = expectedSize)

        // Then
        assertNull(result)
        assertNull(capturedSign)
        assertNull(capturedSize)
    }

    @Test
    @JsName("fn5")
    fun `Given fixture is called  with a upper and lower bound and a qualifier it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedSign = PublicApi.Sign.NEGATIVE
        val expectedSize = 42
        val qualifier = "test"
        val generator = SignedNumericArrayGeneratorStub<Int, Int>()

        var capturedSign: PublicApi.Sign? = null
        var capturedSize: Int? = null

        generator.generateWithSign = { givenType, givenSize ->
            capturedSign = givenType
            capturedSize = givenSize

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
            size = expectedSize,
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
        assertEquals(
            actual = capturedSize,
            expected = expectedSize,
        )
    }
}
