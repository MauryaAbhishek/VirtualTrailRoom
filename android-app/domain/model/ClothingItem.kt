package com.virtualtrialroom.app.domain.model

data class ClothingItem(
    val id: String,
    val name: String,
    val category: ClothingCategory,
    val imageUrl: String,
    val brandName: String,
    val availableSizes: List<String>
)

