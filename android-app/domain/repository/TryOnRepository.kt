package com.virtualtrialroom.app.domain.repository

import com.virtualtrialroom.app.domain.model.TryOnRequest
import com.virtualtrialroom.app.domain.model.TryOnResult
import com.virtualtrialroom.app.util.AppResult
import kotlinx.coroutines.flow.Flow

interface TryOnRepository {
    suspend fun submitTryOn(request: TryOnRequest): AppResult<TryOnResult>
    fun observeTryOnResult(resultId: String): Flow<AppResult<TryOnResult>>
    suspend fun getSavedResults(userId: String): AppResult<List<TryOnResult>>
}

