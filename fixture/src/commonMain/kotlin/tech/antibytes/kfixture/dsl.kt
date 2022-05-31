/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

fun kotlinFixture(
    configurator: PublicApi.Configuration.() -> Unit = {}
): PublicApi.Fixture {
    val configuration = Configuration()

    configurator.invoke(configuration)

    return configuration.build()
}
