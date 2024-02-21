/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("OPT_IN_USAGE")

package tech.antibytes.kfixture.qualifier

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import tech.antibytes.kfixture.FixtureContract
import tech.antibytes.kfixture.int

class QualifierSpec {
    @Test
    @JsName("fn0")
    fun `Given qualifiedBy is called with String it returns a Qualifier`() {
        // Given
        val id = "id"

        // When
        val result = qualifiedBy(id)

        // Then
        assertTrue(result is StringQualifier)
        assertEquals(
            actual = result.value,
            expected = "${FixtureContract.QUALIFIER_PREFIX}$id",
        )
    }

    @Test
    @JsName("fn1")
    fun `Given qualifiedBy is called with Enum it returns a Qualifier`() {
        // Given
        val id = TestEnum.TEST

        // When
        val result = qualifiedBy(id)

        // Then
        assertTrue(result is StringQualifier)
        assertEquals(
            actual = result.value,
            expected = "${FixtureContract.QUALIFIER_PREFIX}${id.toString().lowercase()}",
        )
    }

    @Test
    @JsName("fn2")
    fun `Given resolveQualifier is called with Qualifiers it returns a String`() {
        // Given
        val qualifier1 = StringQualifier("abc")
        val qualifier2 = TypeQualifier(Int::class)

        // When
        val result = resolveQualifier(qualifier1, qualifier2)

        // Then
        assertEquals(
            actual = result,
            expected = "q:abc:$int",
        )
    }
}

private enum class TestEnum {
    TEST,
}
