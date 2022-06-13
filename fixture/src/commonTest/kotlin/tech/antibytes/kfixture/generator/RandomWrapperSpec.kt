/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator

import co.touchlab.stately.isolate.IsolateState
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import tech.antibytes.kfixture.mock.RandomStub
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

@Suppress("UNCHECKED_CAST")
class RandomWrapperSpec {
    private val random = IsolateState { RandomStub() }
    private val capturedMinimum = atomic(-1)
    private val capturedMaximum = atomic(-1)
    private val capturedCount = atomic(-1)
    private val capturedArray = atomic(ByteArray(23))

    @AfterTest
    fun tearDown() {
        random.access { it.clear() }
        capturedMinimum.getAndSet(-1)
        capturedMaximum.getAndSet(-1)
        capturedCount.getAndSet(-1)
        capturedArray.getAndSet(ByteArray(23))
    }

    @Test
    @JsName("fn0")
    fun `It fulfils Random`() {
        val wrapper: Any = RandomWrapper(random as IsolateState<Random>)

        assertTrue(wrapper is Random)
    }

    @Test
    @JsName("fn1")
    fun `Given nextBits is called it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val count = 42
        val expected = 23

        (random as IsolateState<RandomStub>).access {
            it.nextBits = { given ->
                capturedCount.update { given }

                expected
            }
        }

        // When
        val actual = wrapper.nextBits(count)

        // Then
        assertEquals(
            actual = actual,
            expected = expected
        )
        assertEquals(
            actual = capturedCount.value,
            expected = count
        )
    }

    @Test
    @JsName("fn2")
    fun `Given nextBoolean is called it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val expected = true

        (random as IsolateState<RandomStub>).access {
            it.nextBoolean = { expected }
        }

        // When
        val actual = wrapper.nextBoolean()

