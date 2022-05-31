/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.fixture

import tech.antibytes.kfixture.resolveClassName
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class ClassNameResolverSpec {
    @Test
    @JsName("Given_resolveClassName_is_called_with_a_name_it_returns_the_Name_of_the_Class")
    fun `Given resolveClassName is called with a name it returns the Name of the Class`() {
        // Given
        val clazz = Int::class

        // When
        val result = resolveClassName(clazz)

        // Then
        assertEquals(
            actual = clazz.simpleName,
            expected = result
        )
    }
}
