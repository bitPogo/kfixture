/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture.ktx.datetime

@JsModule("@js-joda/timezone")
@JsNonModule
external object JsJodaTimeZoneModule

@Suppress("unused")
private val jsJodaTz = JsJodaTimeZoneModule
