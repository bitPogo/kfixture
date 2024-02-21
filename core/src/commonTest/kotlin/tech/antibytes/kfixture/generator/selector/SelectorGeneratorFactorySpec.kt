/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.selector

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub

class SelectorGeneratorFactorySpec {
    @Test
    @JsName("fn0")
    fun `It fulfils GeneratorFactory`() {
        val generator: Any = SelectorGeneratorFactory(arrayOf(Any()))

        assertTrue(generator is PublicApi.GeneratorFactory<*>)
    }

    @Test
    @JsName("fn1")
    fun `Given the Generator is initialized without proper values it fails`() {
        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            SelectorGeneratorFactory(arrayOf())
        }

        assertEquals(
            actual = error.message,
            expected = "Missing selectable items!",
        )
    }

    @Test
    @JsName("fn3")
    fun `Given getInstance is called it returns a Generator`() {
        // Given
        val random = RandomStub()

        // When
        val generator: Any = SelectorGeneratorFactory(arrayOf(Any())).getInstance(random)

        // Then
        assertTrue(generator is PublicApi.Generator<*>)
    }
}
