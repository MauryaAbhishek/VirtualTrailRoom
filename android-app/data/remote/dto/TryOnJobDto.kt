package com.virtualtrialroom.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TryOnJobDto(
    @SerializedName("id") val id: String,
    @SerializedName("user_image_id") val userImageId: String,
    @SerializedName("clothing_image_id") val clothingImageId: String,
    @SerializedName("status") val status: String,
    @SerializedName("output_image_id") val outputImageId: String?,
    @SerializedName("error_message") val errorMessage: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