        // Then
        assertEquals(
            actual = actual,
            expected = expected
        )
    }

    @Test
    @JsName("fn3")
    fun `Given nextInt is called it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val expected = 42

        (random as IsolateState<RandomStub>).access {
            it.nextInt = { expected }
        }

        // When
        val actual = wrapper.nextInt()

        // Then
        assertEquals(
            actual = actual,
            expected = expected
        )
    }

    @Test
    @JsName("fn4")
    fun `Given nextInt is called with an Int it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val count = 23
        val expected = 42

        (random as IsolateState<RandomStub>).access {
            it.nextIntLimited = { count ->
                capturedCount.update { count }

                expected
            }
        }

        // When
        val actual = wrapper.nextInt(count)

        // Then
        assertEquals(
            actual = actual,
            expected = expected
        )
        assertEquals(
            actual = capturedCount.value,
            expected = count
        )
    }

    @Test
    @JsName("fn5")
    fun `Given nextInt is called with two Ints it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val min = -23
        val max = -23
        val expected = 42

        (random as IsolateState<RandomStub>).access {
            it.nextIntRanged = { min, max ->
                capturedMinimum.update { min }
                capturedMaximum.update { max }

                expected
            }
        }

        // When
        val actual = wrapper.nextInt(min, max)

        // Then
        assertEquals(
            actual = actual,
            expected = expected
        )
        assertEquals(
            actual = capturedMinimum.value,
            expected = min
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = max
        )
    }

    @Test
    @JsName("fn6")
    fun `Given nextLong is called it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val expected = 42

        (random as IsolateState<RandomStub>).access {
            it.nextLong = { expected.toLong() }
        }

        // When
        val actual = wrapper.nextLong()

        // Then
        assertEquals(
            actual = actual,
            expected = expected.toLong()
        )
    }

    @Test
    @JsName("fn7")
    fun `Given nextLong is called with an Long it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val count = 23
        val expected = 42

        (random as IsolateState<RandomStub>).access {
            it.nextLongLimited = { count ->
                capturedCount.update { count.toInt() }

                expected.toLong()
            }
        }

        // When
        val actual = wrapper.nextLong(count.toLong())

        // Then
        assertEquals(
            actual = actual,
            expected = expected.toLong()
        )
        assertEquals(
            actual = capturedCount.value,
            expected = count
        )
    }

    @Test
    @JsName("fn8")
    fun `Given nextLong is called with two Longs it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val min = -23
        val max = -23
        val expected = 42

        (random as IsolateState<RandomStub>).access {
            it.nextLongRanged = { min, max ->
                capturedMinimum.update { min.toInt() }
                capturedMaximum.update { max.toInt() }

                expected.toLong()
            }
        }

        // When
        val actual = wrapper.nextLong(min.toLong(), max.toLong())

        // Then
        assertEquals(
            actual = actual,
            expected = expected.toLong()
        )
        assertEquals(
            actual = capturedMinimum.value,
            expected = min
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = max
        )
    }

    @Test
    @JsName("fn9")
    fun `Given nextDoubles is called it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val expected = 42

        (random as IsolateState<RandomStub>).access {
            it.nextDouble = { expected.toDouble() }
        }

        // When
        val actual = wrapper.nextDouble()

        // Then
        assertEquals(
            actual = actual,
            expected = expected.toDouble()
        )
    }

    @Test
    @JsName("fn10")
    fun `Given nextDoubles is called with an Doubles it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val count = 23
        val expected = 42

        (random as IsolateState<RandomStub>).access {
            it.nextDoubleLimited = { count ->
                capturedCount.update { count.toInt() }

                expected.toDouble()
            }
        }

        // When
        val actual = wrapper.nextDouble(count.toDouble())

        // Then
        assertEquals(
            actual = actual,
            expected = expected.toDouble()
        )
        assertEquals(
            actual = capturedCount.value,
            expected = count
        )
    }

    @Test
    @JsName("fn11")
    fun `Given nextDouble is called with two Doubles it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val min = -23
        val max = -23
        val expected = 42

        (random as IsolateState<RandomStub>).access {
            it.nextDoubleRanged = { min, max ->
                capturedMinimum.update { min.toInt() }
                capturedMaximum.update { max.toInt() }

                expected.toDouble()
            }
        }

        // When
        val actual = wrapper.nextDouble(min.toDouble(), max.toDouble())

        // Then
        assertEquals(
            actual = actual,
            expected = expected.toDouble()
        )
        assertEquals(
            actual = capturedMinimum.value,
            expected = min
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = max
        )
    }

    @Test
    @JsName("fn12")
    fun `Given nextFloat is called it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val expected = 42

        (random as IsolateState<RandomStub>).access {
            it.nextFloat = { expected.toFloat() }
        }

        // When
        val actual = wrapper.nextFloat()

        // Then
        assertEquals(
            actual = actual,
            expected = expected.toFloat()
        )
    }

    @Test
    @JsName("fn13")
    fun `Given nextBytes is called with an ByteArray it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val expected = ByteArray(2)
        val given = ByteArray(1)

        (random as IsolateState<RandomStub>).access {
            it.nextBytes = { given ->
                capturedArray.update { given }
                expected
            }
        }

        // When
        val actual = wrapper.nextBytes(given)

        // Then
        assertSame(
            actual = actual,
            expected = expected
        )
        assertSame(
            actual = capturedArray.value,
            expected = given
        )
    }

    @Test
    @JsName("fn14")
    fun `Given nextBytes is called with an Int it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val expected = ByteArray(2)
        val given = 23

        (random as IsolateState<RandomStub>).access {
            it.nextBytesArray = { given ->
                capturedCount.update { given }
                expected
            }
        }

        // When
        val actual = wrapper.nextBytes(given)

        // Then
        assertSame(
            actual = actual,
            expected = expected
        )
        assertEquals(
            actual = capturedCount.value,
            expected = given
        )
    }

    @Test
    @JsName("fn16")
    fun `Given nextBytes is called with an ByteArray and two Ints it delegates the call to the wrapped random generator`() {
        // Given
        val wrapper = RandomWrapper(random as IsolateState<Random>)
        val expected = ByteArray(2)
        val max = 23
        val min = -23
        val array = ByteArray(1)

        (random as IsolateState<RandomStub>).access {
            it.nextBytesRanged = { givenArray, min, max ->
                capturedArray.update { givenArray }
                capturedMinimum.update { min }
                capturedMaximum.update { max }
                expected
            }
        }

        // When
        val actual = wrapper.nextBytes(array, min, max)

        // Then
        assertSame(
            actual = actual,
            expected = expected
        )
        assertEquals(
            actual = capturedArray.value,
            expected = array
        )
        assertEquals(
            actual = capturedMinimum.value,
            expected = min
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = max
        )
    }

    /*

    override fun nextBytes(array: ByteArray, fromIndex: Int, toIndex: Int): ByteArray {
        return random.access { it.nextBytes(array, fromIndex, toIndex) }
    }
     */
}
