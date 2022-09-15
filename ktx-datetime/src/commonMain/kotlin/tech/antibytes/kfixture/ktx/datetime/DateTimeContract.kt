/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import tech.antibytes.kfixture.PublicApi

internal object DateTimeContract {
    interface LocalizedDateTimeGenerator<T : Any> : PublicApi.Generator<T> {
        fun generate(
            instantGenerator: Function0<Instant>? = null,
            timeZoneGenerator: Function0<TimeZone>? = null,
        ): T
    }

    const val MIN_TIME_PERIOD = 0
    const val MIN_TIME_PERIOD_L = 0L
    const val MAX_MONTH = 11
    const val MAX_MINUTES_SECONDS = 59
    const val MAX_NANOSECONDS = 999999999L
    const val MIN_TIME_OFFSET = 0
    const val MAX_HOUR_OFFSET = 18
}
