/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.random.Random
import tech.antibytes.kfixture.PublicApi

class Fixture(
    override val random: Random,
    override val generators: Map<String, PublicApi.Generator<out Any>>,
) : PublicApi.Fixture
