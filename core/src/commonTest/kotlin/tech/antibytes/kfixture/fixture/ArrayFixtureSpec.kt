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
import tech.antibytes.kfixture.arrayFixture
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.int
import tech.antibytes.kfixture.mock.GeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

@Suppress("USELESS_CAST")
class ArrayFixtureSpec {
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
    @JsName("fn6")
    fun `Given arrayFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> 42 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.arrayFixture<Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn7")
    fun `Given arrayFixture is called it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.arrayFixture<Int>()

        // Then
        assertTrue(result is Array<*>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 10,
        )
        assertEquals(
            actual = result.size,
            expected = size,
        )
        assertTrue(
            result.contentDeepEquals(
                arrayOf(
                    expected,
                    expected,
                    expected,
                    expected,
                    expected,
                ),
            ),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn8")
    fun `Given arrayFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.arrayFixture<Int?>()

        // Then
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 10,
        )
        assertEquals(
            actual = result.size,
            expected = size,
        )
        assertTrue(
            result.contentDeepEquals(
                arrayOf<Int?>(
                    null,
                    null,
                    null,
                    null,
                    null,
                ),
            ),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn9")
    fun `Given arrayFixture is called with a qualifier it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val qualifier = "test"
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf("q:$qualifier:$int" to generator))

        // When
        val result = fixture.arrayFixture<Int>(StringQualifier(qualifier))

        // Then
        assertTrue(
            result.contentDeepEquals(
                arrayOf(
                    expected,
                    expected,
                    expected,
                    expected,
                    expected,
                ),
            ),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn10")
    fun `Given arrayFixture is called with a size it returns a Fixture for the derived Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.arrayFixture<Int>(size = size)

        // Then
        assertTrue(
            result.contentDeepEquals(
                arrayOf(
                    expected,
                    expected,
                    expected,
                    expected,
                    expected,
                ),
            ),
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn11")
    fun `Given fixture is called with a size it returns a Fixture for the derived Array Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Array<Int> = fixture.fixture(
            type = Array::class,
        )

        // Then
        assertTrue(
            result.contentDeepEquals(
                arrayOf(
                    expected,
                    expected,
                    expected,
                    expected,
                    expected,
                ),
            ),
        )
    }
}
