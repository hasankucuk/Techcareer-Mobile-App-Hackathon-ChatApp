package com.techcareer.mobileapphackathon.common.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author: Hasan Küçük on 9.10.2021
 */
open class BaseRepository {
    fun <T> request(request: suspend () -> T): Flow<T> = flow { emit(request()) }
}