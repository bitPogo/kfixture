/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.kfixture

@Repeatable
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class InternalAPI(val message: String = "Only for internal usage.")
