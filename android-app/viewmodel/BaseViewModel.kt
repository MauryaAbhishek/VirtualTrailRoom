package com.virtualtrialroom.app.viewmodel

import androidx.lifecycle.ViewModel
import com.virtualtrialroom.app.util.AppError
import com.virtualtrialroom.app.util.AppResult
import com.virtualtrialroom.app.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel : ViewModel() {
    protected fun <T> MutableStateFlow<UiState<T>>.setResult(result: AppResult<T>) {
        value = when (result) {
            is AppResult.Success -> UiState.Success(result.data)
            is AppResult.Failure -> UiState.Error(result.error)
        }
    }

    protected fun unknownError(throwable: Throwable): AppError {
        return AppError.Unknown(
            message = throwable.message ?: "Something went wrong.",
            cause = throwable
        )
    }
}

