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
import kotlin.test.assertTrue
import kotlinx.atomicfu.atomic
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.int
import tech.antibytes.kfixture.mock.GeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName
import tech.antibytes.kfixture.tripleFixture

@Suppress("USELESS_CAST")
class TripleFixtureSpec {
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
    fun `Given tripleFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.tripleFixture<Int, Int, Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given tripleFixture is called it returns a Fixture for the derrived Type`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.tripleFixture<Int, Int, Int>()

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expected, expected, expected),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given tripleFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int, Int>()

        generator.generate = { expected }
        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.tripleFixture<Int, Int?, Int>()

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expected, null, expected),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given tripleFixture is called with qualifiers it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val generator = GeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(
                "q:$firstQualifier:$int" to generator,
                "q:$secondQualifier:$int" to generator,
                "q:$thirdQualifier:$int" to generator,
            ),
        )

        // When
        val result = fixture.tripleFixture<Int, Int, Int>(
            StringQualifier(firstQualifier),
            StringQualifier(secondQualifier),
            StringQualifier(thirdQualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expected, expected, expected),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given fixture is called with qualifiers and a Type it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val generator = GeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(
                "q:$firstQualifier:$int" to generator,
                "q:$secondQualifier:$int" to generator,
                "q:$thirdQualifier:$int" to generator,
            ),
        )

        // When
        val result: Triple<Int, Int, Int> = fixture.fixture(
            type = Triple::class,
            StringQualifier(firstQualifier),
            StringQualifier(secondQualifier),
            StringQualifier(thirdQualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expected, expected, expected),
        )
    }
}
