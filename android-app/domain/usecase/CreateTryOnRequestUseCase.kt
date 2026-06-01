package com.virtualtrialroom.app.domain.usecase

import com.virtualtrialroom.app.domain.model.ClothingItem
import com.virtualtrialroom.app.domain.model.TryOnRequest
import com.virtualtrialroom.app.domain.model.UserPhoto
import com.virtualtrialroom.app.domain.repository.AuthRepository
import javax.inject.Inject

class CreateTryOnRequestUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(photo: UserPhoto, clothingItem: ClothingItem): TryOnRequest {
        val user = authRepository.requireCurrentUser()
        return TryOnRequest(
            userId = user.id,
            photo = photo,
            clothingItem = clothingItem
        )
    }
}

