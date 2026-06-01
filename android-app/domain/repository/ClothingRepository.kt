package com.virtualtrialroom.app.domain.repository

import com.virtualtrialroom.app.domain.model.ClothingCategory
import com.virtualtrialroom.app.domain.model.ClothingItem
import com.virtualtrialroom.app.util.AppResult

interface ClothingRepository {
    suspend fun getClothingItems(category: ClothingCategory? = null): AppResult<List<ClothingItem>>
    suspend fun getClothingItem(id: String): AppResult<ClothingItem>
}

