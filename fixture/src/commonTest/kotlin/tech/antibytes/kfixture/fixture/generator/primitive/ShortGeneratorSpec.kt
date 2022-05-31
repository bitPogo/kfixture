/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.fixture.generator.primitive

import co.touchlab.stately.isolate.IsolateState
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture.mock.RandomStub
import tech.antibytes.kfixture.generator.primitive.ShortGenerator
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShortGeneratorSpec {
    private val random = IsolateState { RandomStub() }

    @AfterTest
    fun tearDown() {
        random.access { it.clear() }
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn0")
    fun `It fulfils Generator`() {
        val generator: Any = ShortGenerator(random as IsolateState<Random>)

        assertTrue(generator is PublicApi.Generator<*>)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    @JsName("fn1")
    fun `Given generate is called it returns a Short`() {
        // Given
        val expected = 555
        random.access { it.nextInt = { expected } }

        val generator = ShortGenerator(random as IsolateState<Random>)

        // When
        val result = generator.generate()

        // Then
        assertEquals(
            actual = result,
            expected = expected.toShort()
        )
    }
}
