/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.random.Random
import kotlin.reflect.KClass

@Suppress("UNUSED_PARAMETER")
@PublishedApi
internal fun <T> defaultPredicate(value: T): Boolean = true

/**
 * Contract Container of KFixture
 * @author Matthias Geisler
 */
public interface PublicApi {
    /**
     * Generator of values for specific type.
     * @param T the type which the Generator is referring to.
     * @author Matthias Geisler
     */
    public interface Generator<T : Any> {
        /**
         * Generates a instance of given type.
         * @return a instance of a given type.
         */
        public fun generate(): T
    }

    /**
     * Generator of values for specific type.
     * @param T the type which the Generator is referring to.
     * @param R the type which the Generator will instantiate.
     * @author Matthias Geisler
     */
    public interface FilterableGenerator<T : Any, R : Any> : Generator<R> {
        /**
         * Generates a instance of given type if it matches the given predicate.
         * @param predicate which filters non matching values.
         * @return a instance of a given type.
         */
        public fun generate(predicate: (T?) -> Boolean): R
    }

    /**
     * Generator of values for specific type in a given Range.
     * @param T the type which the Generator is referring to.
     * @param R the type which the Generator will instantiate.
     * @author Matthias Geisler
     */
    public interface RangedGenerator<T, R : Any> : FilterableGenerator<T, R> where T : Any, T : Comparable<T> {

        /**
         * Generates a instance of given type in a given range.
         * @param from the lower boundary of the value.
         * @param to the upper boundary of the value.
         * @param predicate which filters non matching values.
         * @throws IllegalArgumentException if start value is greater than the end value.
         * @return a instance of a given type.
         */
        @Throws(IllegalArgumentException::class)
        public fun generate(
            from: T,
            to: T,
            predicate: (T?) -> Boolean = ::defaultPredicate,
        ): R
    }

    /**
     * Generator of values for specific array types.
     * @param T the type which the Generator is referring to.
     * @author Matthias Geisler
     */
    public interface ArrayGenerator<T : Any> : Generator<T> {
        /**
         * Generates a instance of given array type.
         * @param size a fixed size for the resulting Array.
         * If none size is given it chooses a arbitrary size of in between 1 and 10 items.
         * @return a instance of a given type.
         */
        public fun generate(size: Int): T
    }

    /**
     * Generator of values for specific type.
     * @param T the type which the Generator is referring to.
     * @param R the type which the Generator will instantiate.
     * @author Matthias Geisler
     */
    public interface FilterableArrayGenerator<T : Any, R : Any> : ArrayGenerator<R>, FilterableGenerator<T, R> {
        /**
         * Generates a instance of given type if it matches the given predicate.
         * @param predicate which filters non matching values.
         * @return a instance of a given type.
         */
        public fun generate(size: Int, predicate: (T?) -> Boolean): R
    }

    /**
     * Generator of values for specific array types in a given Range.
     * @param T the type which the Generator is referring to.
     * @param R the type which the Generator will instantiate.
     * @author Matthias Geisler
     */
    public interface RangedArrayGenerator<T, R : Any> :
        RangedGenerator<T, R>,
        ArrayGenerator<R> where T : Any, T : Comparable<T> {
        /**
         * Generates a instance of given type in a given range.
         * @param from the lower boundary of the value (inclusive).
         * @param to the upper boundary of the value (inclusive).
         * @param size a fixed given size for the resulting Array.
         * If none size is given it chooses a arbitrary size of in between 1 and 10 items.
         * @param predicate which filters non matching values.
         * @throws IllegalArgumentException if start value is greater than the end value.
         * @return a instance of a given type.
         */
        @Throws(IllegalArgumentException::class)
        public fun generate(
            from: T,
            to: T,
            size: Int,
            predicate: (T?) -> Boolean = ::defaultPredicate,
        ): R

        /**
         * Generates a instance of given type in a given range for a given Size.
         * @param ranges arbitrary amount of boundaries.
         * @param size a fixed given size for the resulting Array.
         * @param predicate which filters non matching values.
         * @throws IllegalArgumentException if start value is greater than the end value.
         * @return a instance of a given type.
         */
        @Throws(IllegalArgumentException::class)
        public fun generate(
            vararg ranges: ClosedRange<T>,
            size: Int? = null,
            predicate: (T?) -> Boolean = ::defaultPredicate,
        ): R
    }

    /**
     * Indicator if the a numeric type should be resoled as positive or negative Number.
     */
    public enum class Sign {
        POSITIVE,
        NEGATIVE,
    }

