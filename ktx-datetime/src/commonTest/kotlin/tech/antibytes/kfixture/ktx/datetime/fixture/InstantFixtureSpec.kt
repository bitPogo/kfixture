/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime.fixture

import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlinx.datetime.Instant
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.ktx.datetime.defaultPredicate
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.Fixture
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.RangedGeneratorStub
import tech.antibytes.kfixture.qualifier.qualifiedBy
import tech.antibytes.kfixture.qualifier.resolveGeneratorId

private data class TypeDescriptor<RangeType, FixtureType : Any>(
    val from: RangeType,
    val to: RangeType,
    val range: ClosedRange<RangeType>,
    val expected: FixtureType,
    val classId: String,
) where RangeType : Any, RangeType : Comparable<RangeType>

class InstantFixtureSpec {
    private val random = RandomStub()

    private val instantDescriptor = TypeDescriptor(
        from = 0L,
        to = 9L,
        range = 0L..9L,
        expected = Instant.fromEpochMilliseconds(716531),
        classId = resolveGeneratorId(Instant::class),
    )

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType : Any, reified FixtureType : Any> getRangeFixtureExtension(): Function5<
        PublicApi.Fixture,
        RangeType,
        RangeType,
        PublicApi.Qualifier?,
        Function1<RangeType?, Boolean>,
        FixtureType,
        > {
        return when (FixtureType::class) {
            Instant::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Long, Instant>(
                    from = from as Long,
                    to = to as Long,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Long?, Boolean>,
                ) as FixtureType
            }
            else -> throw RuntimeException()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType : Any, reified FixtureType : Any> getNullableRangeFixtureExtension(): Function5<
        PublicApi.Fixture,
        RangeType,
        RangeType,
        PublicApi.Qualifier?,
        Function1<RangeType?, Boolean>,
        FixtureType?,
        > {
        return when (FixtureType::class) {
            Instant::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Long, Instant?>(
                    from = from as Long,
                    to = to as Long,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Long?, Boolean>,
                ) as FixtureType?
            }
            else -> throw RuntimeException()
        }
    }

