/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator

import co.touchlab.stately.isolate.IsolateState
import kotlin.random.Random

internal class RandomWrapper(
    private val random: IsolateState<Random>
) : Random() {
    override fun nextBits(bitCount: Int): Int = random.access { it.nextBits(bitCount) }

    override fun nextBoolean(): Boolean = random.access { it.nextBoolean() }

    override fun nextInt(): Int = random.access { it.nextInt() }

    override fun nextInt(until: Int): Int = random.access { it.nextInt(until) }

    override fun nextInt(from: Int, until: Int): Int = random.access { it.nextInt(from, until) }

    override fun nextLong(): Long = random.access { it.nextLong() }

    override fun nextLong(until: Long): Long = random.access { it.nextLong(until) }

    override fun nextLong(from: Long, until: Long): Long = random.access { it.nextLong(from, until) }

    override fun nextDouble(): Double = random.access { it.nextDouble() }

    override fun nextDouble(until: Double): Double = random.access { it.nextDouble(until) }

    override fun nextDouble(from: Double, until: Double): Double = random.access { it.nextDouble(from, until) }

    override fun nextFloat(): Float = random.access { it.nextFloat() }

    override fun nextBytes(size: Int): ByteArray = random.access { it.nextBytes(size) }

    override fun nextBytes(array: ByteArray, fromIndex: Int, toIndex: Int): ByteArray {
        return random.access { it.nextBytes(array, fromIndex, toIndex) }
    }

    override fun nextBytes(array: ByteArray): ByteArray = random.access { it.nextBytes(array) }
}