    /**
     * Generator of values for specific type in a given Range of signed Numbers.
     * @param T the type which the Generator is referring to.
     * @param R the type which the Generator will instantiate.
     * @author Matthias Geisler
     */
    public interface SignedNumberGenerator<T, R : Any> : RangedGenerator<T, R> where T : Any, T : Comparable<T> {
        /**
         * Generates a instance of given type in a given range.
         * @param sign indicates the if the result is positive or negative.
         * @param predicate which filters non matching values.
         * @return a instance of a given type.
         */
        public fun generate(
            sign: Sign,
            predicate: (T?) -> Boolean = ::defaultPredicate,
        ): R
    }

    /**
     * Generator of values for specific type in a given Range of signed Numbers for Arrays.
     * @param T the type which the Generator is referring to.
     * @param R the type which the Generator will instantiate.
     * @author Matthias Geisler
     */
    public interface SignedNumericArrayGenerator<T, R : Any> :
        RangedArrayGenerator<T, R>,
        SignedNumberGenerator<T, R> where T : Any, T : Comparable<T> {
        /**
         * Generates a instance of given type in a given range.
         * @param sign indicates the if the result is positive or negative.
         * @param size a fixed given size for the resulting Array.
         * @param predicate which filters non matching values.
         * @return a instance of a given type.
         */
        public fun generate(
            sign: Sign,
            size: Int,
            predicate: (T?) -> Boolean = ::defaultPredicate,
        ): R
    }

    /**
     * Factory of a Generator
     * @param T the type which the Generator is referring to.
     * @see Generator
     * @author Matthias Geisler
     */
    public interface GeneratorFactory<T : Any> {
        /**
         * Instantiates a Generator
         * @param random a shared instance of Random.
         * @see Random
         * @return a instance of a Generator
         */
        public fun getInstance(random: Random): Generator<T>
    }

    /**
     * Factory of a Generator which has dependencies on other Generators
     * @param T the type which the Generator is referring to.
     * @see Generator
     * @author Matthias Geisler
     */
    public interface DependentGeneratorFactory<T : Any> {
        /**
         * Instantiates a Generator
         * @param random a shared instance of Random.
         * @param generators a Map which holds already registered Generators.
         * @see Random
         * @return a instance of a Generator
         */
        public fun getInstance(
            random: Random,
            generators: Map<String, Generator<out Any>>,
        ): Generator<T>
    }

    /**
     * Indicator to identify a special flavour of a Generator
     * @see Generator
     * @author Matthias Geisler
     */
    public interface Qualifier {
        /**
         * Unique identifier bound to a generator
         */
        public val value: String
    }

    /**
     * Configuration Container to setup the Fixture Generator.
     * @author Matthias Geisler
     */
    public interface Configuration {
        /**
         * Seed which is used when the Random Generator is initialized
         */
        public var seed: Int

        /**
         * Adds a custom Generator to Fixture Generator.
         * However build in types cannot be overridden.
         * @param T the type which the Generator is referring to.
         * @param clazz a KClass the generator is referring to.
         * @param factory the Factory for the Generator.
         * @param qualifier optional Qualifier which can be to differ between flavours of the same type.
         * @return Configuration the current instance of the Configuration.
         * @see Generator
         * @see GeneratorFactory
         * @see Qualifier
         */
        public fun <T : Any> addGenerator(
            clazz: KClass<T>,
            factory: GeneratorFactory<T>,
            qualifier: Qualifier? = null,
        ): Configuration

        /**
         * Adds a custom Generator to Fixture Generator.
         * However build in types cannot be overridden.
         * @param T the type which the Generator is referring to.
         * @param clazz a KClass the generator is referring to.
         * @param factory the Factory which has dependencies for the Generator.
         * @param qualifier optional Qualifier which can be to differ between flavours of the same type.
         * @return Configuration the current instance of the Configuration.
         * @see Generator
         * @see GeneratorFactory
         * @see Qualifier
         */
        public fun <T : Any> addGenerator(
            clazz: KClass<T>,
            factory: DependentGeneratorFactory<T>,
            qualifier: Qualifier? = null,
        ): Configuration
    }

    /**
     * Fixture Generator.
     *
     * @author Matthias Geisler
     */
    public interface Fixture {
        /**
         * Random Generator which is used to generate atomic types.
         */
        public val random: Random

        /**
         * Map which holds all registers Generators.
         */
        public val generators: Map<String, Generator<out Any>>
    }
}
