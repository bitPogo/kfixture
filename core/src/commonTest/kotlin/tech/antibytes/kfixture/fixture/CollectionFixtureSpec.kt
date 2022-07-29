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
import tech.antibytes.kfixture.collectionFixture
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.int
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mutableCollectionFixture
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

class CollectionFixtureSpec {
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
    fun `Given collectionFixture is called it fails if the Type has no corresponding Generator`() {
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
            fixture.collectionFixture<Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn7")
    fun `Given collectionFixture is called it returns a Fixture for the derived Type`() {
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
        val result: Any = fixture.collectionFixture<Int>()

        // Then
        assertTrue(result is Collection<*>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
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
    fun `Given collectionFixture is called with a nested generator it returns a Fixture for the derived Type`() {
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
        val result: Any = fixture.collectionFixture { expected }

        // Then
        assertTrue(result is Collection<*>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
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
    fun `Given collectionFixture is called it returns a Fixture while respecting nullability`() {
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
        val result = fixture.collectionFixture<Int?>()

        // Then
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = size,
        )
        assertEquals<Collection<Int?>>(
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
    fun `Given collectionFixture is called with a qualifier it returns a Fixture for the derrived Type`() {
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
        val result = fixture.collectionFixture<Int>(StringQualifier(qualifier))

        // Then
        assertEquals<Collection<Any>>(
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
    fun `Given collectionFixture is called with a qualifier and nested generator it returns a Fixture for the derrived Type`() {
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
        val result = fixture.collectionFixture(StringQualifier(qualifier)) { givenQualifier ->
            capturedQualifier = givenQualifier

            expected
        }

        // Then
        assertEquals<Collection<Any>>(
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
    fun `Given collectionFixture is called with a size it returns a Fixture for the derived Type in the given size`() {
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
        val result = fixture.collectionFixture<Int>(size = size)

        // Then
        assertEquals<Collection<Any>>(
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
    fun `Given collectionFixture is called with a size and a nested generator it returns a Fixture for the derived Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.collectionFixture(size = size) { expected }

        // Then
        assertEquals<Collection<Any>>(
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
    fun `Given fixture is called with a size it returns a Fixture for the derived Collection Type in the given size`() {
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
        val result: Collection<Int> = fixture.fixture(
            type = Collection::class,
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
    fun `Given fixture is called with a size and a nested generator it returns a Fixture for the derived Collection Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: Collection<Int> = fixture.fixture(
            type = Collection::class,
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
    fun `Given mutableCollectionFixture is called it fails if the Type has no corresponding Generator`() {
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
            fixture.mutableCollectionFixture<Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn13")
    fun `Given mutableCollectionFixture is called it returns a Fixture for the derived Type`() {
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
        val result: Any = fixture.mutableCollectionFixture<Int>()

        // Then
        assertTrue(result is MutableCollection<*>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
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
    fun `Given mutableCollectionFixture is called with a nested generator it returns a Fixture for the derived Type`() {
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
        val result: Any = fixture.mutableCollectionFixture { expected }

        // Then
        assertTrue(result is MutableCollection<*>)
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
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
    fun `Given mutableCollectionFixture is called it returns a Fixture while respecting nullability`() {
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
        val result = fixture.mutableCollectionFixture<Int?>()

        // Then
        assertEquals(
            actual = capturedMinimum.value,
            expected = 1,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 11,
        )
        assertEquals(
            actual = result.size,
            expected = size,
        )
        assertEquals<MutableCollection<Int?>>(
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
    fun `Given mutableCollectionFixture is called with a qualifier it returns a Fixture for the derrived Type`() {
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
        val result = fixture.mutableCollectionFixture<Int>(StringQualifier(qualifier))

        // Then
        assertEquals<Collection<Any>>(
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
    fun `Given mutableCollectionFixture is called with a qualifier and a nested generator it returns a Fixture for the derrived Type`() {
        // Given
        val size = 5
        val expected = 23
        val qualifier = "test"
        val generator = FilterableGeneratorStub<Int, Int>()

        var captureQualifier: PublicApi.Qualifier? = null

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf("q:$qualifier:$int" to generator))

        // When
        val result = fixture.mutableCollectionFixture(StringQualifier(qualifier)) { givenQualifier ->
            captureQualifier = givenQualifier

            expected
        }

        // Then
        assertEquals<Collection<Any>>(
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
            actual = captureQualifier?.value,
            expected = StringQualifier(qualifier).value,
        )
    }

    @Test
    @JsName("fn16")
    fun `Given mutableCollectionFixture is called with a size it returns a Fixture for the derived Type in the given size`() {
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
        val result = fixture.mutableCollectionFixture<Int>(size = size)

        // Then
        assertEquals<Collection<Any>>(
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
    fun `Given mutableCollectionFixture is called with a size and a nested generator it returns a Fixture for the derived Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.mutableCollectionFixture(size = size) { expected }

        // Then
        assertEquals<Collection<Any>>(
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
    fun `Given fixture is called with a size it returns a Fixture for the derived MutableCollection Type in the given size`() {
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
        val result: MutableCollection<Int> = fixture.fixture(
            type = MutableCollection::class,
        )

        // Then
        assertEquals<Collection<Any>>(
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
    fun `Given fixture is called with a size and a nested generator it returns a Fixture for the derived MutableCollection Type in the given size`() {
        // Given
        val size = 5
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        random.nextIntRanged = { _, _ -> size }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result: MutableCollection<Int> = fixture.fixture(
            type = MutableCollection::class,
        ) { expected }

        // Then
        assertEquals<Collection<Any>>(
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
