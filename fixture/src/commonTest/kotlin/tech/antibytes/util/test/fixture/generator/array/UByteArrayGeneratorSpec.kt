/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.test.fixture.generator.array

import co.touchlab.stately.isolate.IsolateState
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import tech.antibytes.util.test.fixture.PublicApi
import tech.antibytes.util.test.fixture.mock.RandomStub
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UByteArrayGeneratorSpec {
    private val random = IsolateState { RandomStub() }
    private val range: AtomicRef<Pair<Int, Int>?> = atomic(null)

    @AfterTest
    fun tearDown() {
        random.access { it.clear() }
        range.update { null }
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils Generator`() {
        val generator: Any = UByteArrayGenerator(random as IsolateState<Random>)

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a UByteArray`() {
        // Given
        val size = 23
        val expected = UByteArray(size)
        val generator = UByteArrayGenerator(random as IsolateState<Random>)

        random.access { stub ->
            (stub as RandomStub).nextIntRanged = { from, to ->
                range.update { Pair(from, to) }
                size
            }
        }

        random.access { stub ->
            (stub as RandomStub).nextByteArray = { arraySize -> ByteArray(arraySize) }
        }

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = Pair(1, 100),
            expected = range.value
        )
        assertTrue(
            expected.contentEquals(result)
        )
    }
}
