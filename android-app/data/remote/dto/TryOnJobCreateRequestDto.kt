package com.virtualtrialroom.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TryOnJobCreateRequestDto(
    @SerializedName("user_image_id") val userImageId: String,
    @SerializedName("clothing_image_id") val clothingImageId: String
)

