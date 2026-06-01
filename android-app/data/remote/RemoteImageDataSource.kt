package com.virtualtrialroom.app.data.remote

import android.net.Uri
import com.virtualtrialroom.app.BuildConfig
import com.virtualtrialroom.app.data.remote.dto.ImageAssetDto
import com.virtualtrialroom.app.data.remote.dto.TryOnJobCreateRequestDto
import com.virtualtrialroom.app.data.remote.dto.TryOnJobDto
import java.io.File
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class RemoteImageDataSource @Inject constructor(
    private val api: VirtualTrialRoomApi
) {
    suspend fun uploadLocalImage(
        localUri: String,
        kind: RemoteImageKind
    ): ImageAssetDto {
        val file = requireLocalFile(localUri)
        val mediaType = contentTypeFor(file).toMediaType()
        val requestBody = file.asRequestBody(mediaType)
        val multipart = MultipartBody.Part.createFormData(
            name = "file",
            filename = file.name,
            body = requestBody
        )
        return api.uploadImage(kind = kind.apiValue, file = multipart)
    }

    suspend fun createTryOnJob(
        userImageId: String,
        clothingImageId: String
    ): TryOnJobDto {
        return api.createTryOnJob(
            TryOnJobCreateRequestDto(
                userImageId = userImageId,
                clothingImageId = clothingImageId
            )
        )
    }

    suspend fun getTryOnJob(jobId: String): TryOnJobDto {
        return api.getTryOnJob(jobId)
    }

    fun imageContentUrl(imageId: String): String {
        return BuildConfig.API_BASE_URL + "api/v1/uploads/images/$imageId/content"
    }

    private fun requireLocalFile(localUri: String): File {
        val uri = Uri.parse(localUri)
        require(uri.scheme == "file") {
            "Only app-local file URIs are supported for upload."
        }
        return File(requireNotNull(uri.path) { "File URI path is missing." }).also {
            require(it.exists()) { "Upload file does not exist." }
            require(it.length() > 0L) { "Upload file is empty." }
        }
    }

    private fun contentTypeFor(file: File): String {
        return when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            else -> "application/octet-stream"
        }
    }
}

enum class RemoteImageKind(val apiValue: String) {
    USER("user"),
    CLOTHING("clothing")
}
