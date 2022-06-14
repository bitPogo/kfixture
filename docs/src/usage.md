# Usage

## Getting started
Assuming you are using MavenCentral already as repository, you just add the following into your projects `build.gralde.kts`:
```kotlin
testImplementation("tech.antibytes.kfixture:core:$KFixtureVersion")
```

## Generating Values
While you initialise the Generator with `kotlinFixture()` you retrieve atomic values via the `fixture` extension:

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
However KFixture has a preset of supported generic Types which can accessed via:

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
    Nested Generics are not supported which means things like `List<List<String>>` cannot not be derived.

Also `fixture` is capable to pick values from a given Iterable:

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

While you may have guessed it, KFixture uses Kotlin's `Random` implementation, which needs always a seed.
The default value is set to 0, but you can set a custom value:

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
|  String  |
|  Unit  |
|  UByte  |
|  UInt  |
|  ULong  |
|  UShort  |

!!!note
    String and Chars will be using Chars between ` ` (32) and `~` (126).
    A String will be composed of 1 to 10 chars.

### Atomic Arrays
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

!!!note
    It will randomly choose a size between 1 and 10 items.
    Also an item of an CharArray will be a Char between ` ` (32) and `~` (126).

### Complex Types
### Iterables
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

!!!note
    Except for `pairFixture` and `tripleFixture` you can define the size via `size`.
    If you do not set a value it will randomly choose a size between 1 and 10 items.

### Others
| Type           | Corresponding Extension |
| -------------- | ----------------------- |
|  Pair          | pairFixture             |
|  Triple        | tripleFixture           |

## Customization
In order to customize and extend KFixture you have 2 options.
You can either simply build on top of the existing Extension Functions or you can create your own Generator.

!!!note
    As a rule of thumb. You may use Extension especially when working with Generics or simple Dummies (like Data Classes).
    For special flavoured Atomics have to implement a custom Generator.

### Custom Extensions
Extensions are likely the easiest way to extend KFixture.
It will allow you to access the Random Generator as well as the Type Generators and of course existing Extensions.
For example:

```kotlin
import tech.antibytes.kfixture.PublicApi.Fixture

class MySpec {
    data class Something(
        val value: String,
        val otherValue: Int
    )

    private val fixture = kotlinFixture()

    private fun Fixture.somethingFixture(): Something {
        return Something(
            value = fixture(),
            otherValue = fixture()
        )
    }


    @Test
    fun myAwesomeTest() {
        val value = fixture.somethingFixture()
        ...
    }
}
```

If you want to access the Type Generators you eventually need to derive the corresponding GeneratorId first.
This is possible via `resolveGeneratorId`:

```kotlin
import tech.antibytes.kfixture.PublicApi.Fixture
import tech.antibytes.kfixture.PublicApi.Qualifier

class MySpec {
    data class Something(
        val value: String,
        val otherValue: Int
    )

    private val fixture = kotlinFixture()

    private inline fun <reified T> Fixture.somethingFixture(
        qualifier: Qualifier? = null
    ): Something {
        val id = resolveGeneratorId(
            T::class,
            qualifier
        )

        return Something(
            value = generators[id].generate(),
            otherValue = fixture()
        )
    }


    @Test
    fun myAwesomeTest() {
        val value = fixture.somethingFixture(qualifiedBy("me"))
        ...
    }
}
```

### Custom Generators
While Extension Function possibly not suite all use cases KFixture also let you define your own Type Generators.
You can either define a Generator for entire Type (unqualified) or you can specialize it with an Qualifier.
Both cases will make it available to the existing Extension Functions.
In order to hook them in you can do the following:

```kotlin
import tech.antibytes.kfixture.PublicApi.Generator
import tech.antibytes.kfixture.PublicApi.GeneratorFactory

class MySpec {
    class MyAwesomeTypeGenerator(
        private val random: Random
    ): Generator<MyAwesomeType> {
        override fun generate(): MyAwesomeType {
            ...
        }

        companion object : GeneratorFactory<MyAwesomeType> {
            override fun getInstance(random: Random): Generator<MyAwesomeType> = MyAwesomeTypeGenerator(random)
        }
    }

    class StringAlphaGenerator(
        private val random: Random
    ) : Generator<String> {
        override fun generate(): String {
            val length = random.nextInt(1, 10)
            val chars = ByteArray(length)

            for (idx in 0 until length) {
                val char = random.nextInt(65, 91)
                chars[idx] = if (random.nextBoolean()) {
                    (char + 32).toByte()
                } else {
                    char.toByte()
                }
            }

            return chars.decodeToString()
        }

        companion object : GeneratorFactory<String> {
            override fun getInstance(random: Random): Generator<String> = StringAlphaGenerator(random)
        }
    }

    private val fixture = kotlinFixture {
        // unqualified
        addGenerator(
            clazz = MyAwesomeType::class,
            factory = MyAwesomeTypeGenerator
        )
        // qualified
        addGenerator(
            clazz = String::class,
            factory = StringAlphaGenerator,
            qualifier = qualifiedBy("special"),
        )
    }

    @Test
    fun myAwesomeTest() {
        val value: String = fixture.fixture(
            qualifier = qualifiedBy("special")
        )
        val awesomeValue: MyAwesomeType = fixture.fixture()
        ...
    }
}
```

In opposite to Extension Functions you will not have the the Fixture Scope at your disposal since Generators are initialized before it.
However to overcome this you can always have an auxiliary Fixture, but this is not recommended.

!!!important
    You are not allowed to override Build-In Types. You can only specialize them.
