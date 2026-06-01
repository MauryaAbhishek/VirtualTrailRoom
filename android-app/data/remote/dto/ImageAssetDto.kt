package com.virtualtrialroom.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ImageAssetDto(
    @SerializedName("id") val id: String,
    @SerializedName("kind") val kind: String,
    @SerializedName("original_filename") val originalFilename: String,
    @SerializedName("content_type") val contentType: String,
    @SerializedName("size_bytes") val sizeBytes: Long,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("sha256") val sha256: String,
    @SerializedName("created_at") val createdAt: String
)

