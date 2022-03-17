/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.test.fixture.mock

import kotlin.js.JsName
import kotlin.random.Random

class RandomStub(
    @JsName("nextIntStub")
    var nextInt: (() -> Int)? = null,
    @JsName("nextIntRangedStub")
    var nextIntRanged: ((Int, Int) -> Int)? = null,
    @JsName("nextDoubleStub")
    var nextDouble: (() -> Double)? = null,
    @JsName("nextBooleanStub")
    var nextBoolean: (() -> Boolean)? = null,
    @JsName("nextFloatStub")
    var nextFloat: (() -> Float)? = null,
    @JsName("nextLongStub")
    var nextLong: (() -> Long)? = null,
    @JsName("nextByteArrayStub")
    var nextByteArray: ((Int) -> ByteArray)? = null,
) : Random() {
    override fun nextInt(): Int {
        return nextInt?.invoke() ?: throw RuntimeException("No sideeffect for nextInt!")
    }

    override fun nextInt(from: Int, until: Int): Int {
        return nextIntRanged?.invoke(from, until) ?: throw RuntimeException("No sideeffect for nextIntRanged!")
    }

    override fun nextDouble(): Double {
        return nextDouble?.invoke() ?: throw RuntimeException("No sideeffect for nextDouble!")
    }

    override fun nextBits(bitCount: Int): Int = TODO("Not yet implemented")

    override fun nextBoolean(): Boolean {
        return nextBoolean?.invoke() ?: throw RuntimeException("No sideeffect for nextBoolean!")
    }

    override fun nextFloat(): Float {
        return nextFloat?.invoke() ?: throw RuntimeException("No sideeffect for nextFloat!")
    }

    override fun nextLong(): Long {
        return nextLong?.invoke() ?: throw RuntimeException("No sideeffect for nextLong!")
    }

    override fun nextBytes(size: Int): ByteArray {
        return nextByteArray?.invoke(size) ?: throw RuntimeException("No sideeffect for nextByteArray!")
    }

    fun clear() {
        nextInt = null
        nextIntRanged = null
        nextDouble = null
        nextBoolean = null
        nextFloat = null
        nextLong = null
        nextByteArray = null
    }
}
