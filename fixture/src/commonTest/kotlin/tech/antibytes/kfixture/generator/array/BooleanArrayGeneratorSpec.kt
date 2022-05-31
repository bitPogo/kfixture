/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import co.touchlab.stately.isolate.IsolateState
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BooleanArrayGeneratorSpec {
    private val fixture = Random(0)
    private val random = IsolateState { RandomStub() }
    private val range: AtomicRef<Pair<Int, Int>?> = atomic(null)

    @AfterTest
    fun tearDown() {
        random.access { it.clear() }
        range.getAndSet(null)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils Generator`() {
        val generator: Any = BooleanArrayGenerator(random as IsolateState<Random>)

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a BooleanArray`() {
        // Given
        val size = 23
        val generator = BooleanArrayGenerator(random as IsolateState<Random>)
        val expected = List(size) { fixture.nextBoolean() }

        random.access { stub ->
            (stub as RandomStub).nextIntRanged = { from, to ->
                range.update { Pair(from, to) }
                size
            }
        }
        val values = expected.toSharedMutableList()
        random.access { stub ->
            (stub as RandomStub).nextBoolean = { values.removeAt(0) }
        }

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = Pair(1, 100),
            expected = range.value
        )
        assertTrue(
            expected.toTypedArray().toBooleanArray().contentEquals(result)
        )
    }
}
