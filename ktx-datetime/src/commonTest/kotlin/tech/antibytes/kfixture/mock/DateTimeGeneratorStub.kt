/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.mock

import kotlin.js.JsName
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import tech.antibytes.kfixture.ktx.datetime.KtxDateTimeContract

class DateTimeGeneratorStub<T : Any>(
    @JsName("onGenerate")
    var generate: Function0<T>? = null,
    @JsName("onGenerateWithGenerators")
    var generateWithGenerators: Function2<(Function0<Instant>)?, Function0<TimeZone>?, T>? = null,
) : KtxDateTimeContract.LocalizedDateTimeGenerator<T> {
    override fun generate(): T {
        return generate?.invoke()
            ?: throw RuntimeException("Missing Stub for generate!")
    }

    override fun generate(
        instantGenerator: Function0<Instant>?,
        timeZoneGenerator: Function0<TimeZone>?,
    ): T {
        return generateWithGenerators?.invoke(instantGenerator, timeZoneGenerator)
            ?: throw RuntimeException("Missing Stub for generateWithGenerators!")
    }

    fun clear() {
        generate = null
        generateWithGenerators = null
    }
}
