/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.collections.IsoMutableList
import co.touchlab.stately.collections.sharedMutableListOf
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CharArrayGeneratorSpec {
    private val fixture = Random(0)
    private val random = RandomStub()
    private val range: IsoMutableList<Pair<Int, Int>> = sharedMutableListOf()

    @AfterTest
    fun tearDown() {
        random.clear()
        range.clear()
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils Generator`() {
        val generator: Any = CharArrayGenerator(random)

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a CharArray`() {
        // Given
        val size = 23
        val generator = CharArrayGenerator(random)
        val expected = List(size) { fixture.nextInt().toChar() }

        val values = expected.toSharedMutableList().also { it.add(0, size.toChar()) }
        random.nextIntRanged = { from, to ->
            range.add(Pair(from, to))

            values.removeAt(0).code
        }

        random.nextInt = { values.removeAt(0).code }

        // When
        val result: CharArray = generator.generate()

        // Then
        assertEquals(
            actual = Pair(1, 10),
            expected = range.removeAt(0)
        )

        range.forEach { actual ->
            assertEquals(
                actual = Pair(32, 126),
                expected = actual
            )
        }

        assertTrue(
            expected.toTypedArray().toCharArray().contentEquals(result)
        )
    }
}
