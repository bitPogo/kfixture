/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.generator

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.datetime.UtcOffset
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

class FixedOffsetTimeZoneSpec {
    private val dependencyGenerator = FilterableGeneratorStub<UtcOffset, UtcOffset>()
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        dependencyGenerator.clear()
        random.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils DependentGenerator`() {
        val generator: Any = FixedOffsetTimeZoneGenerator

        assertTrue(generator is PublicApi.DependentGeneratorFactory<*>)
    }

    @Test
    @JsName("fn1")
    fun `It fulfils FilterableGenerator`() {
        val utcId = resolveGeneratorId(UtcOffset::class)
        val generator: Any = FixedOffsetTimeZoneGenerator.getInstance(
            random,
            mapOf(
                utcId to dependencyGenerator,
            ),
        )

        assertTrue(generator is PublicApi.FilterableGenerator<*, *>)
    }

    @Test
    @JsName("fn2")
    fun `Given generate is called it returns a FixedOffsetTimeZone`() {
        // Given
        val expected = UtcOffset(
            hours = 17,
            minutes = 59,
            seconds = 21,
        )

        dependencyGenerator.generate = { expected }

        // When
        val actual = FixedOffsetTimeZoneGenerator(dependencyGenerator).generate()

        // Then
        assertEquals(
            actual = actual.offset,
            expected = expected,
        )
    }

    @Test
    @JsName("fn3")
    fun `Given generate is called with a predicate it returns a FixedOffsetTimeZone`() {
        // Given
        val expected = mutableListOf(
            UtcOffset(
                hours = 17,
                minutes = 59,
                seconds = 21,
            ),
            UtcOffset(
                hours = 18,
                minutes = 0,
                seconds = 0,
            ),
        )

        dependencyGenerator.generate = { expected.removeFirst() }

        // When
        val actual = FixedOffsetTimeZoneGenerator(dependencyGenerator).generate { offset ->
            offset?.offset?.totalSeconds == 64800
        }

        // Then
        assertEquals(
            actual = actual.offset,
            expected = UtcOffset(
                hours = 18,
                minutes = 0,
                seconds = 0,
            ),
        )
    }
}
