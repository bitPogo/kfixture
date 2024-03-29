/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:filename")

package tech.antibytes.kfixture

import kotlin.native.concurrent.ThreadLocal
import kotlin.reflect.KClass

// Ensure stable names
@ThreadLocal
private val classNames: MutableMap<KClass<*>, String> = HashMap()

@PublishedApi
internal actual fun <T : Any> resolveClassName(clazz: KClass<T>): String {
    if (!classNames.containsKey(clazz)) {
        classNames[clazz] = clazz.qualifiedName ?: "KClass@${clazz.hashCode()}"
    }

    return classNames[clazz]!!
}
