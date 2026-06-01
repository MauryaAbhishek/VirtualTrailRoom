package com.virtualtrialroom.app.domain.usecase

import com.virtualtrialroom.app.domain.model.UserProfile
import com.virtualtrialroom.app.domain.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<UserProfile?> {
        return authRepository.observeCurrentUser()
    }
}

