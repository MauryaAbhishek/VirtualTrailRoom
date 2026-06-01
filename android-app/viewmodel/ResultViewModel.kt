package com.virtualtrialroom.app.viewmodel

import com.virtualtrialroom.app.data.remote.RemoteImageDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val remoteImageDataSource: RemoteImageDataSource
) : BaseViewModel() {
    fun imageUrlFor(outputImageId: String): String {
        return remoteImageDataSource.imageContentUrl(outputImageId)
    }
}
