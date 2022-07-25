/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi

class UnitGeneratorSpec {
    @Test
    @JsName("fn0")
    fun `It fulfils UnitGenerator`() {
        val generator: Any = UnitGenerator

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Test
    @JsName("fn1")
    fun `Given generate is called it returns Unit`() {
        // When
        val actual = UnitGenerator.generate()

        // Then
        assertSame(
            actual = actual,
            expected = Unit,
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn2")
    fun `Given generate is called with a filter it fails`() {
        // Then
        val error = assertFailsWith<IllegalStateException> {
            // When
            UnitGenerator.generate { true }
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "Unit cannot be filtered!",
        )
    }
}
