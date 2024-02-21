/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.intregration

import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.listFixture

class KotlinFixtureSpec {
    @Test
    @JsName("fn0")
    fun `Given kotlinFixture is called it returns a Fixture`() {
        val fixture: Any = kotlinFixture()

        assertTrue(fixture is PublicApi.Fixture)
        assertEquals(
            actual = fixture.fixture<Int>(),
            expected = Random(0).nextInt(),
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
            expected = Random(givenSeed).nextInt(),
        )
    }

    @Test
    @JsName("fn2")
    fun `Given kotlinFixture is called with a ConfigurationAction it returns a Fixture for Generics`() {
        val givenSeed = 23

        val fixture = kotlinFixture {
            seed = givenSeed
        }

        val result: List<List<String>> = fixture.listFixture(
            nestedGenerator = { fixture.listFixture() },
        )

        assertNotNull(result)
    }

    @Test
    @JsName("fn3")
    fun `Given kotlinFixture is called with a ConfigurationAction it returns a Fixture for Strings`() {
        val fixture = kotlinFixture()

        val result: String = fixture.fixture(
            range = 'a'..'z',
        )

        assertNotNull(result)
    }
}
