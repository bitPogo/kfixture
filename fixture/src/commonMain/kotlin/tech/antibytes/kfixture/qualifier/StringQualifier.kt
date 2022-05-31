/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.qualifier

import tech.antibytes.kfixture.FixtureContract.Companion.QUALIFIER_PREFIX
import tech.antibytes.kfixture.PublicApi

internal class StringQualifier(
    private val _value: String
) : PublicApi.Qualifier {
    override val value: String
        get() = "${QUALIFIER_PREFIX}$_value"
}
