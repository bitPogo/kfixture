/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.array

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.RandomStub
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FloatArrayGeneratorSpec {
    private val random = RandomStub()
    private val range: AtomicRef<Pair<Int, Int>?> = atomic(null)

    @AfterTest
    fun tearDown() {
        random.clear()
        range.getAndSet(null)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils Generator`() {
        val generator: Any = FloatArrayGenerator(random)

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a FloatArray`() {
        // Given
        val size = 3
        val expected = FloatArray(size)
        val expectedFloatPoints: List<Float> = mutableListOf(
            0.3.toFloat(),
            0.42.toFloat(),
            0.23.toFloat(),
        )
        val generator = FloatArrayGenerator(random)

        random.nextIntRanged = { from, to ->
            range.update { Pair(from, to) }
            size
        }

        val floatPoints = expectedFloatPoints.toSharedMutableList()
        random.nextBytesArray = { arraySize -> ByteArray(arraySize) }

        random.nextFloat = { floatPoints.removeAt(0) }

        // When
        val result: FloatArray = generator.generate()

        // Then
        assertEquals(
            actual = Pair(1, 10),
            expected = range.value
        )
        assertTrue(
            expected.mapIndexed { idx, byte ->
                byte.toInt() + expectedFloatPoints[idx]
            }.toFloatArray().contentEquals(result)
        )
    }
}
