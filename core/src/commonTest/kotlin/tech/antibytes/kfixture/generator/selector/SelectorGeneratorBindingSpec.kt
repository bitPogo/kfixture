/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.selector

import kotlin.js.JsName
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.mock.ConfigurationStub
import tech.antibytes.kfixture.qualifier.qualifiedBy

class SelectorGeneratorBindingSpec {
    @Test
    @JsName("fn0")
    fun `Given useSelector is called it adds a Selector to the Fixture Configuration`() {
        // Given
        val configuration = ConfigurationStub()
        var capturedClass: KClass<*>? = null
        var capturedFactory: PublicApi.GeneratorFactory<*>? = null
        var capturedQualifier: PublicApi.Qualifier? = null

        configuration.addGenerator = { givenClass, givenFactory, givenQualifier ->
            capturedClass = givenClass
            capturedFactory = givenFactory
            capturedQualifier = givenQualifier
        }

        // When
        val actual = configuration.useSelector(
            listOf(1, 2, 3),
        )

        // Then
        assertSame(
            actual = actual,
            expected = configuration,
        )

        assertEquals(
            actual = capturedClass,
            expected = Int::class,
        )
        assertTrue(
            capturedFactory is PublicApi.GeneratorFactory<*>,
        )
        assertEquals(
            actual = capturedQualifier,
            expected = null,
        )
    }

    @Test
    @JsName("fn1")
    fun `Given useSelector is called with an Qualifier it adds a Selector to the Fixture Configuration`() {
        // Given
        val configuration = ConfigurationStub()
        var capturedClass: KClass<*>? = null
        var capturedFactory: PublicApi.GeneratorFactory<*>? = null
        var capturedQualifier: PublicApi.Qualifier? = null

        val qualifier = qualifiedBy("test")

        configuration.addGenerator = { givenClass, givenFactory, givenQualifier ->
            capturedClass = givenClass
            capturedFactory = givenFactory
            capturedQualifier = givenQualifier
        }

        // When
        val actual = configuration.useSelector(
            listOf(1, 2, 3),
            qualifier,
        )

        // Then
        assertSame(
            actual = actual,
            expected = configuration,
        )

        assertEquals(
            actual = capturedClass,
            expected = Int::class,
        )
        assertTrue(
            capturedFactory is PublicApi.GeneratorFactory<*>,
        )
        assertEquals(
            actual = capturedQualifier,
            expected = qualifier,
        )
    }
}
