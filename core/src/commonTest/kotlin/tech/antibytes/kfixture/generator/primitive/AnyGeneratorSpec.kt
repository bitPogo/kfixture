/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.primitive

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertNotSame
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi

class AnyGeneratorSpec {
    @Test
    @JsName("fn0")
    fun `It fulfils Generator`() {
        val generator: Any = AnyGenerator

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Suppress("USELESS_IS_CHECK")
    @Test
    @JsName("fn1")
    fun `Given generate is called it returns object`() {
        // When
        val actual = AnyGenerator.generate()

        // Then
        assertTrue(actual is Any)
    }

    @Test
    @JsName("fn2")
    fun `Given generate is called it returns distinct objects`() {
        // When
        val obj1 = AnyGenerator.generate()
        val obj2 = AnyGenerator.generate()

        // Then
        assertNotSame(
            actual = obj1,
            illegal = obj2,
        )
    }
}
