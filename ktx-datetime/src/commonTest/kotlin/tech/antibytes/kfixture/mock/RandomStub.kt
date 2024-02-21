/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import kotlin.random.Random

class RandomStub(
    @JsName("nextBitStub")
    var nextBits: ((Int) -> Int)? = null,
    @JsName("nextBooleanStub")
    var nextBoolean: (() -> Boolean)? = null,
    @JsName("nextIntStub")
    var nextInt: (() -> Int)? = null,
    @JsName("nextIntLimitedStub")
    var nextIntLimited: ((Int) -> Int)? = null,
    @JsName("nextIntRangedStub")
    var nextIntRanged: ((Int, Int) -> Int)? = null,
    @JsName("nextLongStub")
    var nextLong: (() -> Long)? = null,
    @JsName("nextLongLimitedStub")
    var nextLongLimited: ((Long) -> Long)? = null,
    @JsName("nextLongRangedStub")
    var nextLongRanged: ((Long, Long) -> Long)? = null,
    @JsName("nextDoubleStub")
    var nextDouble: (() -> Double)? = null,
    @JsName("nextDoubleLimitedStub")
    var nextDoubleLimited: ((Double) -> Double)? = null,
    @JsName("nextDoubleRangedStub")
    var nextDoubleRanged: ((Double, Double) -> Double)? = null,
    @JsName("nextFloatStub")
    var nextFloat: (() -> Float)? = null,
    @JsName("nextBytesStub")
    var nextBytes: ((ByteArray) -> ByteArray)? = null,
    @JsName("nextBytesArrayStub")
    var nextBytesArray: ((Int) -> ByteArray)? = null,
    @JsName("nextBytesRangedStub")
    var nextBytesRanged: ((ByteArray, Int, Int) -> ByteArray)? = null,
) : Random() {
    override fun nextBits(bitCount: Int): Int {
        return nextBits?.invoke(bitCount) ?: throw RuntimeException("No SideEffect for nextBits!")
    }

    override fun nextBoolean(): Boolean {
        return nextBoolean?.invoke() ?: throw RuntimeException("No SideEffect for nextBoolean!")
    }

    override fun nextInt(): Int {
        return nextInt?.invoke() ?: throw RuntimeException("No SideEffect for nextInt!")
    }

    override fun nextInt(until: Int): Int {
        return nextIntLimited?.invoke(until) ?: throw RuntimeException("No SideEffect for nextIntLimited!")
    }

    override fun nextInt(from: Int, until: Int): Int {
        return nextIntRanged?.invoke(from, until) ?: throw RuntimeException("No SideEffect for nextIntRanged!")
    }

    override fun nextLong(): Long {
        return nextLong?.invoke() ?: throw RuntimeException("No SideEffect for nextLong!")
    }

    override fun nextLong(until: Long): Long {
        return nextLongLimited?.invoke(until) ?: throw RuntimeException("No SideEffect for nextLongLimited!")
    }

    override fun nextLong(from: Long, until: Long): Long {
        return nextLongRanged?.invoke(from, until) ?: throw RuntimeException("No SideEffect for nextLongRanged!")
    }

    override fun nextDouble(): Double {
        return nextDouble?.invoke() ?: throw RuntimeException("No SideEffect for nextDouble!")
    }

    override fun nextDouble(until: Double): Double {
        return nextDoubleLimited?.invoke(until) ?: throw RuntimeException("No SideEffect for nextDoubleLimited!")
    }

    override fun nextDouble(from: Double, until: Double): Double {
        return nextDoubleRanged?.invoke(from, until) ?: throw RuntimeException("No SideEffect for nextDoubleRanged!")
    }

    override fun nextFloat(): Float {
        return nextFloat?.invoke() ?: throw RuntimeException("No SideEffect for nextFloat!")
    }

    override fun nextBytes(size: Int): ByteArray {
        return nextBytesArray?.invoke(size) ?: throw RuntimeException("No SideEffect for nextByteArray!")
    }

    override fun nextBytes(array: ByteArray): ByteArray {
        return nextBytes?.invoke(array) ?: throw RuntimeException("No SideEffect for nextBytes!")
    }

    override fun nextBytes(array: ByteArray, fromIndex: Int, toIndex: Int): ByteArray {
        return nextBytesRanged?.invoke(array, fromIndex, toIndex)
            ?: throw RuntimeException("No SideEffect for nextBytes!")
    }

    fun clear() {
        nextBits = null
        nextBoolean = null
        nextInt = null
        nextIntLimited = null
        nextIntRanged = null
        nextLong = null
        nextLongLimited = null
        nextLongRanged = null
        nextDouble = null
        nextDoubleLimited = null
        nextDoubleRanged = null
        nextFloat = null
        nextBytes = null
        nextBytesArray = null
        nextBytesRanged = null
    }
}
