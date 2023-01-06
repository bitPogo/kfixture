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
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import tech.antibytes.kfixture.Fixture
import tech.antibytes.kfixture.IgnoreAndroid
import tech.antibytes.kfixture.IgnoreJvm
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.byteArray
import tech.antibytes.kfixture.charArray
import tech.antibytes.kfixture.defaultPredicate
import tech.antibytes.kfixture.doubleArray
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.floatArray
import tech.antibytes.kfixture.intArray
import tech.antibytes.kfixture.longArray
import tech.antibytes.kfixture.mock.FilterableGeneratorStub
import tech.antibytes.kfixture.mock.RandomStub
import tech.antibytes.kfixture.mock.RangedGeneratorStub
import tech.antibytes.kfixture.qualifier.StringQualifier
import tech.antibytes.kfixture.resolveClassName
import tech.antibytes.kfixture.shortArray
import tech.antibytes.kfixture.string
import tech.antibytes.kfixture.uByteArray
import tech.antibytes.kfixture.uIntArray
import tech.antibytes.kfixture.uLongArray
import tech.antibytes.kfixture.uShortArray

private data class TypeDescriptor<RangeType, FixtureType : Any>(
    val from: RangeType,
    val to: RangeType,
    val range: ClosedRange<RangeType>,
    val expected: FixtureType,
    val classId: String,
) where RangeType : Any, RangeType : Comparable<RangeType>

private data class ByteRange(
    override val start: Byte,
    override val endInclusive: Byte,
) : ClosedRange<Byte>

private data class ShortRange(
    override val start: Short,
    override val endInclusive: Short,
) : ClosedRange<Short>

private data class UByteRange(
    override val start: UByte,
    override val endInclusive: UByte,
) : ClosedRange<UByte>

private data class UShortRange(
    override val start: UShort,
    override val endInclusive: UShort,
) : ClosedRange<UShort>

class RangedSpecialArrayFixtureSpec {
    private val random = RandomStub()

    private val charArrayDescriptors = TypeDescriptor(
        from = '0',
        to = '9',
        range = '0'..'9',
        expected = "23".toCharArray(),
        classId = charArray,
    )

    private val stringArrayDescriptor = TypeDescriptor(
        from = '0',
        to = '9',
        range = '0'..'9',
        expected = "23",
        classId = string,
    )

    private val byteArrayDescriptor = TypeDescriptor(
        from = 0.toByte(),
        to = 24.toByte(),
        range = ByteRange(0.toByte(), 24.toByte()),
        expected = "23".encodeToByteArray(),
        classId = byteArray,
    )

    private val shortArrayDescriptor = TypeDescriptor(
        from = 0.toShort(),
        to = 9.toShort(),
        range = ShortRange(0.toShort(), 9.toShort()),
        expected = shortArrayOf(2, 3),
        classId = shortArray,
    )

    private val intArrayDescriptor = TypeDescriptor(
        from = 0,
        to = 9,
        range = 0..9,
        expected = intArrayOf(2, 3),
        classId = intArray,
    )

    private val floatArrayDescriptor = TypeDescriptor(
        from = 0.0f,
        to = 1.0f,
        range = 0.0f..1.0f,
        expected = floatArrayOf(0.2f, 1.3f),
        classId = floatArray,
    )

    private val longArrayDescriptor = TypeDescriptor(
        from = 0L,
        to = 9L,
        range = 0L..9L,
        expected = longArrayOf(2, 3),
        classId = longArray,
    )

    private val doubleArrayDescriptor = TypeDescriptor(
        from = 0.0,
        to = 1.0,
        range = 0.0..1.0,
        expected = doubleArrayOf(0.2, 1.3),
        classId = doubleArray,
    )

    private val ubyteArrayDescriptor = TypeDescriptor(
        from = 0.toUByte(),
        to = 24.toUByte(),
        range = UByteRange(0.toUByte(), 24.toUByte()),
        expected = "23".encodeToByteArray().toUByteArray(),
        classId = uByteArray,
    )

