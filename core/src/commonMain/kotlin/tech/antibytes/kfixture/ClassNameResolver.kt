/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:filename")

package tech.antibytes.kfixture

import kotlin.reflect.KClass

@InternalAPI
@PublishedApi
internal expect fun <T : Any> resolveClassName(clazz: KClass<T>): String
