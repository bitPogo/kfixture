/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.fixture.qualifier

import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.qualifier.TypeQualifier
import tech.antibytes.kfixture.resolveClassName
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TypeQualifierSpec {
    @Test
    @JsName("It_fulfils_Qualifier")
    fun `It fulfils Qualifier`() {
        val qualifier: Any = TypeQualifier(Int::class)

        assertTrue(qualifier is PublicApi.Qualifier)
    }

    @Test
    @JsName("It_contains_a_value")
    fun `It contains a value`() {
        val clazz = Int::class
        val qualifier = TypeQualifier(clazz)

        assertEquals(
            actual = qualifier.value,
            expected = resolveClassName(clazz)
        )
    }
}
