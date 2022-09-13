/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.RangedGeneratorStub
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

class InstantGeneratorSpec {
    private val dependencyGenerator = RangedGeneratorStub<Long, Long>()

    @AfterTest
    fun tearDown() {
        dependencyGenerator.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils DependentGeneratorFactory`() {
        val generator: Any = InstantGenerator

        assertTrue(generator is PublicApi.DependentGeneratorFactory<*>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given getInstance is called it returns an FilterableGenerator`() {
        val longId = resolveGeneratorId(Long::class)
        val generator: Any = InstantGenerator.getInstance(RandomStub(), mapOf(longId to dependencyGenerator))

        assertTrue(generator is PublicApi.RangedGenerator<*, *>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate is called it returns a Instant`() {
        // Given
        val expected = 23L

        var capturedFrom: Long? = null
        var capturedTo: Long? = null

        dependencyGenerator.generateWithRange = { from, to, _ ->
            capturedFrom = from
            capturedTo = to

            expected
        }

        val generator = InstantGenerator(
            epochMilliSecondsGenerator = dependencyGenerator,
        )

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result.toEpochMilliseconds(),
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = 0L,
        )
        assertEquals(
            actual = capturedTo,
            expected = Long.MAX_VALUE,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn3")
    fun `Given generate is called with a predicate it returns a Instant`() {
        // Given
        val expected = 23L
        val expectedPredicate: Function1<Long?, Boolean> = { true }

        var capturedFrom: Long? = null
        var capturedTo: Long? = null
        var capturedPredicate: Function1<Long?, Boolean>? = null

        dependencyGenerator.generateWithRange = { from, to, predicate ->
            capturedFrom = from
            capturedTo = to
            capturedPredicate = predicate

            expected
        }

        val generator = InstantGenerator(
            epochMilliSecondsGenerator = dependencyGenerator,
        )

        // When
        val result = generator.generate(predicate = expectedPredicate)

        // Then
        assertEquals(
            actual = result.toEpochMilliseconds(),
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = 0L,
        )
        assertEquals(
            actual = capturedTo,
            expected = Long.MAX_VALUE,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn5")
    fun `Given generate is called with a range and a predicate it fails if the the lower bound is less than zero`() {
        // Given
        val expectedFrom = -1L
        val expectedTo = 23L
        val expectedPredicate: Function1<Long?, Boolean> = { true }

        val generator = InstantGenerator(
            epochMilliSecondsGenerator = dependencyGenerator,
        )

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(
                from = expectedFrom,
                to = expectedTo,
                predicate = expectedPredicate,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "The lower bound of an Instant must be greater than -1!",
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn6")
    fun `Given generate is called with a range and a predicate it fails if the the upper bound is less than lower bound`() {
        // Given
        val expectedFrom = 1L
        val expectedTo = 0L
        val expectedPredicate: Function1<Long?, Boolean> = { true }

        val generator = InstantGenerator(
            epochMilliSecondsGenerator = dependencyGenerator,
        )

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(
                from = expectedFrom,
                to = expectedTo,
                predicate = expectedPredicate,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "The upper bound of an Instant must be greater than lower bound!",
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn7")
    fun `Given generate is called with a range and a predicate it fails if the the upper bound is equal to the lower bound`() {
        // Given
        val expectedFrom = 1L
        val expectedTo = 1L
        val expectedPredicate: Function1<Long?, Boolean> = { true }

        val generator = InstantGenerator(
            epochMilliSecondsGenerator = dependencyGenerator,
        )

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            generator.generate(
                from = expectedFrom,
                to = expectedTo,
                predicate = expectedPredicate,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "The upper bound of an Instant must be greater than lower bound!",
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn8")
    fun `Given generate is called with a range and a predicate it returns a Instant`() {
        // Given
        val expected = 23L
        val expectedPredicate: Function1<Long?, Boolean> = { true }

        var capturedFrom: Long? = null
        var capturedTo: Long? = null
        var capturedPredicate: Function1<Long?, Boolean>? = null

        dependencyGenerator.generateWithRange = { from, to, predicate ->
            capturedFrom = from
            capturedTo = to
            capturedPredicate = predicate

            expected
        }

        val generator = InstantGenerator(
            epochMilliSecondsGenerator = dependencyGenerator,
        )

        // When
        val result = generator.generate(predicate = expectedPredicate)

        // Then
        assertEquals(
            actual = result.toEpochMilliseconds(),
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = 0L,
        )
        assertEquals(
            actual = capturedTo,
            expected = Long.MAX_VALUE,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }
}
