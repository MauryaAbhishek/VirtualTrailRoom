package com.virtualtrialroom.app.domain.model

data class TryOnResult(
    val id: String,
    val requestId: String,
    val outputImageUrl: String,
    val status: ProcessingStatus,
    val createdAtEpochMillis: Long
)

