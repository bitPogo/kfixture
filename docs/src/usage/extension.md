# Extensions
In order to extend KFixture you have 2 options.
You can either simply build on top of the existing Extension Functions or you can create your own Generator.

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
KFixture also let you define your own Generators which might come in handy when used in tandem with for example [KMock](https://github.com/bitPogo/kmock).
You can either define a Generator for an entire Type (unqualified) or you can specialize it with an Qualifier.
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

Unlike with Extension Functions you will not have the Fixture scope, but you can define dependencies.
This allows you to make use of already registered Generators.
Let's refactor the example above while we use the build-in Char Generator:

```kotlin
import tech.antibytes.kfixture.PublicApi.Generator
import tech.antibytes.kfixture.PublicApi.DependentGeneratorFactory
import tech.antibytes.kfixture.PublicApi.RangedGenerator

class MySpec {
    class StringAlphaGenerator(
        private val charGenerator: PublicApi.RangedGenerator<Char, Char>,
        private val random: Random,
    ) : PublicApi.Generator<String> {
        override fun generate(): String {
            val length = random.nextInt(1, 10)
            val chars = CharArray(length) {
                val char = charGenerator.generate(
                    from = 'A',
                    to = 'Z',
                )

                if (random.nextBoolean()) {
                    char + 32
                } else {
                    char
                }
            }

            return chars.concatToString()
        }

        @Suppress("UNCHECKED_CAST")
        companion object : PublicApi.DependentGeneratorFactory<String> {
            override fun getInstance(
                random: Random,
                generators: Map<String, PublicApi.Generator<out Any>>
            ): PublicApi.Generator<String> {
                return StringAlphaGenerator(
                    charGenerator = generators[resolveGeneratorId(Char::class)] as PublicApi.RangedGenerator<Char, Char>,
                    random = random
                )
            }
        }
    }

    private val fixture = kotlinFixture {
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
        ...
    }
}
```

If this still not fits entirely your needs you may overcome this by using auxiliary Fixture.
Go this path only when it is really necessary.

!!!important
    You are not allowed to override Build-In Types. You can only specialize them.

## Type Overview
You may find the definitions of the Interfaces in `PublicApi`.
However the following should provide you with missing information about the build-in types:

### Atomics

| Type           |  Interface |
| -------------- | ---------- |
|  Any           | `Generator<Any>` |
|  Boolean       | `Generator<Boolean>` |
|  Byte          | `SignedNumberGenerator<Byte, Byte>` |
|  Char          | `RangedGenerator<Char, Char>` |
|  Double        | `SignedNumberGenerator<Double, Double>` |
|  Float         | `RangedGenerator<Float, Float>` |
|  Int           | `SignedNumberGenerator<Int, Int>` |
|  Long          | `SignedNumberGenerator<Long, Long>` |
|  Short         | `SignedNumberGenerator<Short, Short>` |
|  Unit          | `Generator<Unit>` |
|  UByte         | `RangedGenerator<UByte, UByte>` |
|  UInt          | `RangedGenerator<UInt, UInt>` |
|  ULong         | `RangedGenerator<ULong, ULong>` |
|  UShort        | `RangedGenerator<UShort, UShort>` |

### AtomicArrays
| Type           |  Interface |
| -------------- | ---------- |
|  BooleanArray  | `ArrayGenerator<Boolean, BooleanArray>` |
|  ByteArray     | `SignedNumericArrayGenerator<Byte, ByteArray>` |
|  CharArray     | `RangedArrayGenerator<Char, CharArray>` |
|  String        | `RangedArrayGenerator<Char, String>` |
|  DoubleArray   | `SignedNumericArrayGenerator<Double, DoubleArray>` |
|  FloatArray    | `RangedArrayGenerator<Float, FloatArray>` |
|  IntArray      | `SignedNumericArrayGenerator<Int, IntArray>` |
|  LongArray     | `SignedNumericArrayGenerator<Long, LongArray>` |
|  ShortArray    | `SignedNumericArrayGenerator<Short, ShortArray>` |
|  UByteArray    | `RangedArrayGenerator<UByte, UByteArray>` |
|  UIntArray     | `RangedArrayGenerator<UInt, UIntArray>` |
|  ULongArray    | `RangedArrayGenerator<ULong, ULongArray>` |
|  UShortArray   | `RangedArrayGenerator<UShort, UShortArray>` |
