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
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.int
import tech.antibytes.kfixture.listFixture
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mutableListFixture
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

class ListFixtureSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils Fixture`() {
        val fixture: Any = Fixture(random, emptyMap())

        assertTrue(fixture is PublicApi.Fixture)
    }

    @Test
    @JsName("fn6")
    fun `Given listFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> 42 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.listFixture<Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn7")
    fun `Given listFixture is called it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedMinimum = -1
        var capturedMaximum = -1

        generator.generate = { expected }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum = givenMinimum
            capturedMaximum = givenMaximum
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.listFixture<Int>()

        // Then
        assertTrue(result is List<*>)
        assertEquals(
            actual = capturedMinimum,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = size,
        )
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn7a")
    fun `Given listFixture is called with a nestedGenerator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedMinimum = -1
        var capturedMaximum = -1

        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum = givenMinimum
            capturedMaximum = givenMaximum
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.listFixture { expected }

        // Then
        assertTrue(result is List<*>)
        assertEquals(
            actual = capturedMinimum,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = size,
        )
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn8")
    fun `Given listFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedMinimum = -1
        var capturedMaximum = -1

        generator.generate = { expected }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum = givenMinimum
            capturedMaximum = givenMaximum
            size
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.listFixture<Int?>()

        // Then
        assertEquals(
            actual = capturedMinimum,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = size,
        )
        assertEquals(
            actual = result,
            expected = listOf(
                null,
                null,
                null,
                null,
                null,
            ),
        )
    }

    @Test
    @JsName("fn9")
    fun `Given listFixture is called with a qualifier it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val qualifier = "test"
        val generator = FilterableGeneratorStub<Int, Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf("q:$qualifier:$int" to generator))

        // When
        val result = fixture.listFixture<Int>(StringQualifier(qualifier))

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn9a")
    fun `Given listFixture is called with a qualifier and a nestedGenerator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val qualifier = "test"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedQualifier: PublicApi.Qualifier? = null

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf("q:$qualifier:$int" to generator))

        // When
        val result = fixture.listFixture(StringQualifier(qualifier)) { givenQualifier ->
            capturedQualifier = givenQualifier

            expected
        }

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
        assertEquals(
            actual = capturedQualifier?.value,
            expected = StringQualifier(qualifier).value,
        )
    }

    @Test
    @JsName("fn10")
    fun `Given listFixture is called with a size it returns a Fixture for the derived Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.listFixture<Int>(size = size)

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn10a")
    fun `Given listFixture is called with a size and a nestedGenerator it returns a Fixture for the derived Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.listFixture(size = size) { expected }

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn11")
    fun `Given fixture is called with a size it returns a Fixture for the derived List Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: List<Int> = fixture.fixture(
            type = List::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn11a")
    fun `Given fixture is called with a size and a nestedGenerator it returns a Fixture for the derived List Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: List<Int> = fixture.fixture(
            type = List::class,
        ) { expected }

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn12")
    fun `Given mutableListFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> 42 }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.mutableListFixture<Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn13")
    fun `Given mutableListFixture is called it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedMinimum = -1
        var capturedMaximum = -1

        generator.generate = { expected }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum = givenMinimum
            capturedMaximum = givenMaximum
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.mutableListFixture<Int>()

        // Then
        assertTrue(result is MutableList<*>)
        assertEquals(
            actual = capturedMinimum,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = size,
        )
        assertEquals(
            actual = result,
            expected = mutableListOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn13a")
    fun `Given mutableListFixture is called and a nestedGenerator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedMinimum = -1
        var capturedMaximum = -1

        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum = givenMinimum
            capturedMaximum = givenMaximum
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.mutableListFixture { expected }

        // Then
        assertTrue(result is MutableList<*>)
        assertEquals(
            actual = capturedMinimum,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = size,
        )
        assertEquals(
            actual = result,
            expected = mutableListOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn14")
    fun `Given mutableListFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedMinimum = -1
        var capturedMaximum = -1

        generator.generate = { expected }
        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum = givenMinimum
            capturedMaximum = givenMaximum
            size
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.mutableListFixture<Int?>()

        // Then
        assertEquals(
            actual = capturedMinimum,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = size,
        )
        assertEquals<MutableList<Int?>>(
            actual = result,
            expected = mutableListOf(
                null,
                null,
                null,
                null,
                null,
            ),
        )
    }

    @Test
    @JsName("fn15")
    fun `Given mutableListFixture is called with a qualifier it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val qualifier = "test"
        val generator = FilterableGeneratorStub<Int, Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf("q:$qualifier:$int" to generator))

        // When
        val result = fixture.mutableListFixture<Int>(StringQualifier(qualifier))

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn15a")
    fun `Given mutableListFixture is called with a qualifier and a nestedGenerator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val qualifier = "test"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedQualifier: PublicApi.Qualifier? = null

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf("q:$qualifier:$int" to generator))

        // When
        val result = fixture.mutableListFixture(StringQualifier(qualifier)) { givenQualifier ->
            capturedQualifier = givenQualifier

            expected
        }

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
        assertEquals(
            actual = capturedQualifier?.value,
            expected = StringQualifier(qualifier).value,
        )
    }

    @Test
    @JsName("fn16")
    fun `Given mutableListFixture is called with a size it returns a Fixture for the derived Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.mutableListFixture<Int>(size = size)

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn16a")
    fun `Given mutableListFixture is called with a size and a nestedGenerator it returns a Fixture for the derived Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.mutableListFixture(size = size) { expected }

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn17")
    fun `Given fixture is called with a size it returns a Fixture for the derived MutableList Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        generator.generate = { expected }
        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: MutableList<Int> = fixture.fixture(
            type = MutableList::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }

    @Test
    @JsName("fn17a")
    fun `Given fixture is called with a size and a nested generator it returns a Fixture for the derived MutableList Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: MutableList<Int> = fixture.fixture(
            type = MutableList::class,
        ) { expected }

        // Then
        assertEquals(
            actual = result,
            expected = listOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }
}
