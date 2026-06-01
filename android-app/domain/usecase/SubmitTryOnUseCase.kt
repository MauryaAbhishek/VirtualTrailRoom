package com.virtualtrialroom.app.domain.usecase

import com.virtualtrialroom.app.domain.model.TryOnRequest
import com.virtualtrialroom.app.domain.model.TryOnResult
import com.virtualtrialroom.app.domain.repository.TryOnRepository
import com.virtualtrialroom.app.util.AppResult
import javax.inject.Inject

class SubmitTryOnUseCase @Inject constructor(
    private val tryOnRepository: TryOnRepository
) {
    suspend operator fun invoke(request: TryOnRequest): AppResult<TryOnResult> {
        return tryOnRepository.submitTryOn(request)
    }
}

