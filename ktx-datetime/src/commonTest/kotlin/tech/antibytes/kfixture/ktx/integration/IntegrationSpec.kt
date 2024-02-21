/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.integration

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.ktx.datetime.fixture.fixture
import tech.antibytes.kfixture.ktx.datetime.useDateTimeWithOnlyUtc
import tech.antibytes.kfixture.ktx.datetime.useDateTimeWithTimeZones

class IntegrationSpec {
    @Test
    @JsName("fn0")
    fun `It creates a LocalDateTime with only UTC TimeZone`() {
        val fixture = kotlinFixture {
            useDateTimeWithOnlyUtc()
        }

        val dateTime: LocalDateTime = fixture.fixture()

        assertTrue {
            dateTime.toInstant(TimeZone.UTC).toEpochMilliseconds() != 0L
        }
    }

    @Test
    @JsName("fn1")
    fun `It creates a LocalDateTime with more TimeZones`() {
        val fixture = kotlinFixture {
            useDateTimeWithTimeZones()
        }

        val dateTime: LocalDate = fixture.fixture(
            timeZoneGenerator = null,
        )

        assertTrue {
            dateTime.toEpochDays() != 0
        }
    }
}
