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
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mutableSetFixture
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName
import tech.antibytes.kfixture.setFixture

class SetFixtureSpec {
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
    @JsName("fn6")
    fun `Given setFixture is called it fails if the Type has no corresponding Generator`() {
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
            fixture.setFixture<Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn7")
    fun `Given setFixture is called it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

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
        val result: Any = fixture.setFixture<Int>()

        // Then
        assertTrue(result is Set<*>)
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
            expected = 1, // it is a set and kills doublets
        )
        assertEquals(
            actual = result,
            expected = setOf(
                expected,
            ),
        )
    }

    @Test
    @JsName("fn7a")
    fun `Given setFixture is called with a nested generator it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.setFixture { expected }

        // Then
        assertTrue(result is Set<*>)
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
            expected = 1, // it is a set and kills doublets
        )
        assertEquals(
            actual = result,
            expected = setOf(
                expected,
            ),
        )
    }

    @Test
    @JsName("fn8")
    fun `Given setFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

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
        val result = fixture.setFixture<Int?>()

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
            expected = 1, // it is a set and kills doublets
        )
        assertEquals(
            actual = result,
            expected = setOf(
                null,
            ),
        )
    }

    @Test
    @JsName("fn9")
    fun `Given setFixture is called with a qualifier it returns a Fixture for the derrived Type`() {
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
        val result = fixture.setFixture<Int>(StringQualifier(qualifier))

        // Then
        assertEquals(
            actual = result,
            expected = setOf(
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
    fun `Given setFixture is called with a qualifier and a nested generator it returns a Fixture for the derrived Type`() {
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
        val result = fixture.setFixture(StringQualifier(qualifier)) { givenQualifier ->
            capturedQualifier = givenQualifier

            expected
        }

        // Then
        assertEquals(
            actual = result,
            expected = setOf(
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
    fun `Given setFixture is called with a size it returns a Fixture for the derived Type in the given size`() {
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
        val result = fixture.setFixture<Int>(size = size)

        // Then
        assertEquals(
            actual = result,
            expected = setOf(
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
    fun `Given setFixture is called with a size and a nested Generator it returns a Fixture for the derived Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.setFixture(size = size) { expected }

        // Then
        assertEquals(
            actual = result,
            expected = setOf(
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
    fun `Given fixture is called with a size it returns a Fixture for the derived Set Type in the given size`() {
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
        val result: Set<Int> = fixture.fixture(
            type = Set::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = setOf(
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
    fun `Given fixture is called with a size and a nested generator it returns a Fixture for the derived Set Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Set<Int> = fixture.fixture(
            type = Set::class,
        ) { expected }

        // Then
        assertEquals(
            actual = result,
            expected = setOf(
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
    fun `Given mutableSetFixture is called it fails if the Type has no corresponding Generator`() {
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
            fixture.mutableSetFixture<Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn13")
    fun `Given mutableSetFixture is called it returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

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
        val result: Any = fixture.mutableSetFixture<Int>()

        // Then
        assertTrue(result is MutableSet<*>)
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
            expected = 1, // it is a set and kills doublets
        )
        assertEquals(
            actual = result,
            expected = mutableSetOf(
                expected,
            ),
        )
    }

    @Test
    @JsName("fn13a")
    fun `Given mutableSetFixture is called it and nested generator returns a Fixture for the derived Type`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)
            size
        }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Any = fixture.mutableSetFixture { expected }

        // Then
        assertTrue(result is MutableSet<*>)
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
            expected = 1, // it is a set and kills doublets
        )
        assertEquals(
            actual = result,
            expected = mutableSetOf(
                expected,
            ),
        )
    }

    @Test
    @JsName("fn14")
    fun `Given mutableSetFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

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
        val result = fixture.mutableSetFixture<Int?>()

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
            expected = 1, // it is a set and kill doublets
        )
        assertEquals<MutableSet<Int?>>(
            actual = result,
            expected = mutableSetOf(
                null,
            ),
        )
    }

    @Test
    @JsName("fn15")
    fun `Given mutableSetFixture is called with a qualifier it returns a Fixture for the derrived Type`() {
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
        val result = fixture.mutableSetFixture<Int>(StringQualifier(qualifier))

        // Then
        assertEquals(
            actual = result,
            expected = setOf(
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
    fun `Given mutableSetFixture is called with a qualifier and nested generator it returns a Fixture for the derrived Type`() {
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
        val result = fixture.mutableSetFixture(StringQualifier(qualifier)) { givenQualifier ->
            capturedQualifier = givenQualifier

            expected
        }

        // Then
        assertEquals(
            actual = result,
            expected = setOf(
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
    fun `Given mutableSetFixture is called with a size it returns a Fixture for the derived Type in the given size`() {
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
        val result = fixture.mutableSetFixture<Int>(size = size)

        // Then
        assertEquals(
            actual = result,
            expected = setOf(
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
    fun `Given mutableSetFixture is called with a size and a nested generator it returns a Fixture for the derived Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.mutableSetFixture(size = size) { expected }

        // Then
        assertEquals(
            actual = result,
            expected = setOf(
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
    fun `Given fixture is called with a size it returns a Fixture for the derived MutableSet Type in the given size`() {
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
        val result: MutableSet<Int> = fixture.fixture(
            type = MutableSet::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = setOf(
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
    fun `Given fixture is called with a size and a nested generator it returns a Fixture for the derived MutableSet Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: MutableSet<Int> = fixture.fixture(
            type = MutableSet::class,
        ) { expected }

        // Then
        assertEquals(
            actual = result,
            expected = setOf(
                expected,
                expected,
                expected,
                expected,
                expected,
            ),
        )
    }
}
