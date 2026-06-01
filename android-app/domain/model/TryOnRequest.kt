package com.virtualtrialroom.app.domain.model

data class TryOnRequest(
    val userId: String,
    val photo: UserPhoto,
    val clothingItem: ClothingItem
)

