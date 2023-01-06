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
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName
import tech.antibytes.kfixture.tripleFixture

class TripleFixtureSpec {
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
    fun `Given tripleFixture is called it fails if the Type has no corresponding Generator`() {
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
            fixture.tripleFixture<Int, Int, Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int).",
        )
    }

    @Test
    @JsName("fn2")
    fun `Given tripleFixture is called it returns a Fixture for the derrived Type`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()
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
    @JsName("fn2a")
    fun `Given tripleFixture is called it with a nestedGenerator for first returns a Fixture for the derrived Type`() {
        // Given
        val expectedFirst = 42
        val expectedRest = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expectedRest }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.tripleFixture<Int, Int, Int>(
            firstGenerator = { expectedFirst },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedRest, expectedRest),
        )
    }

    @Test
    @JsName("fn2b")
    fun `Given tripleFixture is called it with a nestedGenerator for second returns a Fixture for the derrived Type`() {
        // Given
        val expectedSecond = 42
        val expectedRest = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expectedRest }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.tripleFixture<Int, Int, Int>(
            secondGenerator = { expectedSecond },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedRest, expectedSecond, expectedRest),
        )
    }

    @Test
    @JsName("fn2c")
    fun `Given tripleFixture is called it with a nestedGenerator for third returns a Fixture for the derrived Type`() {
        // Given
        val expectedThird = 42
        val expectedRest = 23
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expectedRest }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.tripleFixture<Int, Int, Int>(
            thirdGenerator = { expectedThird },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedRest, expectedRest, expectedThird),
        )
    }

    @Test
    @JsName("fn2d")
    fun `Given tripleFixture is called it with a nestedGenerator for first and second returns a Fixture for the derrived Type`() {
        // Given
        val expectedFirst = 41
        val expectedSecond = 23
        val expectedThird = 42
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expectedThird }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.tripleFixture<Int, Int, Int>(
            firstGenerator = { expectedFirst },
            secondGenerator = { expectedSecond },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedSecond, expectedThird),
        )
    }

    @Test
    @JsName("fn2e")
    fun `Given tripleFixture is called it with a nestedGenerator for first and third returns a Fixture for the derrived Type`() {
        // Given
        val expectedFirst = 41
        val expectedSecond = 23
        val expectedThird = 42
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expectedSecond }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.tripleFixture<Int, Int, Int>(
            firstGenerator = { expectedFirst },
            thirdGenerator = { expectedThird },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedSecond, expectedThird),
        )
    }

    @Test
    @JsName("fn2f")
    fun `Given tripleFixture is called it with a nestedGenerator for second and third returns a Fixture for the derrived Type`() {
        // Given
        val expectedFirst = 41
        val expectedSecond = 23
        val expectedThird = 42
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expectedFirst }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.tripleFixture<Int, Int, Int>(
            secondGenerator = { expectedSecond },
            thirdGenerator = { expectedThird },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedSecond, expectedThird),
        )
    }

    @Test
    @JsName("fn2g")
    fun `Given tripleFixture is called it with a nestedGenerator for first and second and third returns a Fixture for the derrived Type`() {
        // Given
        val expectedFirst = 41
        val expectedSecond = 23
        val expectedThird = 42
        val generator = FilterableGeneratorStub<Int, Int>()
        generator.generate = { expectedSecond }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random, mapOf(int to generator))

        // When
        val result = fixture.tripleFixture(
            firstGenerator = { expectedFirst },
            secondGenerator = { expectedSecond },
            thirdGenerator = { expectedThird },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedSecond, expectedThird),
        )
    }

    @Test
    @JsName("fn3")
    fun `Given tripleFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val generator = FilterableGeneratorStub<Int, Int>()

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
    @JsName("fn4")
    fun `Given tripleFixture is called with qualifiers it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val generator = FilterableGeneratorStub<Int, Int>()
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
            firstQualifier = StringQualifier(firstQualifier),
            secondQualifier = StringQualifier(secondQualifier),
            thirdQualifier = StringQualifier(thirdQualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expected, expected, expected),
        )
    }

    @Test
    @JsName("fn4a")
    fun `Given tripleFixture is called it with qualifiers and a nestedGenerator for first returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedFirst = 42
        val expectedRest = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedRest }

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
            firstQualifier = StringQualifier(firstQualifier),
            firstGenerator = { givenQualifier ->
                capturedQualifier = givenQualifier

                expectedFirst
            },
            secondQualifier = StringQualifier(secondQualifier),
            thirdQualifier = StringQualifier(thirdQualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedRest, expectedRest),
        )
        assertEquals(
            actual = capturedQualifier?.value,
            expected = StringQualifier(firstQualifier).value,
        )
    }

    @Test
    @JsName("fn4b")
    fun `Given tripleFixture is called it with qualifiers and a nestedGenerator for second returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedSecond = 42
        val expectedRest = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedRest }

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
            firstQualifier = StringQualifier(firstQualifier),
            secondGenerator = { givenQualifier ->
                capturedQualifier = givenQualifier

                expectedSecond
            },
            secondQualifier = StringQualifier(secondQualifier),
            thirdQualifier = StringQualifier(thirdQualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedRest, expectedSecond, expectedRest),
        )
        assertEquals(
            actual = capturedQualifier?.value,
            expected = StringQualifier(secondQualifier).value,
        )
    }

    @Test
    @JsName("fn4c")
    fun `Given tripleFixture is called it with qualifiers and a nestedGenerator for third returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedThird = 42
        val expectedRest = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedRest }

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
            firstQualifier = StringQualifier(firstQualifier),
            secondQualifier = StringQualifier(secondQualifier),
            thirdQualifier = StringQualifier(thirdQualifier),
            thirdGenerator = { givenQualifier ->
                capturedQualifier = givenQualifier

                expectedThird
            },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedRest, expectedRest, expectedThird),
        )
        assertEquals(
            actual = capturedQualifier?.value,
            expected = StringQualifier(thirdQualifier).value,
        )
    }

    @Test
    @JsName("fn4d")
    fun `Given tripleFixture is called it with qualifier and a nestedGenerator for first and second returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedFirst = 41
        val expectedSecond = 23
        val expectedThird = 42
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedFirstQualifier: PublicApi.Qualifier? = null
        var capturedSecondQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedThird }

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
            firstQualifier = StringQualifier(firstQualifier),
            firstGenerator = { givenQualifier ->
                capturedFirstQualifier = givenQualifier

                expectedFirst
            },
            secondGenerator = { givenQualifier ->
                capturedSecondQualifier = givenQualifier

                expectedSecond
            },
            secondQualifier = StringQualifier(secondQualifier),
            thirdQualifier = StringQualifier(thirdQualifier),
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedSecond, expectedThird),
        )
        assertEquals(
            actual = capturedFirstQualifier?.value,
            expected = StringQualifier(firstQualifier).value,
        )
        assertEquals(
            actual = capturedSecondQualifier?.value,
            expected = StringQualifier(secondQualifier).value,
        )
    }

    @Test
    @JsName("fn4e")
    fun `Given tripleFixture is called it with qualifier and a nestedGenerator for first and third returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedFirst = 41
        val expectedSecond = 23
        val expectedThird = 42
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedFirstQualifier: PublicApi.Qualifier? = null
        var capturedThirdQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedSecond }

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
            firstQualifier = StringQualifier(firstQualifier),
            firstGenerator = { givenQualifier ->
                capturedFirstQualifier = givenQualifier

                expectedFirst
            },
            secondQualifier = StringQualifier(secondQualifier),
            thirdQualifier = StringQualifier(thirdQualifier),
            thirdGenerator = { givenQualifier ->
                capturedThirdQualifier = givenQualifier

                expectedThird
            },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedSecond, expectedThird),
        )
        assertEquals(
            actual = capturedFirstQualifier?.value,
            expected = StringQualifier(firstQualifier).value,
        )
        assertEquals(
            actual = capturedThirdQualifier?.value,
            expected = StringQualifier(thirdQualifier).value,
        )
    }

    @Test
    @JsName("fn4f")
    fun `Given tripleFixture is called it with qualifier and a nestedGenerator for second and third returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedFirst = 41
        val expectedSecond = 23
        val expectedThird = 42
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedSecondQualifier: PublicApi.Qualifier? = null
        var capturedThirdQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedFirst }

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
            firstQualifier = StringQualifier(firstQualifier),
            secondQualifier = StringQualifier(secondQualifier),
            secondGenerator = { givenQualifier ->
                capturedSecondQualifier = givenQualifier

                expectedSecond
            },
            thirdQualifier = StringQualifier(thirdQualifier),
            thirdGenerator = { givenQualifier ->
                capturedThirdQualifier = givenQualifier

                expectedThird
            },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedSecond, expectedThird),
        )
        assertEquals(
            actual = capturedSecondQualifier?.value,
            expected = StringQualifier(secondQualifier).value,
        )
        assertEquals(
            actual = capturedThirdQualifier?.value,
            expected = StringQualifier(thirdQualifier).value,
        )
    }

    @Test
    @JsName("fn4g")
    fun `Given tripleFixture is called it with qualifier and a nestedGenerator for first and second and third returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedFirst = 41
        val expectedSecond = 23
        val expectedThird = 42
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedFirstQualifier: PublicApi.Qualifier? = null
        var capturedSecondQualifier: PublicApi.Qualifier? = null
        var capturedThirdQualifier: PublicApi.Qualifier? = null

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
        val result = fixture.tripleFixture(
            firstQualifier = StringQualifier(firstQualifier),
            firstGenerator = { givenQualifier ->
                capturedFirstQualifier = givenQualifier

                expectedFirst
            },
            secondQualifier = StringQualifier(secondQualifier),
            secondGenerator = { givenQualifier ->
                capturedSecondQualifier = givenQualifier

                expectedSecond
            },
            thirdQualifier = StringQualifier(thirdQualifier),
            thirdGenerator = { givenQualifier ->
                capturedThirdQualifier = givenQualifier

                expectedThird
            },
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedSecond, expectedThird),
        )
        assertEquals(
            actual = capturedFirstQualifier?.value,
            expected = StringQualifier(firstQualifier).value,
        )
        assertEquals(
            actual = capturedSecondQualifier?.value,
            expected = StringQualifier(secondQualifier).value,
        )
        assertEquals(
            actual = capturedThirdQualifier?.value,
            expected = StringQualifier(thirdQualifier).value,
        )
    }

    @Test
    @JsName("fn5")
    fun `Given fixture is called with qualifiers and a Type it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val generator = FilterableGeneratorStub<Int, Int>()
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

    @Test
    @JsName("fn5a")
    fun `Given fixture is called it with qualifiers and a nestedGenerator for first returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedFirst = 52
        val expectedRest = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedRest }

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
            firstQualifier = StringQualifier(firstQualifier),
            firstGenerator = { givenQualifier ->
                capturedQualifier = givenQualifier

                expectedFirst
            },
            secondQualifier = StringQualifier(secondQualifier),
            thirdQualifier = StringQualifier(thirdQualifier),
            type = Triple::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedRest, expectedRest),
        )
        assertEquals(
            actual = capturedQualifier?.value,
            expected = StringQualifier(firstQualifier).value,
        )
    }

    @Test
    @JsName("fn5b")
    fun `Given fixture is called it with qualifiers and a nestedGenerator for second returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedSecond = 52
        val expectedRest = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedRest }

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
            firstQualifier = StringQualifier(firstQualifier),
            secondGenerator = { givenQualifier ->
                capturedQualifier = givenQualifier

                expectedSecond
            },
            secondQualifier = StringQualifier(secondQualifier),
            thirdQualifier = StringQualifier(thirdQualifier),
            type = Triple::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedRest, expectedSecond, expectedRest),
        )
        assertEquals(
            actual = capturedQualifier?.value,
            expected = StringQualifier(secondQualifier).value,
        )
    }

    @Test
    @JsName("fn5c")
    fun `Given fixture is called it with qualifiers and a nestedGenerator for third returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedThird = 52
        val expectedRest = 23
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedRest }

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
            firstQualifier = StringQualifier(firstQualifier),
            secondQualifier = StringQualifier(secondQualifier),
            thirdQualifier = StringQualifier(thirdQualifier),
            thirdGenerator = { givenQualifier ->
                capturedQualifier = givenQualifier

                expectedThird
            },
            type = Triple::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedRest, expectedRest, expectedThird),
        )
        assertEquals(
            actual = capturedQualifier?.value,
            expected = StringQualifier(thirdQualifier).value,
        )
    }

    @Test
    @JsName("fn5d")
    fun `Given fixture is called it with qualifier and a nestedGenerator for first and second returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedFirst = 51
        val expectedSecond = 23
        val expectedThird = 52
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedFirstQualifier: PublicApi.Qualifier? = null
        var capturedSecondQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedThird }

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
            firstQualifier = StringQualifier(firstQualifier),
            firstGenerator = { givenQualifier ->
                capturedFirstQualifier = givenQualifier

                expectedFirst
            },
            secondGenerator = { givenQualifier ->
                capturedSecondQualifier = givenQualifier

                expectedSecond
            },
            secondQualifier = StringQualifier(secondQualifier),
            thirdQualifier = StringQualifier(thirdQualifier),
            type = Triple::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedSecond, expectedThird),
        )
        assertEquals(
            actual = capturedFirstQualifier?.value,
            expected = StringQualifier(firstQualifier).value,
        )
        assertEquals(
            actual = capturedSecondQualifier?.value,
            expected = StringQualifier(secondQualifier).value,
        )
    }

    @Test
    @JsName("fn5e")
    fun `Given fixture is called it with qualifier and a nestedGenerator for first and third returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedFirst = 51
        val expectedSecond = 23
        val expectedThird = 52
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedFirstQualifier: PublicApi.Qualifier? = null
        var capturedThirdQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedSecond }

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
            firstQualifier = StringQualifier(firstQualifier),
            firstGenerator = { givenQualifier ->
                capturedFirstQualifier = givenQualifier

                expectedFirst
            },
            secondQualifier = StringQualifier(secondQualifier),
            thirdQualifier = StringQualifier(thirdQualifier),
            thirdGenerator = { givenQualifier ->
                capturedThirdQualifier = givenQualifier

                expectedThird
            },
            type = Triple::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedSecond, expectedThird),
        )
        assertEquals(
            actual = capturedFirstQualifier?.value,
            expected = StringQualifier(firstQualifier).value,
        )
        assertEquals(
            actual = capturedThirdQualifier?.value,
            expected = StringQualifier(thirdQualifier).value,
        )
    }

    @Test
    @JsName("fn5f")
    fun `Given fixture is called it with qualifier and a nestedGenerator for second and third returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedFirst = 51
        val expectedSecond = 23
        val expectedThird = 52
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedSecondQualifier: PublicApi.Qualifier? = null
        var capturedThirdQualifier: PublicApi.Qualifier? = null
        generator.generate = { expectedFirst }

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
            firstQualifier = StringQualifier(firstQualifier),
            secondQualifier = StringQualifier(secondQualifier),
            secondGenerator = { givenQualifier ->
                capturedSecondQualifier = givenQualifier

                expectedSecond
            },
            thirdQualifier = StringQualifier(thirdQualifier),
            thirdGenerator = { givenQualifier ->
                capturedThirdQualifier = givenQualifier

                expectedThird
            },
            type = Triple::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedSecond, expectedThird),
        )
        assertEquals(
            actual = capturedSecondQualifier?.value,
            expected = StringQualifier(secondQualifier).value,
        )
        assertEquals(
            actual = capturedThirdQualifier?.value,
            expected = StringQualifier(thirdQualifier).value,
        )
    }

    @Test
    @JsName("fn5g")
    fun `Given fixture is called it with qualifier and a nestedGenerator for first and second and third returns a Fixture for the derrived Type`() {
        // Given
        val firstQualifier = "testFirst"
        val secondQualifier = "testSecond"
        val thirdQualifier = "testThird"
        val expectedFirst = 51
        val expectedSecond = 23
        val expectedThird = 52
        val generator = FilterableGeneratorStub<Int, Int>()

        var capturedFirstQualifier: PublicApi.Qualifier? = null
        var capturedSecondQualifier: PublicApi.Qualifier? = null
        var capturedThirdQualifier: PublicApi.Qualifier? = null

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
            firstQualifier = StringQualifier(firstQualifier),
            firstGenerator = { givenQualifier ->
                capturedFirstQualifier = givenQualifier

                expectedFirst
            },
            secondQualifier = StringQualifier(secondQualifier),
            secondGenerator = { givenQualifier ->
                capturedSecondQualifier = givenQualifier

                expectedSecond
            },
            thirdQualifier = StringQualifier(thirdQualifier),
            thirdGenerator = { givenQualifier ->
                capturedThirdQualifier = givenQualifier

                expectedThird
            },
            type = Triple::class,
        )

        // Then
        assertEquals(
            actual = result,
            expected = Triple(expectedFirst, expectedSecond, expectedThird),
        )
        assertEquals(
            actual = capturedFirstQualifier?.value,
            expected = StringQualifier(firstQualifier).value,
        )
        assertEquals(
            actual = capturedSecondQualifier?.value,
            expected = StringQualifier(secondQualifier).value,
        )
        assertEquals(
            actual = capturedThirdQualifier?.value,
            expected = StringQualifier(thirdQualifier).value,
        )
    }
}