    private val ushortArrayDescriptor = TypeDescriptor(
        from = 0.toUShort(),
        to = 9.toUShort(),
        range = UShortRange(0.toUShort(), 9.toUShort()),
        expected = shortArrayOf(2, 3).toUShortArray(),
        classId = uShortArray,
    )

    private val uintArrayDescriptor = TypeDescriptor(
        from = 0u,
        to = 9u,
        range = 0u..9u,
        expected = intArrayOf(2, 3).toUIntArray(),
        classId = uIntArray,
    )

    private val ulongArrayDescriptor = TypeDescriptor(
        from = 0uL,
        to = 9uL,
        range = 0uL..9uL,
        expected = longArrayOf(2, 3).toULongArray(),
        classId = uLongArray,
    )

    @AfterTest
    fun tearDown() {
        random.clear()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils Fixture`() {
        val fixture: Any = Fixture(random, emptyMap())

        assertTrue(fixture is PublicApi.Fixture)
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType : Any, reified FixtureType : Any> getRangeFixtureExtension(): Function5<
        PublicApi.Fixture,
        RangeType,
        RangeType,
        PublicApi.Qualifier?,
        Function1<RangeType?, Boolean>,
        FixtureType,> {
        return when (FixtureType::class) {
            CharArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Char, CharArray>(
                    from = from as Char,
                    to = to as Char,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Char?, Boolean>,
                ) as FixtureType
            }
            String::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Char, String>(
                    from = from as Char,
                    to = to as Char,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Char?, Boolean>,
                ) as FixtureType
            }
            ByteArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Byte, ByteArray>(
                    from = from as Byte,
                    to = to as Byte,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Byte?, Boolean>,
                ) as FixtureType
            }
            ShortArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Short, ShortArray>(
                    from = from as Short,
                    to = to as Short,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Short?, Boolean>,
                ) as FixtureType
            }
            IntArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Int, IntArray>(
                    from = from as Int,
                    to = to as Int,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Int?, Boolean>,
                ) as FixtureType
            }
            FloatArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Float, FloatArray>(
                    from = from as Float,
                    to = to as Float,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Float?, Boolean>,
                ) as FixtureType
            }
            LongArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Long, LongArray>(
                    from = from as Long,
                    to = to as Long,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Long?, Boolean>,
                ) as FixtureType
            }
            DoubleArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Double, DoubleArray>(
                    from = from as Double,
                    to = to as Double,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Double?, Boolean>,
                ) as FixtureType
            }
            UByteArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<UByte, UByteArray>(
                    from = from as UByte,
                    to = to as UByte,
                    qualifier = qualifier,
                    predicate = predicate as Function1<UByte?, Boolean>,
                ) as FixtureType
            }
            UShortArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<UShort, UShortArray>(
                    from = from as UShort,
                    to = to as UShort,
                    qualifier = qualifier,
                    predicate = predicate as Function1<UShort?, Boolean>,
                ) as FixtureType
            }
            UIntArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<UInt, UIntArray>(
                    from = from as UInt,
                    to = to as UInt,
                    qualifier = qualifier,
                    predicate = predicate as Function1<UInt?, Boolean>,
                ) as FixtureType
            }
            ULongArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<ULong, ULongArray>(
                    from = from as ULong,
                    to = to as ULong,
                    qualifier = qualifier,
                    predicate = predicate as Function1<ULong?, Boolean>,
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
        FixtureType?,> {
        return when (FixtureType::class) {
            CharArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Char, CharArray?>(
                    from = from as Char,
                    to = to as Char,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Char?, Boolean>,
                ) as FixtureType?
            }
            String::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Char, String?>(
                    from = from as Char,
                    to = to as Char,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Char?, Boolean>,
                ) as FixtureType?
            }
            ByteArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Byte, ByteArray?>(
                    from = from as Byte,
                    to = to as Byte,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Byte?, Boolean>,
                ) as FixtureType?
            }
            ShortArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Short, ShortArray?>(
                    from = from as Short,
                    to = to as Short,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Short?, Boolean>,
                ) as FixtureType?
            }
            IntArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Int, IntArray?>(
                    from = from as Int,
                    to = to as Int,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Int?, Boolean>,
                ) as FixtureType?
            }
            FloatArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Float, FloatArray?>(
                    from = from as Float,
                    to = to as Float,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Float?, Boolean>,
                ) as FixtureType?
            }
            LongArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Long, LongArray?>(
                    from = from as Long,
                    to = to as Long,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Long?, Boolean>,
                ) as FixtureType?
            }
            DoubleArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<Double, DoubleArray?>(
                    from = from as Double,
                    to = to as Double,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Double?, Boolean>,
                ) as FixtureType?
            }
            UByteArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<UByte, UByteArray?>(
                    from = from as UByte,
                    to = to as UByte,
                    qualifier = qualifier,
                    predicate = predicate as Function1<UByte?, Boolean>,
                ) as FixtureType?
            }
            UShortArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<UShort, UShortArray?>(
                    from = from as UShort,
                    to = to as UShort,
                    qualifier = qualifier,
                    predicate = predicate as Function1<UShort?, Boolean>,
                ) as FixtureType?
            }
            UIntArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<UInt, UIntArray?>(
                    from = from as UInt,
                    to = to as UInt,
                    qualifier = qualifier,
                    predicate = predicate as Function1<UInt?, Boolean>,
                ) as FixtureType?
            }
            ULongArray::class -> { fixture, from, to, qualifier, predicate ->
                fixture.fixture<ULong, ULongArray?>(
                    from = from as ULong,
                    to = to as ULong,
                    qualifier = qualifier,
                    predicate = predicate as Function1<ULong?, Boolean>,
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
        resolveClassName(FixtureType::class)

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
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest1(
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest1(
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest1(
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest1(
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest1(
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest1(
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest1(
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
        runTest1(
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest1(
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest1(
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest1(
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest2(
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest2(
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest2(
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest2(
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest2(
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest2(
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest2(
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
        runTest2(
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest2(
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest2(
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest2(
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest3(
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest3(
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest3(
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest3(
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest3(
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest3(
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest3(
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
        runTest3(
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest3(
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest3(
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest3(
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest4(
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest4(
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest4(
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest4(
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest4(
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest4(
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest4(
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
        runTest4(
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest4(
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest4(
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest4(
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest5(
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest5(
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest5(
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest5(
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest5(
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest5(
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest5(
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
    }

    @Test
    @IgnoreAndroid
    // Compiler does not defer RangeType correctly
    @IgnoreJvm
    // Compiler does not defer RangeType correctly
    @JsName("fn5a")
    fun `Given fixture is called with a upper and lower bound it returns a Fixture for the derived Type for unsigned`() {
        runTest5(
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest5(
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest5(
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest5(
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest6(
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest6(
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest6(
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest6(
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest6(
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest6(
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest6(
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
    }

    @Test
    @IgnoreAndroid
    // Compiler does not defer RangeType correctly
    @IgnoreJvm
    // Compiler does not defer RangeType correctly
    @JsName("fn6a")
    fun `Given fixture is called with a upper and lower bound and predicate it returns a Fixture for the derived Type for unsigned`() {
        runTest6(
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest6(
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest6(
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest6(
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest7(
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest7(
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest7(
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest7(
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest7(
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest7(
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest7(
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
        runTest7(
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest7(
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest7(
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest7(
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest8(
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest8(
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest8(
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest8(
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest8(
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest8(
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest8(
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
        runTest8(
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest8(
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest8(
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest8(
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$classId" to generator),
        )

        // When
        val result = getRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            from,
            to,
            StringQualifier(qualifier),
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
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest9(
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest9(
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest9(
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest9(
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest9(
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest9(
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest9(
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
    }

    @Test
    @IgnoreAndroid
    // Compiler does not defer RangeType correctly
    @IgnoreJvm
    // Compiler does not defer RangeType correctly
    @JsName("fn9a")
    fun `Given fixture is called  with a upper and lower bound and a qualifier it returns a Fixture for the derived Type for unsigned`() {
        runTest9(
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest9(
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest9(
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest9(
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$classId" to generator),
        )

        // When
        val result = getRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            from,
            to,
            StringQualifier(qualifier),
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
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest10(
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest10(
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest10(
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest10(
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest10(
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest10(
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest10(
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
    }

    @Test
    @IgnoreAndroid
    // Compiler does not defer RangeType correctly
    @IgnoreJvm
    // Compiler does not defer RangeType correctly
    @JsName("fn10a")
    fun `Given fixture is called  with a upper and lower bound and a qualifier and a predicate it returns a Fixture for the derived Type for unsigned`() {
        runTest10(
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest10(
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest10(
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest10(
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified RangeType : Any, reified FixtureType : Any> getCloseRangeFixtureExtension(): Function4<
        PublicApi.Fixture,
        ClosedRange<Comparable<Any>>,
        PublicApi.Qualifier?,
        Function1<RangeType?, Boolean>,
        FixtureType,> {
        return when (FixtureType::class) {
            CharArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Char, CharArray>(
                    range = range as ClosedRange<Char>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Char?, Boolean>,
                ) as FixtureType
            }
            String::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Char, String>(
                    range = range as ClosedRange<Char>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Char?, Boolean>,
                ) as FixtureType
            }
            ByteArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Byte, ByteArray>(
                    range = range as ClosedRange<Byte>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Byte?, Boolean>,
                ) as FixtureType
            }
            ShortArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Short, ShortArray>(
                    range = range as ClosedRange<Short>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Short?, Boolean>,
                ) as FixtureType
            }
            IntArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Int, IntArray>(
                    range = range as ClosedRange<Int>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Int?, Boolean>,
                ) as FixtureType
            }
            FloatArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Float, FloatArray>(
                    range = range as ClosedRange<Float>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Float?, Boolean>,
                ) as FixtureType
            }
            LongArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Long, LongArray>(
                    range = range as ClosedRange<Long>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Long?, Boolean>,
                ) as FixtureType
            }
            DoubleArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Double, DoubleArray>(
                    range = range as ClosedRange<Double>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Double?, Boolean>,
                ) as FixtureType
            }
            UByteArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<UByte, UByteArray>(
                    range = range as ClosedRange<UByte>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<UByte?, Boolean>,
                ) as FixtureType
            }
            UShortArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<UShort, UShortArray>(
                    range = range as ClosedRange<UShort>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<UShort?, Boolean>,
                ) as FixtureType
            }
            UIntArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<UInt, UIntArray>(
                    range = range as ClosedRange<UInt>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<UInt?, Boolean>,
                ) as FixtureType
            }
            ULongArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<ULong, ULongArray>(
                    range = range as ClosedRange<ULong>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<ULong?, Boolean>,
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
        FixtureType?,> {
        return when (FixtureType::class) {
            CharArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Char, CharArray?>(
                    range = range as ClosedRange<Char>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Char?, Boolean>,
                ) as FixtureType?
            }
            String::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Char, String?>(
                    range = range as ClosedRange<Char>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Char?, Boolean>,
                ) as FixtureType?
            }
            ByteArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Byte, ByteArray?>(
                    range = range as ClosedRange<Byte>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Byte?, Boolean>,
                ) as FixtureType?
            }
            ShortArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Short, ShortArray?>(
                    range = range as ClosedRange<Short>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Short?, Boolean>,
                ) as FixtureType?
            }
            IntArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Int, IntArray?>(
                    range = range as ClosedRange<Int>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Int?, Boolean>,
                ) as FixtureType?
            }
            FloatArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Float, FloatArray?>(
                    range = range as ClosedRange<Float>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Float?, Boolean>,
                ) as FixtureType?
            }
            LongArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Long, LongArray?>(
                    range = range as ClosedRange<Long>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Long?, Boolean>,
                ) as FixtureType?
            }
            DoubleArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<Double, DoubleArray?>(
                    range = range as ClosedRange<Double>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<Double?, Boolean>,
                ) as FixtureType?
            }
            UByteArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<UByte, UByteArray?>(
                    range = range as ClosedRange<UByte>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<UByte?, Boolean>,
                ) as FixtureType?
            }
            UShortArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<UShort, UShortArray?>(
                    range = range as ClosedRange<UShort>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<UShort?, Boolean>,
                ) as FixtureType?
            }
            UIntArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<UInt, UIntArray?>(
                    range = range as ClosedRange<UInt>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<UInt?, Boolean>,
                ) as FixtureType?
            }
            ULongArray::class -> { fixture, range, qualifier, predicate ->
                fixture.fixture<ULong, ULongArray?>(
                    range = range as ClosedRange<ULong>,
                    qualifier = qualifier,
                    predicate = predicate as Function1<ULong?, Boolean>,
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
        resolveClassName(FixtureType::class)

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
            range = charArrayDescriptors.range,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest11(
            range = stringArrayDescriptor.range,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest11(
            range = byteArrayDescriptor.range,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest11(
            range = shortArrayDescriptor.range,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest11(
            range = intArrayDescriptor.range,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest11(
            range = floatArrayDescriptor.range,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest11(
            range = longArrayDescriptor.range,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest11(
            range = doubleArrayDescriptor.range,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
        runTest11(
            range = ubyteArrayDescriptor.range,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest11(
            range = ushortArrayDescriptor.range,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest11(
            range = uintArrayDescriptor.range,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest11(
            range = ulongArrayDescriptor.range,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            range = charArrayDescriptors.range,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest12(
            range = stringArrayDescriptor.range,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest12(
            range = byteArrayDescriptor.range,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest12(
            range = shortArrayDescriptor.range,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest12(
            range = intArrayDescriptor.range,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest12(
            range = floatArrayDescriptor.range,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest12(
            range = longArrayDescriptor.range,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest12(
            range = doubleArrayDescriptor.range,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
        runTest12(
            range = ubyteArrayDescriptor.range,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest12(
            range = ushortArrayDescriptor.range,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest12(
            range = uintArrayDescriptor.range,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest12(
            range = ulongArrayDescriptor.range,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            range = charArrayDescriptors.range,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest13(
            range = stringArrayDescriptor.range,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest13(
            range = byteArrayDescriptor.range,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest13(
            range = shortArrayDescriptor.range,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest13(
            range = intArrayDescriptor.range,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest13(
            range = floatArrayDescriptor.range,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest13(
            range = longArrayDescriptor.range,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest13(
            range = doubleArrayDescriptor.range,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
        runTest13(
            range = ubyteArrayDescriptor.range,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest13(
            range = ushortArrayDescriptor.range,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest13(
            range = uintArrayDescriptor.range,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest13(
            range = ulongArrayDescriptor.range,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            range = charArrayDescriptors.range,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest14(
            range = stringArrayDescriptor.range,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest14(
            range = byteArrayDescriptor.range,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest14(
            range = shortArrayDescriptor.range,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest14(
            range = intArrayDescriptor.range,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest14(
            range = floatArrayDescriptor.range,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest14(
            range = longArrayDescriptor.range,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest14(
            range = doubleArrayDescriptor.range,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
        runTest14(
            range = ubyteArrayDescriptor.range,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest14(
            range = ushortArrayDescriptor.range,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest14(
            range = uintArrayDescriptor.range,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest14(
            range = ulongArrayDescriptor.range,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            range = charArrayDescriptors.range,
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest15(
            range = stringArrayDescriptor.range,
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest15(
            range = byteArrayDescriptor.range,
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest15(
            range = shortArrayDescriptor.range,
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest15(
            range = intArrayDescriptor.range,
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest15(
            range = floatArrayDescriptor.range,
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest15(
            range = longArrayDescriptor.range,
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest15(
            range = doubleArrayDescriptor.range,
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
    }

    @Test
    @IgnoreAndroid
    // Compiler does not defer RangeType correctly
    @IgnoreJvm
    // Compiler does not defer RangeType correctly
    @JsName("fn15a")
    fun `Given fixture is called with a range it returns a Fixture for the derived Type for ubyte`() {
        runTest15(
            range = ubyteArrayDescriptor.range,
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest15(
            range = ushortArrayDescriptor.range,
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest15(
            range = uintArrayDescriptor.range,
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest15(
            range = ulongArrayDescriptor.range,
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            range = charArrayDescriptors.range,
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest16(
            range = stringArrayDescriptor.range,
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest16(
            range = byteArrayDescriptor.range,
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest16(
            range = shortArrayDescriptor.range,
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest16(
            range = intArrayDescriptor.range,
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest16(
            range = floatArrayDescriptor.range,
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest16(
            range = longArrayDescriptor.range,
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest16(
            range = doubleArrayDescriptor.range,
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
    }

    @Test
    @IgnoreAndroid
    // Compiler does not defer RangeType correctly
    @IgnoreJvm
    // Compiler does not defer RangeType correctly
    @JsName("fn16a")
    fun `Given fixture is called with a range and a predicate it returns a Fixture for the derived Type for unsigned`() {
        runTest16(
            range = ubyteArrayDescriptor.range,
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest16(
            range = ushortArrayDescriptor.range,
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest16(
            range = uintArrayDescriptor.range,
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest16(
            range = ulongArrayDescriptor.range,
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            range = charArrayDescriptors.range,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest17(
            range = stringArrayDescriptor.range,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest17(
            range = byteArrayDescriptor.range,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest17(
            range = shortArrayDescriptor.range,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest17(
            range = intArrayDescriptor.range,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest17(
            range = floatArrayDescriptor.range,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest17(
            range = longArrayDescriptor.range,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest17(
            range = doubleArrayDescriptor.range,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
        runTest17(
            range = ubyteArrayDescriptor.range,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest17(
            range = ushortArrayDescriptor.range,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest17(
            range = uintArrayDescriptor.range,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest17(
            range = ulongArrayDescriptor.range,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

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
            range = charArrayDescriptors.range,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest18(
            range = stringArrayDescriptor.range,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest18(
            range = byteArrayDescriptor.range,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest18(
            range = shortArrayDescriptor.range,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest18(
            range = intArrayDescriptor.range,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest18(
            range = floatArrayDescriptor.range,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest18(
            range = longArrayDescriptor.range,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest18(
            range = doubleArrayDescriptor.range,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
        runTest18(
            range = ubyteArrayDescriptor.range,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest18(
            range = ushortArrayDescriptor.range,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest18(
            range = uintArrayDescriptor.range,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest18(
            range = ulongArrayDescriptor.range,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$classId" to generator),
        )

        // When
        val result = getCloseRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            range as ClosedRange<Comparable<Any>>,
            StringQualifier(qualifier),
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
            range = charArrayDescriptors.range,
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest19(
            range = stringArrayDescriptor.range,
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest19(
            range = byteArrayDescriptor.range,
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest19(
            range = shortArrayDescriptor.range,
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest19(
            range = intArrayDescriptor.range,
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest19(
            range = floatArrayDescriptor.range,
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest19(
            range = longArrayDescriptor.range,
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest19(
            range = doubleArrayDescriptor.range,
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
    }

    @Test
    @IgnoreAndroid
    // Compiler does not defer RangeType correctly
    @IgnoreJvm
    // Compiler does not defer RangeType correctly
    @JsName("fn19a")
    fun `Given fixture is called with a range and a qualifier it returns a Fixture for the derived Type for unsigned`() {
        runTest19(
            range = ubyteArrayDescriptor.range,
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest19(
            range = ushortArrayDescriptor.range,
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest19(
            range = uintArrayDescriptor.range,
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest19(
            range = ulongArrayDescriptor.range,
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
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
        resolveClassName(FixtureType::class)

        val fixture = Fixture(
            random,
            mapOf("q:$qualifier:$classId" to generator),
        )

        // When
        val result = getCloseRangeFixtureExtension<RangeType, FixtureType>()(
            fixture,
            range as ClosedRange<Comparable<Any>>,
            StringQualifier(qualifier),
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
            range = charArrayDescriptors.range,
            from = charArrayDescriptors.from,
            to = charArrayDescriptors.to,
            classId = charArrayDescriptors.classId,
            expected = charArrayDescriptors.expected,
        )
        runTest20(
            range = stringArrayDescriptor.range,
            from = stringArrayDescriptor.from,
            to = stringArrayDescriptor.to,
            classId = stringArrayDescriptor.classId,
            expected = stringArrayDescriptor.expected,
        )
        runTest20(
            range = byteArrayDescriptor.range,
            from = byteArrayDescriptor.from,
            to = byteArrayDescriptor.to,
            classId = byteArrayDescriptor.classId,
            expected = byteArrayDescriptor.expected,
        )
        runTest20(
            range = shortArrayDescriptor.range,
            from = shortArrayDescriptor.from,
            to = shortArrayDescriptor.to,
            classId = shortArrayDescriptor.classId,
            expected = shortArrayDescriptor.expected,
        )
        runTest20(
            range = intArrayDescriptor.range,
            from = intArrayDescriptor.from,
            to = intArrayDescriptor.to,
            classId = intArrayDescriptor.classId,
            expected = intArrayDescriptor.expected,
        )
        runTest20(
            range = floatArrayDescriptor.range,
            from = floatArrayDescriptor.from,
            to = floatArrayDescriptor.to,
            classId = floatArrayDescriptor.classId,
            expected = floatArrayDescriptor.expected,
        )
        runTest20(
            range = longArrayDescriptor.range,
            from = longArrayDescriptor.from,
            to = longArrayDescriptor.to,
            classId = longArrayDescriptor.classId,
            expected = longArrayDescriptor.expected,
        )
        runTest20(
            range = doubleArrayDescriptor.range,
            from = doubleArrayDescriptor.from,
            to = doubleArrayDescriptor.to,
            classId = doubleArrayDescriptor.classId,
            expected = doubleArrayDescriptor.expected,
        )
    }

    @Test
    @IgnoreAndroid
    // Compiler does not defer RangeType correctly
    @IgnoreJvm
    // Compiler does not defer RangeType correctly
    @JsName("fn20a")
    fun `Given fixture is called with a range and a qualifier and a predicate it returns a Fixture for the derived Type for unsigned`() {
        runTest20(
            range = ubyteArrayDescriptor.range,
            from = ubyteArrayDescriptor.from,
            to = ubyteArrayDescriptor.to,
            classId = ubyteArrayDescriptor.classId,
            expected = ubyteArrayDescriptor.expected,
        )
        runTest20(
            range = ushortArrayDescriptor.range,
            from = ushortArrayDescriptor.from,
            to = ushortArrayDescriptor.to,
            classId = ushortArrayDescriptor.classId,
            expected = ushortArrayDescriptor.expected,
        )
        runTest20(
            range = uintArrayDescriptor.range,
            from = uintArrayDescriptor.from,
            to = uintArrayDescriptor.to,
            classId = uintArrayDescriptor.classId,
            expected = uintArrayDescriptor.expected,
        )
        runTest20(
            range = ulongArrayDescriptor.range,
            from = ulongArrayDescriptor.from,
            to = ulongArrayDescriptor.to,
            classId = ulongArrayDescriptor.classId,
            expected = ulongArrayDescriptor.expected,
        )
    }
}
