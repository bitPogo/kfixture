/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:filename")

package tech.antibytes.kfixture

import kotlin.reflect.KClass

// Ensure stable names
private val classNames: MutableMap<KClass<*>, String> = HashMap()

// TODO: since JS does not provide a package prefix, think about to make it more unique, while keeping the current behaviour
@PublishedApi
internal actual fun <T : Any> resolveClassName(clazz: KClass<T>): String {
    if (!classNames.containsKey(clazz)) {
        classNames[clazz] = clazz.simpleName ?: "KClass@${clazz.hashCode()}"
    }

    return classNames[clazz]!!
}
