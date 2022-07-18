/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.test.Test
import kotlin.test.assertEquals

class ClassNameResolverSpec {
    @Test
    fun `Given resolveClassName is called with a name it returns the Name of the Class`() {
        // Given
        val clazz = Int::class

        // When
        val result = resolveClassName(clazz)

        // Then
        assertEquals(
            actual = clazz.qualifiedName,
            expected = result,
        )
    }
}
