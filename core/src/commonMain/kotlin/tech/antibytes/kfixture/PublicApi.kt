/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.random.Random
import kotlin.reflect.KClass

/**
 * Contract Container of KFixture
 * @author Matthias Geisler
 */
public interface PublicApi {
    /**
     * Generator of value for specific type.
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
    }

    /**
     * Fixture Generator
     *
     * @author Matthias Geisler
     */
    public interface Fixture {
        /**
         * Random Generator which is used to generate atomic types
         */
        public val random: Random

        /**
         * Map which holds all registers Generators
         */
        public val generators: Map<String, Generator<out Any>>
    }
}