    private inline fun <reified RangeType, reified FixtureType : Any> runTest1(
        from: RangeType,
        to: RangeType,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val generator = RangedGeneratorStub<RangeType, FixtureType>()
        generator.generateWithRange = { _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            getRangeFixtureExtension<RangeType, FixtureType>()(
                fixture,
                from,
                to,
                null,
                ::defaultPredicate,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($classId).",
        )
    }

    @Test
    @JsName("fn1")
    fun `Given fixture is called with a upper and lower bound it fails if the Type has no corresponding Generator`() {
        runTest1(
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    private inline fun <reified RangeType, reified FixtureType : Any> runTest2(
        from: RangeType,
        to: RangeType,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val generator = RangedGeneratorStub<RangeType, FixtureType>()
        generator.generateWithRange = { _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            getRangeFixtureExtension<RangeType, FixtureType>()(
                fixture,
                from,
                to,
                null,
            ) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($classId).",
        )
    }

    @Test
    @JsName("fn2")
    fun `Given fixture is called with a upper and lower bound and a predicate it fails if the Type has no corresponding Generator`() {
        runTest2(
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    private inline fun <reified RangeType, reified FixtureType : Any> runTest3(
        from: RangeType,
        to: RangeType,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val generator = FilterableGeneratorStub<RangeType, FixtureType>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, mapOf(classId to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            getRangeFixtureExtension<RangeType, FixtureType>()(
                fixture,
                from,
                to,
                null,
                ::defaultPredicate,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($classId).",
        )
    }

    @Test
    @JsName("fn3")
    fun `Given fixture is called with a upper and lower bound it fails the corresponding Generator is not a RangedGenerator`() {
        runTest3(
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    private inline fun <reified RangeType, reified FixtureType : Any> runTest4(
        from: RangeType,
        to: RangeType,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val generator = FilterableGeneratorStub<RangeType, FixtureType>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, mapOf(classId to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            getRangeFixtureExtension<RangeType, FixtureType>()(
                fixture,
                from,
                to,
                null,
            ) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($classId).",
        )
    }

    @Test
    @JsName("fn4")
    fun `Given fixture is called with a upper and lower bound and predicate it fails the corresponding Generator is not a RangedGenerator`() {
        runTest4(
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    private inline fun <reified RangeType, reified FixtureType : Any> runTest5(
        from: RangeType,
        to: RangeType,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val generator = RangedGeneratorStub<RangeType, FixtureType>()

        var capturedFrom: RangeType? = null
        var capturedTo: RangeType? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, mapOf(classId to generator))

        // When
        val result = getRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            from,
            to,
            null,
            ::defaultPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = from,
        )
        assertEquals(
            actual = capturedTo,
            expected = to,
        )
    }

    @Test
    @JsName("fn5")
    fun `Given fixture is called with a upper and lower bound it returns a Fixture for the derived Type`() {
        runTest5(
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    private inline fun <reified RangeType, reified FixtureType : Any> runTest6(
        from: RangeType,
        to: RangeType,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val expectedPredicate: Function1<RangeType?, Boolean> = { true }
        val generator = RangedGeneratorStub<RangeType, FixtureType>()

        var capturedFrom: RangeType? = null
        var capturedTo: RangeType? = null
        var capturedPredicate: Function1<RangeType?, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, mapOf(classId to generator))

        // When

        val result = getRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            from,
            to,
            null,
            expectedPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = from,
        )
        assertEquals(
            actual = capturedTo,
            expected = to,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @JsName("fn6")
    fun `Given fixture is called with a upper and lower bound and predicate it returns a Fixture for the derived Type`() {
        runTest6(
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    private inline fun <reified RangeType, reified FixtureType : Any> runTest7(
        from: RangeType,
        to: RangeType,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val generator = RangedGeneratorStub<RangeType, FixtureType>()

        var capturedFrom: RangeType? = null
        var capturedTo: RangeType? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, mapOf(classId to generator))

        // When
        val result = getNullableRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            from,
            to,
            null,
            ::defaultPredicate,
        )

        // Then
        assertNull(result)
        assertNull(capturedFrom)
        assertNull(capturedTo)
    }

    @Test
    @JsName("fn7")
    fun `Given fixture is called with a upper and lower bound it returns a Fixture while respecting nullability`() {
        runTest7(
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    private inline fun <reified RangeType, reified FixtureType : Any> runTest8(
        from: RangeType,
        to: RangeType,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val expectedPredicate: Function1<RangeType?, Boolean> = { true }
        val generator = RangedGeneratorStub<RangeType, FixtureType>()

        var capturedFrom: RangeType? = null
        var capturedTo: RangeType? = null
        var capturedPredicate: Function1<RangeType, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, mapOf(classId to generator))

        // When
        val result = getNullableRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            from,
            to,
            null,
            expectedPredicate,
        )

        // Then
        assertNull(result)
        assertNull(capturedFrom)
        assertNull(capturedTo)
        assertNull(capturedPredicate)
    }

    @Test
    @JsName("fn8")
    fun `Given fixture is called with a upper and lower bound with a predicate it returns a Fixture while respecting nullability`() {
        runTest8(
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    private inline fun <reified RangeType, reified FixtureType : Any> runTest9(
        from: RangeType,
        to: RangeType,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val qualifier = "test"
        val generator = RangedGeneratorStub<RangeType, FixtureType>()

        var capturedFrom: RangeType? = null
        var capturedTo: RangeType? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$classId" to generator),
        )

        // When
        val result = getRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            from,
            to,
            qualifiedBy(qualifier),
            ::defaultPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = from,
        )
        assertEquals(
            actual = capturedTo,
            expected = to,
        )
    }

    @Test
    @JsName("fn9")
    fun `Given fixture is called  with a upper and lower bound and a qualifier it returns a Fixture for the derived Type`() {
        runTest9(
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    private inline fun <reified RangeType, reified FixtureType : Any> runTest10(
        from: RangeType,
        to: RangeType,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val expectedPredicate: Function1<RangeType?, Boolean> = { true }
        val qualifier = "test"
        val generator = RangedGeneratorStub<RangeType, FixtureType>()

        var capturedFrom: RangeType? = null
        var capturedTo: RangeType? = null
        var capturedPredicate: Function1<RangeType, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$classId" to generator),
        )

        // When
        val result = getRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            from,
            to,
            qualifiedBy(qualifier),
            expectedPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = from,
        )
        assertEquals(
            actual = capturedTo,
            expected = to,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @JsName("fn10")
    fun `Given fixture is called  with a upper and lower bound and a qualifier and a predicate it returns a Fixture for the derived Type`() {
        runTest10(
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType : Any, reified FixtureType : Any> getCloseRangeFixtureExtension(): Function4<
        PublicApi.Fixture,
        ClosedRange<Comparable<Any>>,
        PublicApi.Qualifier?,
        Function1<RangeType?, Boolean>,
        FixtureType,
        > {
        return when (FixtureType::class) {
            Instant::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Long, Instant>(
                    range = range as ClosedRange<Long>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Long?, Boolean>,
                ) as FixtureType
            }
            else -> throw RuntimeException()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType : Any, reified FixtureType : Any> getNullableCloseRangeFixtureExtension(): Function4<
        PublicApi.Fixture,
        ClosedRange<Comparable<Any>>,
        PublicApi.Qualifier?,
        Function1<RangeType?, Boolean>,
        FixtureType?,
        > {
        return when (FixtureType::class) {
            Instant::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Long, Instant?>(
                    range = range as ClosedRange<Long>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Long?, Boolean>,
                ) as FixtureType?
            }
            else -> throw RuntimeException()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType, reified FixtureType : Any> runTest11(
        range: ClosedRange<RangeType>,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val generator = RangedGeneratorStub<RangeType, FixtureType>()
        generator.generateWithRange = { _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            getCloseRangeFixtureExtension<RangeType, FixtureType>()(
                fixture,
                range as ClosedRange<Comparable<Any>>,
                null,
                ::defaultPredicate,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($classId).",
        )
    }

    @Test
    @JsName("fn11")
    fun `Given fixture is called with a range it fails if the Type has no corresponding Generator`() {
        runTest11(
            range = instantDescriptor.range,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType, reified FixtureType : Any> runTest12(
        range: ClosedRange<RangeType>,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val generator = RangedGeneratorStub<RangeType, FixtureType>()
        generator.generateWithRange = { _, _, _ ->
            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, emptyMap())

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            getCloseRangeFixtureExtension<RangeType, FixtureType>()(
                fixture,
                range as ClosedRange<Comparable<Any>>,
                null,
            ) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($classId).",
        )
    }

    @Test
    @JsName("fn12")
    fun `Given fixture is called with a range and a predicate it fails if the Type has no corresponding Generator`() {
        runTest12(
            range = instantDescriptor.range,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType, reified FixtureType : Any> runTest13(
        range: ClosedRange<RangeType>,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val generator = FilterableGeneratorStub<RangeType, FixtureType>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, mapOf(classId to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            getCloseRangeFixtureExtension<RangeType, FixtureType>()(
                fixture,
                range as ClosedRange<Comparable<Any>>,
                null,
                ::defaultPredicate,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($classId).",
        )
    }

    @Test
    @JsName("fn13")
    fun `Given fixture is called with a range it fails the corresponding Generator is not a RangedGenerator`() {
        runTest13(
            range = instantDescriptor.range,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType, reified FixtureType : Any> runTest14(
        range: ClosedRange<RangeType>,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val generator = FilterableGeneratorStub<RangeType, FixtureType>()
        generator.generate = { expected }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, mapOf(classId to generator))

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            getCloseRangeFixtureExtension<RangeType, FixtureType>()(
                fixture,
                range as ClosedRange<Comparable<Any>>,
                null,
            ) { false }
        }

        assertEquals(
            actual = error.message,
            expected = "Missing Generator for ClassID ($classId).",
        )
    }

    @Test
    @JsName("fn14")
    fun `Given fixture is called with a range and a predicate it fails the corresponding Generator is not a RangedGenerator`() {
        runTest14(
            range = instantDescriptor.range,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType, reified FixtureType : Any> runTest15(
        from: RangeType,
        to: RangeType,
        range: ClosedRange<RangeType>,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        val generator = RangedGeneratorStub<RangeType, FixtureType>()

        var capturedFrom: RangeType? = null
        var capturedTo: RangeType? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, mapOf(classId to generator))

        // When
        val result = getCloseRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            range as ClosedRange<Comparable<Any>>,
            null,
            ::defaultPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = from,
        )
        assertEquals(
            actual = capturedTo,
            expected = to,
        )
    }

    @Test
    @JsName("fn15")
    fun `Given fixture is called with a range it returns a Fixture for the derived Type`() {
        runTest15(
            range = instantDescriptor.range,
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType, reified FixtureType : Any> runTest16(
        from: RangeType,
        to: RangeType,
        range: ClosedRange<RangeType>,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val expectedPredicate: Function1<RangeType?, Boolean> = { true }
        val generator = RangedGeneratorStub<RangeType, FixtureType>()

        var capturedFrom: RangeType? = null
        var capturedTo: RangeType? = null
        var capturedPredicate: Function1<RangeType, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, mapOf(classId to generator))

        // When
        val result = getCloseRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            range as ClosedRange<Comparable<Any>>,
            null,
            expectedPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = from,
        )
        assertEquals(
            actual = capturedTo,
            expected = to,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @JsName("fn16")
    fun `Given fixture is called with a range and a predicate it returns a Fixture for the derived Type`() {
        runTest16(
            range = instantDescriptor.range,
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType, reified FixtureType : Any> runTest17(
        range: ClosedRange<RangeType>,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val generator = RangedGeneratorStub<RangeType, FixtureType>()

        var capturedFrom: RangeType? = null
        var capturedTo: RangeType? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, mapOf(classId to generator))

        // When
        val result = getNullableCloseRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            range as ClosedRange<Comparable<Any>>,
            null,
            ::defaultPredicate,
        )

        // Then
        assertNull(result)
        assertNull(capturedFrom)
        assertNull(capturedTo)
    }

    @Test
    @JsName("fn17")
    fun `Given fixture is called with a range it returns a Fixture while respecting nullability`() {
        runTest17(
            range = instantDescriptor.range,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType, reified FixtureType : Any> runTest18(
        range: ClosedRange<RangeType>,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val expectedPredicate: Function1<RangeType?, Boolean> = { true }
        val generator = RangedGeneratorStub<RangeType, FixtureType>()

        var capturedFrom: RangeType? = null
        var capturedTo: RangeType? = null
        var capturedPredicate: Function1<RangeType, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        random.nextBoolean = { true }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(random, mapOf(classId to generator))

        // When
        val result = getNullableCloseRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            range as ClosedRange<Comparable<Any>>,
            null,
            expectedPredicate,
        )

        // Then
        assertNull(result)
        assertNull(capturedFrom)
        assertNull(capturedTo)
        assertNull(capturedPredicate)
    }

    @Test
    @JsName("fn18")
    fun `Given fixture is called with a range and a predicate it returns a Fixture while respecting nullability`() {
        runTest18(
            range = instantDescriptor.range,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType, reified FixtureType : Any> runTest19(
        from: RangeType,
        to: RangeType,
        range: ClosedRange<RangeType>,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val qualifier = "test"
        val generator = RangedGeneratorStub<RangeType, FixtureType>()

        var capturedFrom: RangeType? = null
        var capturedTo: RangeType? = null

        generator.generateWithRange = { givenFrom, givenTo, _ ->
            capturedFrom = givenFrom
            capturedTo = givenTo

            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$classId" to generator),
        )

        // When
        val result = getCloseRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            range as ClosedRange<Comparable<Any>>,
            qualifiedBy(qualifier),
            ::defaultPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = from,
        )
        assertEquals(
            actual = capturedTo,
            expected = to,
        )
    }

    @Test
    @JsName("fn19")
    fun `Given fixture is called with a range and a qualifier it returns a Fixture for the derived Type`() {
        runTest19(
            range = instantDescriptor.range,
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType, reified FixtureType : Any> runTest20(
        from: RangeType,
        to: RangeType,
        range: ClosedRange<RangeType>,
        classId: String,
        expected: FixtureType,
    ) where RangeType : Any, RangeType : Comparable<RangeType> {
        // Given
        val qualifier = "test"
        val expectedPredicate: Function1<RangeType?, Boolean> = { true }
        val generator = RangedGeneratorStub<RangeType, FixtureType>()

        var capturedFrom: RangeType? = null
        var capturedTo: RangeType? = null
        var capturedPredicate: Function1<RangeType, Boolean>? = null

        generator.generateWithRange = { givenFrom, givenTo, givenPredicate ->
            capturedFrom = givenFrom
            capturedTo = givenTo
            capturedPredicate = givenPredicate

            expected
        }

        // Ensure stable names since reified is in play
        resolveGeneratorId(FixtureType::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$classId" to generator),
        )

        // When
        val result = getCloseRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            range as ClosedRange<Comparable<Any>>,
            qualifiedBy(qualifier),
            expectedPredicate,
        )

        // Then
        assertEquals(
            actual = result,
            expected = expected,
        )
        assertEquals(
            actual = capturedFrom,
            expected = from,
        )
        assertEquals(
            actual = capturedTo,
            expected = to,
        )
        assertSame(
            actual = capturedPredicate,
            expected = expectedPredicate,
        )
    }

    @Test
    @JsName("fn20")
    fun `Given fixture is called with a range and a qualifier and a predicate it returns a Fixture for the derived Type`() {
        runTest20(
            range = instantDescriptor.range,
            from = instantDescriptor.from,
            to = instantDescriptor.to,
            classId = instantDescriptor.classId,
            expected = instantDescriptor.expected,
        )
    }
}
