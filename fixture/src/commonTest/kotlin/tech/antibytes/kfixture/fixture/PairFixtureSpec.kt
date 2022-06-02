/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.fixture

import co.touchlab.stately.isolate.IsolateState
import kotlinx.atomicfu.atomic
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.int
import tech.antibytes.kfixture.mock.GeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.pairFixture
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@Suppress("USELESS_CAST")
class PairFixtureSpec {
    private val random = IsolateState { RandomStub() }
    private val capturedMinimum = atomic(-1)
    private val capturedMaximum = atomic(-1)

    @AfterTest
    fun tearDown() {
        random.access { it.clear() }
        capturedMinimum.getAndSet(-1)
        capturedMaximum.getAndSet(-1)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils Fixture`() {
        val fixture: Any = Fixture(random as IsolateState<Random>, emptyMap())

        assertTrue(fixture is PublicApi.Fixture)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given pairFixture is called it fails if the Type has no corresponding Generator`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.pairFixture<Int, Int>()
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($int)."
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given pairFixture is called it returns a Fixture for the derrived Type`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, mapOf(int to generator))

        // When
        val result = fixture.pairFixture<Int, Int>()

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expected, expected)
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given pairFixture is called it returns a Fixture while respecting nullability`() {
        // Given
        val expected = 23
        val generator = GeneratorStub<Int>()

        generator.generate = { expected }
        random.access { it.nextBoolean = { true } }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(random as IsolateState<Random>, mapOf(int to generator))

        // When
        val result = fixture.pairFixture<Int, Int?>()

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expected, null)
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn4")
    fun `Given pairFixture is called with qualifiers it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random as IsolateState<Random>,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            )
        )

        // When
        val result = fixture.pairFixture<Int, Int>(
            StringQualifier(keyQualifier),
            StringQualifier(valueQualifier)
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expected, expected)
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given fixture is called with qualifiers and a Type it returns a Fixture for the derived Type`() {
        // Given
        val expected = 23
        val keyQualifier = "testKey"
        val valueQualifier = "testValue"
        val generator = GeneratorStub<Int>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Int::class)

        val fixture = Fixture(
            random as IsolateState<Random>,
            mapOf(
                "q:$keyQualifier:$int" to generator,
                "q:$valueQualifier:$int" to generator,
            )
        )

        // When
        val result: Pair<Int, Int> = fixture.fixture(
            keyQualifier = StringQualifier(keyQualifier),
            valueQualifier = StringQualifier(valueQualifier),
            type = Pair::class
        )

        // Then
        assertEquals(
            actual = result,
            expected = Pair(expected, expected)
        )
    }
}
