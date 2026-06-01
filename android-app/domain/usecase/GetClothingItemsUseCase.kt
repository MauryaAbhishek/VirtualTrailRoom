package com.virtualtrialroom.app.domain.usecase

import com.virtualtrialroom.app.domain.model.ClothingCategory
import com.virtualtrialroom.app.domain.model.ClothingItem
import com.virtualtrialroom.app.domain.repository.ClothingRepository
import com.virtualtrialroom.app.util.AppResult
import javax.inject.Inject

class GetClothingItemsUseCase @Inject constructor(
    private val clothingRepository: ClothingRepository
) {
    suspend operator fun invoke(category: ClothingCategory? = null): AppResult<List<ClothingItem>> {
        return clothingRepository.getClothingItems(category)
    }
}

