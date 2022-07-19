/* ktlint-disable filename */
/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.generator.selector

import tech.antibytes.kfixture.PublicApi

public inline fun <reified T : Any> PublicApi.Configuration.useSelector(
    options: List<T>,
    qualifier: PublicApi.Qualifier? = null,
): PublicApi.Configuration = addGenerator(
    T::class,
    SelectorGeneratorFactory(options.toTypedArray()),
    qualifier,
)
