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
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.pairFixture
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName

class PairFixtureSpec {
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
    @JsName("fn1")
    fun `Given pairFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.pairFixture<Int, Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn2")
    fun `Given pairFixture is called it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.pairFixture<Int, Int>()

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expected, expected),
        )
    }

    @Test
    @JsName("fn2a")
    fun `Given pairFixture is called with a nested generator for first it returns a Fixture for the derived Type`() {
        // Given
        val expectedFirst = 42
        val expectedSecond = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expectedSecond }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.pairFixture<Int, Int>(
            firstGenerator = { expectedFirst },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expectedFirst, expectedSecond),
        )
    }

    @Test
    @JsName("fn2b")
    fun `Given pairFixture is called with a nested generator for second it returns a Fixture for the derived Type`() {
        // Given
        val expectedFirst = 42
        val expectedSecond = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expectedFirst }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.pairFixture<Int, Int>(
            secondGenerator = { expectedSecond },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expectedFirst, expectedSecond),
        )
    }

    @Test
    @JsName("fn2c")
    fun `Given pairFixture is called with a nested generator for bth it returns a Fixture for the derived Type`() {
        // Given
        val expectedFirst = 42
        val expectedSecond = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.pairFixture(
            firstGenerator = { expectedFirst },
            secondGenerator = { expectedSecond },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expectedFirst, expectedSecond),
        )
    }

    @Test
    @JsName("fn3")
    fun `Given pairFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        generator.generate = { expected }
        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.pairFixture<Int, Int?>()

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expected, null),
        )
    }

    @Test
    @JsName("fn4")
    fun `Given pairFixture is called with qualifiers it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            ),
        )

        // When
        val result = fixture.pairFixture<Int, Int>(
            StringQualifier(keyQualifier),
            StringQualifier(valueQualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expected, expected),
        )
    }

    @Test
    @JsName("fn4a")
    fun `Given pairFixture is called with qualifiers and nested generator for first it returns a Fixture for the derived Type`() {
        // Given
        val expectedFirst = 42
        val expectedSecond = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedSecond }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            ),
        )

        // When
        val result = fixture.pairFixture<Int, Int>(
            firstQualifier = StringQualifier(keyQualifier),
            firstGenerator = { givenQualifier ->
                capturedQualifier = givenQualifier

                expectedFirst
            },
            secondQualifier = StringQualifier(valueQualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expectedFirst, expectedSecond),
        )
        assertEquals(
            actual = capturedQualifier?.value,
            expected = StringQualifier(keyQualifier).value,
        )
    }

    @Test
    @JsName("fn4b")
    fun `Given pairFixture is called with qualifiers and nested generator for second it returns a Fixture for the derived Type`() {
        // Given
        val expectedFirst = 42
        val expectedSecond = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedFirst }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            ),
        )

        // When
        val result = fixture.pairFixture<Int, Int>(
            firstQualifier = StringQualifier(keyQualifier),
            secondQualifier = StringQualifier(valueQualifier),
            secondGenerator = { givenQualifier ->
                capturedQualifier = givenQualifier

                expectedSecond
            },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expectedFirst, expectedSecond),
        )
        assertEquals(
            actual = capturedQualifier?.value,
            expected = StringQualifier(valueQualifier).value,
        )
    }

    @Test
    @JsName("fn4c")
    fun `Given pairFixture is called with qualifiers and nested generator for both it returns a Fixture for the derived Type`() {
        // Given
        val expectedFirst = 42
        val expectedSecond = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedFirstQualifier: PublicApi.Qualifier? = null
        var capturedSecondQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedFirst }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            ),
        )

        // When
        val result = fixture.pairFixture(
            firstQualifier = StringQualifier(keyQualifier),
            firstGenerator = { givenQualifier ->
                capturedFirstQualifier = givenQualifier

                expectedFirst
            },
            secondQualifier = StringQualifier(valueQualifier),
            secondGenerator = { givenQualifier ->
                capturedSecondQualifier = givenQualifier

                expectedSecond
            },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expectedFirst, expectedSecond),
        )
        assertEquals(
            actual = capturedFirstQualifier?.value,
            expected = StringQualifier(keyQualifier).value,
        )
        assertEquals(
            actual = capturedSecondQualifier?.value,
            expected = StringQualifier(valueQualifier).value,
        )
    }

    @Test
    @JsName("fn5")
    fun `Given fixture is called with qualifiers and a Type it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            ),
        )

        // When
        val result: Pair<Int, Int> = fixture.fixture(
            firstQualifier = StringQualifier(keyQualifier),
            secondQualifier = StringQualifier(valueQualifier),
            type = Pair::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expected, expected),
        )
    }

    @Test
    @JsName("fn5a")
    fun `Given fixture is called with qualifiers and nested generator for first it returns a Fixture for the derived Type`() {
        // Given
        val expectedFirst = 42
        val expectedSecond = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedSecond }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            ),
        )

        // When
        val result: Pair<Int, Int> = fixture.fixture(
            type = Pair::class,
            firstQualifier = StringQualifier(keyQualifier),
            firstGenerator = { givenQualifier ->
                capturedQualifier = givenQualifier

                expectedFirst
            },
            secondQualifier = StringQualifier(valueQualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expectedFirst, expectedSecond),
        )
        assertEquals(
            actual = capturedQualifier?.value,
            expected = StringQualifier(keyQualifier).value,
        )
    }

    @Test
    @JsName("fn5b")
    fun `Given fixture is called with qualifiers and nested generator for second it returns a Fixture for the derived Type`() {
        // Given
        val expectedFirst = 42
        val expectedSecond = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedFirst }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            ),
        )

        // When
        val result: Pair<Int, Int> = fixture.fixture(
            firstQualifier = StringQualifier(keyQualifier),
            secondQualifier = StringQualifier(valueQualifier),
            secondGenerator = { givenQualifier ->
                capturedQualifier = givenQualifier

                expectedSecond
            },
            type = Pair::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expectedFirst, expectedSecond),
        )
        assertEquals(
            actual = capturedQualifier?.value,
            expected = StringQualifier(valueQualifier).value,
        )
    }

    @Test
    @JsName("fn5c")
    fun `Given fixture is called with qualifiers and nested generator for both it returns a Fixture for the derived Type`() {
        // Given
        val expectedFirst = 42
        val expectedSecond = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedFirstQualifier: PublicApi.Qualifier? = null
        var capturedSecondQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedFirst }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            ),
        )

        // When
        val result: Pair<Int, Int> = fixture.fixture(
            firstQualifier = StringQualifier(keyQualifier),
            firstGenerator = { givenQualifier ->
                capturedFirstQualifier = givenQualifier

                expectedFirst
            },
            secondQualifier = StringQualifier(valueQualifier),
            secondGenerator = { givenQualifier ->
                capturedSecondQualifier = givenQualifier

                expectedSecond
            },
            type = Pair::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expectedFirst, expectedSecond),
        )
        assertEquals(
            actual = capturedFirstQualifier?.value,
            expected = StringQualifier(keyQualifier).value,
        )
        assertEquals(
            actual = capturedSecondQualifier?.value,
            expected = StringQualifier(valueQualifier).value,
        )
    }
}
