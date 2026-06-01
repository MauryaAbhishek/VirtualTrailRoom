package com.virtualtrialroom.app.domain.repository

import com.virtualtrialroom.app.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeCurrentUser(): Flow<UserProfile?>
    suspend fun requireCurrentUser(): UserProfile
}

