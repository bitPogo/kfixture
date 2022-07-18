/* ktlint-disable filename */
/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import kotlin.reflect.KClass

@InternalAPI
@PublishedApi
internal expect fun <T : Any> resolveClassName(clazz: KClass<T>): String
