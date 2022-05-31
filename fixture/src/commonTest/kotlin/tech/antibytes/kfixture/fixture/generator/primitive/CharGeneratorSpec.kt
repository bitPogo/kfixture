/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.fixture.generator.primitive

import co.touchlab.stately.isolate.IsolateState
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture.mock.RandomStub
import tech.antibytes.kfixture.generator.primitive.CharGenerator
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CharGeneratorSpec {
    private val random = IsolateState { RandomStub() }
    private val range: AtomicRef<Pair<Int, Int>?> = atomic(null)

    @AfterTest
    fun tearDown() {
        random.access { it.clear() }
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils Generator`() {
        val generator: Any = CharGenerator(random as IsolateState<Random>)

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a Char`() {
        // Given
        val expected = 100

        random.access { stub ->
            stub.nextIntRanged = { from, to ->
                range.update { Pair(from, to) }
                expected
            }
        }

        val generator = CharGenerator(random as IsolateState<Random>)

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = range.value,
            expected = Pair(33, 126)
        )
        assertEquals(
            actual = result,
            expected = expected.toChar()
        )
    }
}
