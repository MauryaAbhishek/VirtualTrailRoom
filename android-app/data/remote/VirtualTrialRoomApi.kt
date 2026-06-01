package com.virtualtrialroom.app.data.remote

import com.virtualtrialroom.app.data.remote.dto.ImageAssetDto
import com.virtualtrialroom.app.data.remote.dto.TryOnJobCreateRequestDto
import com.virtualtrialroom.app.data.remote.dto.TryOnJobDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface VirtualTrialRoomApi {
    @Multipart
    @POST("api/v1/uploads/images")
    suspend fun uploadImage(
        @Query("kind") kind: String,
        @Part file: MultipartBody.Part
    ): ImageAssetDto

    @GET("api/v1/uploads/images/{imageId}")
    suspend fun getImage(
        @Path("imageId") imageId: String
    ): ImageAssetDto

    @POST("api/v1/try-on/jobs")
    suspend fun createTryOnJob(
        @Body request: TryOnJobCreateRequestDto
    ): TryOnJobDto

    @GET("api/v1/try-on/jobs/{jobId}")
    suspend fun getTryOnJob(
        @Path("jobId") jobId: String
    ): TryOnJobDto
}

