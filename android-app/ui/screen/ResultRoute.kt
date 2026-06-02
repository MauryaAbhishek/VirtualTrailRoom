package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.virtualtrialroom.app.ui.components.ErrorState
import com.virtualtrialroom.app.ui.components.LoadingState
import com.virtualtrialroom.app.util.AppError
import com.virtualtrialroom.app.viewmodel.ResultViewModel

@Composable
fun ResultRoute(
    resultId: String,
    onBeforeAfterClick: () -> Unit,
    onShareCustomerClick: () -> Unit,
    onBackToHomeClick: () -> Unit,
    viewModel: ResultViewModel = hiltViewModel()
) {
    val imageUrl = viewModel.imageUrlFor(resultId)
    var retryKey by remember(resultId) { mutableIntStateOf(0) }
    var isImageLoading by remember(resultId, retryKey) { mutableStateOf(true) }
    var imageErrorMessage by remember(resultId, retryKey) { mutableStateOf<String?>(null) }
    val displayUrl = remember(imageUrl, retryKey) { "$imageUrl?reload=$retryKey" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF100006))
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToHomeClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                modifier = Modifier.weight(1f),
                text = "YOUR LOOK IS READY! 😍",
                color = Color.White,
                fontWeight = FontWeight.Black
            )
            IconButton(onClick = {}) {
                Icon(Icons.Filled.FavoriteBorder, contentDescription = "Favorite", tint = Color.White)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.72f)
                .clip(RoundedCornerShape(28.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFFFFEAD6), Maroon))),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = displayUrl,
                contentDescription = "Generated try-on image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                onLoading = {
                    isImageLoading = true
                    imageErrorMessage = null
                },
                onSuccess = {
                    isImageLoading = false
                    imageErrorMessage = null
                },
                onError = { state ->
                    isImageLoading = false
                    val reason = state.result.throwable.message
                        ?: state.result.throwable::class.simpleName
                        ?: "Unknown image loading error"
                    imageErrorMessage = "Generated image could not be loaded. $reason"
                }
            )
            Surface(modifier = Modifier.align(Alignment.TopEnd).padding(12.dp), color = Color.White.copy(alpha = 0.86f), shape = RoundedCornerShape(100.dp)) {
                Text(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), text = "HD", color = MaroonDeep, fontWeight = FontWeight.Black)
            }
            if (isImageLoading) {
                LoadingState(message = "Loading generated image")
            }
        }
        if (imageErrorMessage != null) {
            ErrorState(
                error = AppError.Network(imageErrorMessage ?: "Generated image could not be loaded."),
                onRetryClick = { retryKey += 1 }
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ResultAction("Share", Icons.Filled.Share) {
                onShareCustomerClick()
            }
            ResultAction("Compare", Icons.Filled.Download) { onBeforeAfterClick() }
            ResultAction("Try Another", Icons.Filled.Refresh) { retryKey += 1 }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ResultAction(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(onClick = onClick, color = Color.Transparent) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(shape = CircleShape, color = Color.White) {
                Icon(modifier = Modifier.padding(14.dp), imageVector = icon, contentDescription = label, tint = MaroonDeep)
            }
            Text(text = label, color = Color.White, style = MaterialTheme.typography.labelMedium)
        }
    }
}
