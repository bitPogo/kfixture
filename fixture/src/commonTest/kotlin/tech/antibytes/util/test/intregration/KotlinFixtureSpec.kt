/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.test.intregration

import tech.antibytes.util.test.fixture.PublicApi
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KotlinFixtureSpec {
    @Test
    @JsName("fn0")
    fun `Given kotlinFixture is called it returns a Fixture`() {
        val fixture: Any = kotlinFixture()

        assertTrue(fixture is PublicApi.Fixture)
        assertEquals(
            actual = fixture.fixture<Int>(),
            expected = Random(0).nextInt()
        )
    }

    @Test
    @JsName("fn1")
    fun `Given kotlinFixture is called with a ConfigurationAction it returns a Fixture`() {
        val givenSeed = 23

        val fixture: Any = kotlinFixture {
            seed = givenSeed
        }

        assertTrue(fixture is PublicApi.Fixture)
        assertEquals(
            actual = fixture.fixture(),
            expected = Random(givenSeed).nextInt()
        )
    }
}
