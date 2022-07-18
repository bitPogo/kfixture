/* ktlint-disable filename */
/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

private val classNames: MutableMap<KClass<*>, String> = ConcurrentHashMap()

@PublishedApi
internal actual fun <T : Any> resolveClassName(clazz: KClass<T>): String {
    if (!classNames.containsKey(clazz)) {
        classNames[clazz] = clazz.java.name
    }

    return classNames[clazz]!!
}
