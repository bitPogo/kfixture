# Usage

## Getting started
Assuming you are using MavenCentral already, you just add the following into your projects `build.gralde.kts`:
```kotlin
testImplementation("tech.antibytes.kfixture:core:$KFixtureVersion")
```

## Generating Values
You initialise the Generator with `kotlinFixture()` and you may retrieve atomic values via the `fixture` extension:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    @Test
    fun myAwesomeTest() {
        val value: String = fixture.fixture()
        ...
    }
}
```

In opposite to JVM libraries, KFixture cannot generate values for arbitrary types due to the nature of KMP.
This goes especially for Generics like List or Array.
However KFixture has a preset of supported generic Types which can accessed like:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    @Test
    fun myAwesomeTest() {
        val value: List<String> = fixture.listFixture()
        ...
    }
}
```

or via:

```kotlin
class MySpec {
    private val fixture = kotlinFixture()

    @Test
    fun myAwesomeTest() {
        val value: List<String> = fixture.fixture(type = List::class)
        ...
    }
}
```

!!!note
    Nested Generics are not supported which means things like `List<List<String>>` cannot not be derived in the default setting.

## Setting a Seed

KFixture uses Kotlin's `Random` implementation to generate the most basic values.
The default Random Seed value is set to 0, but you can set a custom value:

```kotlin
class MySpec {
    private val fixture = kotlinFixture {
        seed = 42
    }
    ...
}
```

## Build-In types
### Atomics
Following types are directly supported by the `fixture` extension:

| Type           |
| -------------- |
|  Any  |
|  Boolean  |
|  Byte  |
|  Char  |
|  Double  |
|  Float  |
|  Int  |
|  Long  |
|  Short  |
|  Unit  |
|  UByte  |
|  UInt  |
|  ULong  |
|  UShort  |

!!!note
    Chars will be between ` ` (32) and `~` (126).

### AtomicArrays
Following types are directly supported by the `fixture` extension:

| Type           |
| -------------- |
|  BooleanArray  |
|  ByteArray  |
|  CharArray  |
|  DoubleArray  |
|  FloatArray  |
|  IntArray  |
|  LongArray  |
|  ShortArray  |
|  UByteArray  |
|  UIntArray  |
|  ULongArray  |
|  UShortArray  |
|  String  |

!!!note
    It will randomly choose a size between 1 and 10 items.
    Also an item of an CharArray or String will be a Char between ` ` (32) and `~` (126).

### Complex Types
#### Iterables
| Type           | Corresponding Extension |
| -------------- | ----------------------- |
|  Sequence      | sequenceFixture         |
|  Array         | arrayFixture            |
|  List          | listFixture             |
|  MutableList   | mutableListFixture      |
|  Set           | setFixture              |
|  MutableSet    | mutableSetFixture       |
|  Map           | mapFixture              |
|  MutableMap    | mutableMapFixture       |

#### Others
| Type           | Corresponding Extension |
| -------------- | ----------------------- |
|  Pair          | pairFixture             |
|  Triple        | tripleFixture           |
|  Enum          | enumFixture             |
