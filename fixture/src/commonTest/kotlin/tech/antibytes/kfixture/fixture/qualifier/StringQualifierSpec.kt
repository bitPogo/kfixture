/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.fixture.qualifier

import tech.antibytes.kfixture.FixtureContract.Companion.QUALIFIER_PREFIX
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.qualifier.StringQualifier
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StringQualifierSpec {
    @Test
    @JsName("It_fulfils_Qualifier")
    fun `It fulfils Qualifier`() {
        val qualifier: Any = StringQualifier("asd")

        assertTrue(qualifier is PublicApi.Qualifier)
    }

    @Test
    @JsName("It_has_an_value")
    fun `It has an value`() {
        val value = "ad"
        val qualifier = StringQualifier(value)

        assertEquals(
            actual = qualifier.value,
            expected = "${QUALIFIER_PREFIX}$value"
        )
    }
}
