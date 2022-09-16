/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import tech.antibytes.kfixture.PublicApi

public interface KtxDateTimeContract {
    public interface LocalizedDateTimeGenerator<T : Any> : PublicApi.Generator<T> {
        public fun generate(
            instantGenerator: Function0<Instant>? = null,
            timeZoneGenerator: Function0<TimeZone>? = null,
        ): T
    }
}

internal object DateTimeContract {
    const val MIN_TIME_STAMP = -9007199254740991L // js // native -31619087596800000L
    const val MAX_TIME_STAMP = 9007199254740991L // js // native 31494784780799999L
    const val MIN_TIME_PERIOD = 0
    const val MIN_TIME_PERIOD_L = 0L
    const val MAX_MONTH = 11
    const val MAX_MINUTES_SECONDS = 59
    const val MAX_NANOSECONDS = 999999999L
    const val MIN_TIME_OFFSET = 0
    const val MAX_HOUR_OFFSET = 18
}
