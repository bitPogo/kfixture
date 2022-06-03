/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import co.touchlab.stately.isolate.IsolateState
import kotlin.random.Random
import kotlin.reflect.KClass

public interface PublicApi {
    public interface Generator<T : Any> {
        public fun generate(): T
    }

    public interface GeneratorFactory<T : Any> {
        public fun getInstance(random: IsolateState<Random>): Generator<T>
    }

    public interface Qualifier {
        public val value: String
    }

    public interface Configuration {
        public var seed: Int

        public fun <T : Any> addGenerator(
            clazz: KClass<T>,
            factory: GeneratorFactory<T>,
            qualifier: Qualifier? = null
        ): Configuration
    }

    public interface Fixture {
        public val random: IsolateState<Random>
        public val generators: Map<String, Generator<out Any>>
    }
}
