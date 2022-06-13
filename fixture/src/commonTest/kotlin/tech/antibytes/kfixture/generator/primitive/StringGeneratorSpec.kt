/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import co.touchlab.stately.collections.sharedMutableListOf
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("USELESS_CAST")
class StringGeneratorSpec {
    private val random = RandomStub()

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils Generator`() {
        val generator: Any = StringGenerator(random)

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a String`() {
        // Given
        val capturedRanges = sharedMutableListOf<Pair<Int, Int>>()
        val randomValues = sharedMutableListOf(
            3,
            'a'.code,
            'b'.code,
            'c'.code
        )

        random.nextIntRanged = { from, until ->
            capturedRanges.add(Pair(from, until))
            randomValues.removeAt(0)
        }

        val generator = StringGenerator(random)

        // When
        val result: Any = generator.generate()

        // Then
        assertTrue(result is String)
        assertEquals(
            expected = "abc",
            actual = result
        )

        assertEquals(
            actual = capturedRanges[0],
            expected = Pair(1, 10)
        )
        assertEquals(
            actual = capturedRanges[1],
            expected = Pair(33, 126)
        )
        assertEquals(
            actual = capturedRanges[2],
            expected = Pair(33, 126)
        )
        assertEquals(
            actual = capturedRanges[3],
            expected = Pair(33, 126)
        )
    }
}
