package com.virtualtrialroom.app.util

sealed interface AppError {
    val message: String

    data class Network(override val message: String, val cause: Throwable? = null) : AppError
    data class Validation(override val message: String) : AppError
    data class Unauthorized(override val message: String = "Authentication is required.") : AppError
    data class Storage(override val message: String, val cause: Throwable? = null) : AppError
    data class AiProcessing(override val message: String, val cause: Throwable? = null) : AppError
    data class Unknown(override val message: String, val cause: Throwable? = null) : AppError
}

