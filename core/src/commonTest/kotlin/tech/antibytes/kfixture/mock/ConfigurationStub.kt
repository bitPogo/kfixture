/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.reflect.KClass
import tech.antibytes.kfixture.PublicApi

class ConfigurationStub(
    var addGenerator: ((KClass<*>, PublicApi.GeneratorFactory<*>, PublicApi.Qualifier?) -> Unit)? = null,
) : PublicApi.Configuration {
    override var seed: Int = 23

    override fun <T : Any> addGenerator(
        clazz: KClass<out T>,
        factory: PublicApi.GeneratorFactory<out T>,
        qualifier: PublicApi.Qualifier?,
    ): PublicApi.Configuration {
        addGenerator?.invoke(clazz as KClass<*>, factory as PublicApi.GeneratorFactory<*>, qualifier)
            ?: throw IllegalArgumentException("Missing SideEffect addGenerator")

        return this
    }

    override fun <T : Any> addGenerator(
        clazz: KClass<out T>,
        factory: PublicApi.DependentGeneratorFactory<out T>,
        qualifier: PublicApi.Qualifier?,
    ): PublicApi.Configuration {
        TODO("Not yet implemented")
    }
}
