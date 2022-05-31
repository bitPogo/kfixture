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

class DoubleArrayGeneratorSpec {
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
        val generator: Any = DoubleArrayGenerator(random as IsolateState<Random>)

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a DoubleArray`() {
        // Given
        val size = 3
        val expected = DoubleArray(size)
        val expectedFloatPoints: List<Double> = mutableListOf(
            0.3,
            0.42,
            0.23,
        )
        val generator = DoubleArrayGenerator(random as IsolateState<Random>)

        random.access { stub ->
            (stub as RandomStub).nextIntRanged = { from, to ->
                range.update { Pair(from, to) }
                size
            }
        }
        val floatPoints = expectedFloatPoints.toMutableList()
        random.access { stub ->
            (stub as RandomStub).nextByteArray = { arraySize -> ByteArray(arraySize) }
        }

        random.access { stub ->
            (stub as RandomStub).nextDouble = { floatPoints.removeAt(0) }
        }

        // When
        val result: DoubleArray = generator.generate()

        // Then
        assertEquals(
            actual = Pair(1, 100),
            expected = range.value
        )
        assertTrue(
            expected.mapIndexed { idx, byte ->
                byte.toInt() + expectedFloatPoints[idx]
            }.toDoubleArray().contentEquals(result)
        )
    }
}
