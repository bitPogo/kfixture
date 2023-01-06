/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("OPT_IN_USAGE")

package tech.antibytes.kfixture.fixture

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.any
import tech.antibytes.kfixture.defaultPredicate
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.RangedGeneratorStub
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.rangedFixture
import tech.antibytes.kfixture.resolveClassName

private class ComparableAny : Comparable<Any> {
    override fun compareTo(other: Any): Int {
        TODO("Not yet implemented")
    }
}

class RangedFixtureBaseSpec {
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
    fun `Given rangedFixture is called with a upper and lower bound it fails if the Type has no corresponding Generator`() {
        // Given
        val from = ComparableAny()
        val to = ComparableAny()
        val expected = Any()

        val generator = RangedGeneratorStub<ComparableAny, Any>()
        generator.generateWithRange = { _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Any::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.rangedFixture<ComparableAny, Any>(
                from = from,
                to = to,
                null,
                ::defaultPredicate,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($any).",
        )
    }

    @Test
    @JsName("fn2")
    fun `Given rangedFixture is called with a upper and lower bound and a predicate it fails if the Type has no corresponding Generator`() {
        // Given
        val from = ComparableAny()
        val to = ComparableAny()
        val expected = Any()

        val generator = RangedGeneratorStub<ComparableAny, Any>()
        generator.generateWithRange = { _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Any::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.rangedFixture<ComparableAny, Any>(
                from = from,
                to = to,
                null,
            ) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($any).",
        )
    }

    @Test
    @JsName("fn3")
    fun `Given rangedFixture is called with a upper and lower bound it fails the corresponding Generator is not a RangedGenerator`() {
        // Given
        val from = ComparableAny()
        val to = ComparableAny()
        val expected = Any()

        val generator = FilterableGeneratorStub<ComparableAny, Any>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Any::class)

        val fixture = Fixture(random, mapOf(any to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.rangedFixture<ComparableAny, Any>(
                from = from,
                to = to,
                null,
                ::defaultPredicate,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($any).",
        )
    }

    @Test
    @JsName("fn4")
    fun `Given rangedFixture is called with a upper and lower bound and predicate it fails the corresponding Generator is not a RangedGenerator`() {
        // Given
        val from = ComparableAny()
        val to = ComparableAny()
        val expected = Any()

        val generator = FilterableGeneratorStub<ComparableAny, Any>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveClassName(Any::class)

        val fixture = Fixture(random, mapOf(any to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            fixture.rangedFixture<ComparableAny, Any>(
                from = from,
                to = to,
                null,
            ) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($any).",
        )
    }

    @Test
    @JsName("fn5")
    fun `Given rangedFixture is called with a upper and lower bound it returns a Fixture for the derived Type`() {
        // Given
        val from = ComparableAny()
        val to = ComparableAny()
        val expected = Any()

        val generator = RangedGeneratorStub<ComparableAny, Any>()

        var capturedFrom: ComparableAny? = null
        var capturedTo: ComparableAny? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Any::class)

        val fixture = Fixture(random, mapOf(any to generator))

        // When
        val result = fixture.rangedFixture<ComparableAny, Any>(
            from = from,
            to = to,
            null,
            ::defaultPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = from,
        )
        assertEquals(
            actual = capturedTo,
            expected = to,
        )
    }

    @Test
    @JsName("fn6")
    fun `Given rangedFixture is called with a upper and lower bound and predicate it returns a Fixture for the derived Type`() {
        // Given
        val from = ComparableAny()
        val to = ComparableAny()
        val expected = Any()

        val expectedPredicate: Function1<ComparableAny?, Boolean> = { true }
        val generator = RangedGeneratorStub<ComparableAny, Any>()

        var capturedFrom: ComparableAny? = null
        var capturedTo: ComparableAny? = null
        var capturedPredicate: Function1<ComparableAny?, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Any::class)

        val fixture = Fixture(random, mapOf(any to generator))

        // When

        val result = fixture.rangedFixture<ComparableAny, Any>(
            from = from,
            to = to,
            null,
            expectedPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = from,
        )
        assertEquals(
            actual = capturedTo,
            expected = to,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @JsName("fn7")
    fun `Given rangedFixture is called with a upper and lower bound it returns a Fixture while respecting nullability`() {
        // Given
        val from = ComparableAny()
        val to = ComparableAny()
        val expected = Any()

        val generator = RangedGeneratorStub<ComparableAny, Any>()

        var capturedFrom: ComparableAny? = null
        var capturedTo: ComparableAny? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Any::class)

        val fixture = Fixture(random, mapOf(any to generator))

        // When
        val result = fixture.rangedFixture<ComparableAny, Any?>(
            from = from,
            to = to,
            null,
            ::defaultPredicate,
        )

        // Then
        assertNull(result)
        assertNull(capturedFrom)
        assertNull(capturedTo)
    }

    @Test
    @JsName("fn8")
    fun `Given rangedFixture is called with a upper and lower bound with a predicate it returns a Fixture while respecting nullability`() {
        // Given
        val from = ComparableAny()
        val to = ComparableAny()
        val expected = Any()

        val expectedPredicate: Function1<ComparableAny?, Boolean> = { true }
        val generator = RangedGeneratorStub<ComparableAny, Any>()

        var capturedFrom: ComparableAny? = null
        var capturedTo: ComparableAny? = null
        var capturedPredicate: Function1<ComparableAny, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveClassName(Any::class)

        val fixture = Fixture(random, mapOf(any to generator))

        // When
        val result = fixture.rangedFixture<ComparableAny, Any?>(
            from = from,
            to = to,
            null,
            expectedPredicate,
        )

        // Then
        assertNull(result)
        assertNull(capturedFrom)
        assertNull(capturedTo)
        assertNull(capturedPredicate)
    }

    @Test
    @JsName("fn9")
    fun `Given rangedFixture is called  with a upper and lower bound and a qualifier it returns a Fixture for the derived Type`() {
        // Given
        val from = ComparableAny()
        val to = ComparableAny()
        val expected = Any()

        val qualifier = "test"
        val generator = RangedGeneratorStub<ComparableAny, Any>()

        var capturedFrom: ComparableAny? = null
        var capturedTo: ComparableAny? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Any::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$any" to generator),
        )

        // When
        val result = fixture.rangedFixture<ComparableAny, Any>(
            from = from,
            to = to,
            StringQualifier(qualifier),
            ::defaultPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = from,
        )
        assertEquals(
            actual = capturedTo,
            expected = to,
        )
    }

    @Test
    @JsName("fn10")
    fun `Given rangedFixture is called  with a upper and lower bound and a qualifier and a predicate it returns a Fixture for the derived Type`() {
        // Given
        val from = ComparableAny()
        val to = ComparableAny()
        val expected = Any()

        val expectedPredicate: Function1<ComparableAny?, Boolean> = { true }
        val qualifier = "test"
        val generator = RangedGeneratorStub<ComparableAny, Any>()

        var capturedFrom: ComparableAny? = null
        var capturedTo: ComparableAny? = null
        var capturedPredicate: Function1<ComparableAny, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveClassName(Any::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$any" to generator),
        )

        // When
        val result = fixture.rangedFixture<ComparableAny, Any>(
            from = from,
            to = to,
            StringQualifier(qualifier),
            expectedPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = from,
        )
        assertEquals(
            actual = capturedTo,
            expected = to,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }
}
