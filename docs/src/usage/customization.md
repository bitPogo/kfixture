# Customization
Since the default Generators may produce values in undesired range, KFixture has some possibilities to deal with that.

## Ranges
### Atomics
Let's say you need an integer in the range of 12 to 42.
You can set an additional range arguments:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    @Test
    fun myAwesomeTest() {
        val value: Int = fixture.fixture(
            from = 12,
            to = 42,
        )
        ...
    }
}
```

Alternatively you can also use a ClosedRange directly:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    @Test
    fun myAwesomeTest() {
        val value: Int = fixture.fixture(
            range = 12..42,
        )
        ...
    }
}
```

!!!note
    For numbers the default range goes over the entire range of the type.
    Chars however will be chosen between ` ` (32) and `~` (126).

If you want simply to express that you want only positive/negative Number, KFixture allows a short cut for that:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    @Test
    fun myAwesomeTest() {
        val value: Int = fixture.fixture(
            sign = Sign.POSITIVE,
        )
        ...
    }
}
```

!!!note
    Float does not support signing.

### AtomicArrays
AtomicArrays (e.g. ByteArray) have similar capacities to Atomics.
They can be use ranges arguments:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    @Test
    fun myAwesomeTest() {
        val value: IntArray = fixture.fixture(
            from = 12,
            to = 42,
        )
        ...
    }
}
```

or a ClosedRange directly:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    @Test
    fun myAwesomeTest() {
        val value: IntArray = fixture.fixture(
            12..42,
        )
        ...
    }
}
```

or signing:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    @Test
    fun myAwesomeTest() {
        val value: IntArray = fixture.fixture(
            sign = Sign.NEGATIVE,
        )
        ...
    }
}
```


Additionally AtomicArrays can take multiple ranges.
KFixture will randomly select a range per item:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    @Test
    fun myAwesomeTest() {
        val value: IntArray = fixture.fixture(
            12..42,
            102..222,
            143..567
        )
        ...
    }
}
```

## Size

AtomicArrays and build-in Generics (except Tuples and Enums) are also sizeable.
This means they take an additional parameter `size`:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    @Test
    fun myAwesomeTest() {
        val value: IntArray = fixture.fixture(
            size = 23,
        )
        ...
    }
}
```

!!!note
    If you do not set a size it will randomly choose one (between 1 and 10 items).

## Filter

In some cases it might be desired to filter certain values out.
KFixture provides for this also a handle.
Similar to Kotlin's `filter` function you simply provide `fixture` a predicate:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    @Test
    fun myAwesomeTest() {
        val value: IntArray = fixture.fixture(
            from = 12,
            to = 42,
        ) { it != 40 } // 40 will be excluded
        ...
    }
}
```

## Nested Generators

In order to keep Generics agnostic they will not take a concrete parameter like `predicate`.
However all Generic Generators will allow you to inject a customized Generator via `nestedGenerator` or similar:



```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    private inline fun <reified T : Number> positiveNumber(
        qualifier: PublicApi.Qualifier?
    ): T = fixture.fixture(
        sign = PublicApi.Sign.POSITIVE
    )

    @Test
    fun myAwesomeTest() {
        val value: List<Int> = fixture.listFixture(
            nestedGenerator = ::positiveNumber
        )
        ...
    }
}
```

## Selections

### Inline
`fixture` is also capable to pick values from a given Iterable:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()
    private val possibleValues = listOf("a", "b", "c")

    @Test
    fun myAwesomeTest() {
        val value1 = fixture.fixture(possibleValues)
        val value2 = fixture.fixture(0..100)
        ...
    }
}
```

### Selection Generator

If you want to distribute Options over the entire lifetime of `fixture` you can use the `SelectionGenerator`:

```kotlin
class MySpec {
    private val fixture = kotlinFixture {
        useSelector(
            options = listOf("a", "b", "c"),
            qualifier = qualifiedBy("abc")
        )
    }

    @Test
    fun myAwesomeTest() {
        val value: String = fixture.fixture(
            qualifier = qualifiedBy("abc")
        )
        ...
    }
}
```

The selector will pick a value of the provided option list whenever it is referenced.
You also may noticed the usage of a qualifier.
Qualifiers are described in under [Extensions](extension.md#custom-generators), so please take a look there.

## Overview
### Atomics

| Type           |  Range  | Signed  | Filterable |
| -------------- | ------- | ------- | ---------- |
|  Any           |   ❌    |   ❌    |      ❌     |
|  Boolean       |   ❌    |   ❌    |      ❌     |
|  Byte          |   ✅    |   ✅    |      ✅     |
|  Char          |   ✅    |   ❌    |      ✅     |
|  Double        |   ✅    |   ✅    |      ✅     |
|  Float         |   ✅    |   ❌    |      ✅     |
|  Int           |   ✅    |   ✅    |      ✅     |
|  Long          |   ✅    |   ✅    |      ✅     |
|  Short         |   ✅    |   ✅    |      ✅     |
|  Unit          |   ❌    |   ❌    |      ❌     |
|  UByte         |   ✅    |   ❌    |      ✅     |
|  UInt          |   ✅    |   ❌    |      ✅     |
|  ULong         |   ✅    |   ❌    |      ✅     |
|  UShort        |   ✅    |   ❌    |      ✅     |

### AtomicArrays
| Type           |  Range  | Signed  | Filterable |
| -------------- | ------- | ------- | ---------- |
|  BooleanArray  |   ❌    |   ❌    |      ❌     |
|  ByteArray     |   ✅    |   ✅    |      ✅     |
|  CharArray     |   ✅    |   ❌    |      ✅     |
|  String        |   ✅    |   ❌    |      ✅     |
|  DoubleArray   |   ✅    |   ✅    |      ✅     |
|  FloatArray    |   ✅    |   ❌    |      ✅     |
|  IntArray      |   ✅    |   ✅    |      ✅     |
|  LongArray     |   ✅    |   ✅    |      ✅     |
|  ShortArray    |   ✅    |   ✅    |      ✅     |
|  UByteArray    |   ✅    |   ❌    |      ✅     |
|  UIntArray     |   ✅    |   ❌    |      ✅     |
|  ULongArray    |   ✅    |   ❌    |      ✅     |
|  UShortArray   |   ✅    |   ❌    |      ✅     |
