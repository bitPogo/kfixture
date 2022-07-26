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
import kotlinx.atomicfu.update
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.int
import tech.antibytes.kfixture.mock.ArrayGeneratorStub
import tech.antibytes.kfixture.mock.GeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

@Suppress("USELESS_CAST")
class AtomicFixtureSpec {
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
    fun `Given fixture is called it fails if the Type has no corresponding Generator`() {
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
            fixture.fixture<Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given fixture is called it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture()

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given fixture is called with size it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val expectedSize = 42
        val generator = ArrayGeneratorStub<Int, Int>()

        var capturedSize: Int? = null

        generator.generate = { givenSize ->
            capturedSize = givenSize

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int = fixture.fixture(size = expectedSize)

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedSize,
            expected = expectedSize,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given fixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int, Int>()

        random.nextBoolean = { true }
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture()

        // Then
        assertNull(result)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given fixture is called with size it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val expectedSize = 42
        val generator = ArrayGeneratorStub<Int, Int>()

        var capturedSize: Int? = null

        generator.generate = { givenSize ->
            capturedSize = givenSize

            expected
        }
        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Int? = fixture.fixture(size = expectedSize)

        // Then
        assertNull(result)
        assertNull(capturedSize)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given fixture is called with a qualifier it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val qualifier = "test"
        val generator = GeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$int" to generator),
        )

        // When
        val result: Int = fixture.fixture(StringQualifier(qualifier))

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn7")
    fun `Given fixture is called with a qualifier and size it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val qualifier = "test"
        val expectedSize = 42
        val generator = ArrayGeneratorStub<Int, Int>()

        var capturedSize: Int? = null

        generator.generate = { givenSize ->
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
        val result: Int = fixture.fixture(qualifier = StringQualifier(qualifier), size = expectedSize)

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedSize,
            expected = expectedSize,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn8")
    fun `Given fixture is called it returns a type for a Number Type`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int, Int>()
        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> 2 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(int to generator),
        )

        // When
        val result: Number = fixture.fixture()

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn9")
    fun `Given fixture is called with size it returns a type for a Number Type`() {
        // Given
        val expected = 23
        val expectedSize = 42
        val generator = ArrayGeneratorStub<Int, Int>()

        var capturedSize: Int? = null

        generator.generate = { givenSize ->
            capturedSize = givenSize

            expected
        }
        random.nextIntRanged = { _, _ -> 2 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(int to generator),
        )

        // When
        val result: Number = fixture.fixture(size = expectedSize)

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedSize,
            expected = expectedSize,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn10")
    fun `Given fixture is called with a qualifier it returns a type for a Number Type`() {
        // Given
        val expected = 23
        val qualifier = "test"
        val generator = GeneratorStub<Int, Int>()
        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> 2 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$int" to generator),
        )

        // When
        val result: Number = fixture.fixture(StringQualifier(qualifier))

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn11")
    fun `Given fixture is called with a qualifier and size it returns a type for a Number Type`() {
        // Given
        val expected = 23
        val qualifier = "test"
        val expectedSize = 42
        val generator = ArrayGeneratorStub<Int, Int>()

        var capturedSize: Int? = null

        generator.generate = { givenSize ->
            capturedSize = givenSize

            expected
        }
        random.nextIntRanged = { _, _ -> 2 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$int" to generator),
        )

        // When
        val result: Number = fixture.fixture(qualifier = StringQualifier(qualifier), size = expectedSize)

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedSize,
            expected = expectedSize,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn12")
    fun `Given fixture is called with Iterable it returns a random picked item out of it`() {
        // Given
        random.nextIntRanged = { givenLower, givenUpper ->
            capturedMinimum.update { givenLower }
            capturedMaximum.update { givenUpper }
            1
        }

        val fixture = Fixture(
            random,
            emptyMap(),
        )

        // When
        val result = fixture.fixture(options = 0..23)

        // Then
        assertEquals(
            actual = result,
            expected = 1,
        )
        assertEquals(
            actual = capturedMinimum.value,
            expected = 0,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 24,
        )
    }
}
