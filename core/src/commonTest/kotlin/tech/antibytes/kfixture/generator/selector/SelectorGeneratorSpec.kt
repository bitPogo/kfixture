/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.selector

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub

class SelectorGeneratorSpec {
    @Test
    @JsName("fn0")
    fun `It fulfils Generator`() {
        val generator: Any = SelectorGenerator(RandomStub(), arrayOf(Any()))

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Test
    @JsName("fn2")
    fun `Given generate is called it selects one of the provided Items`() {
        // Given
        val items = arrayOf(Any(), Any(), Any())
        val random = RandomStub()

        var capturedMin: Int? = null
        var capturedMax: Int? = null

        random.nextIntRanged = { givenMin, givenMax ->
            capturedMin = givenMin
            capturedMax = givenMax

            1
        }

        // When
        val actual = SelectorGenerator(random, items).generate()

        // Then
        assertEquals(
            actual = actual,
            expected = items[1],
        )

        assertEquals(
            actual = capturedMin,
            expected = 0,
        )
        assertEquals(
            actual = capturedMax,
            expected = items.size,
        )
    }
}
