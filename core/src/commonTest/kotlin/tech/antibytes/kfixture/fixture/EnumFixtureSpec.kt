/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.fixture

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.atomicfu.atomic
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.enumFixture
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.mock.RandomStub

class EnumFixtureSpec {
    private val random = RandomStub()
    private val capturedMinimum = atomic(-1)
    private val capturedMaximum = atomic(-1)

    enum class TestEnum {
        TEST1,
        TEST2,
        TEST3,
    }

    @AfterTest
    fun tearDown() {
        random.clear()
        capturedMinimum.getAndSet(-1)
        capturedMaximum.getAndSet(-1)
    }

    @Test
    @JsName("fn0")
    fun `It fulfils Fixture`() {
        val fixture: Any = Fixture(random, emptyMap())

        assertTrue(fixture is PublicApi.Fixture)
    }

    @Test
    @JsName("fn1")
    fun `Given enumFixture is called it returns a item of an Enum`() {
        // Given
        val expected = TestEnum.TEST2
        val index = 1

        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)

            index
        }

        val fixture = Fixture(random, emptyMap())

        // When
        val result: TestEnum = fixture.enumFixture()

        // Then
        assertEquals(
            actual = capturedMinimum.value,
            expected = 0,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 3,
        )
        assertSame(
            actual = result,
            expected = expected,
        )
    }

    @Test
    @JsName("fn2")
    fun `Given enumFixture with a predicate is called it returns a item of an Enum`() {
        // Given
        val expected = TestEnum.TEST2
        val indices = mutableListOf(0, 2, 1)

        random.nextIntRanged = { _, _ -> indices.removeFirst() }

        val fixture = Fixture(random, emptyMap())

        // When
        val result: TestEnum = fixture.enumFixture { value ->
            value != TestEnum.TEST1 && value != TestEnum.TEST3
        }

        // Then
        assertSame(
            actual = result,
            expected = expected,
        )
    }

    @Test
    @JsName("fn3")
    fun `Given fixture is called it returns a item of an Enum`() {
        // Given
        val expected = TestEnum.TEST2
        val index = 1

        random.nextIntRanged = { givenMinimum, givenMaximum ->
            capturedMinimum.getAndSet(givenMinimum)
            capturedMaximum.getAndSet(givenMaximum)

            index
        }

        val fixture = Fixture(random, emptyMap())

        // When
        val result: TestEnum = fixture.fixture(type = Enum::class)

        // Then
        assertEquals(
            actual = capturedMinimum.value,
            expected = 0,
        )
        assertEquals(
            actual = capturedMaximum.value,
            expected = 3,
        )
        assertSame(
            actual = result,
            expected = expected,
        )
    }

    @Test
    @JsName("fn4")
    fun `Given fixture with a predicate is called it returns a item of an Enum`() {
        // Given
        val expected = TestEnum.TEST2
        val indices = mutableListOf(0, 2, 1)

        random.nextIntRanged = { _, _ -> indices.removeFirst() }

        val fixture = Fixture(random, emptyMap())

        // When
        val result: TestEnum = fixture.fixture(
            type = Enum::class,
        ) { value ->
            value != TestEnum.TEST1 && value != TestEnum.TEST3
        }

        // Then
        assertSame(
            actual = result,
            expected = expected,
        )
    }
}
