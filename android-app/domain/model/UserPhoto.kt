package com.virtualtrialroom.app.domain.model

data class UserPhoto(
    val id: String,
    val localUri: String,
    val source: ImageSource,
    val width: Int,
    val height: Int,
    val capturedAtEpochMillis: Long
)

