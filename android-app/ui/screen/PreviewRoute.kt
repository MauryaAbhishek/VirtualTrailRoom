package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.virtualtrialroom.app.ui.components.AppScaffold
import com.virtualtrialroom.app.ui.components.ErrorState
import com.virtualtrialroom.app.ui.components.LoadingState
import com.virtualtrialroom.app.ui.components.PrimaryActionButton
import com.virtualtrialroom.app.util.AppError
import com.virtualtrialroom.app.viewmodel.PreviewViewModel

@Composable
fun PreviewRoute(
    photoId: String,
    clothingId: String,
    onBackClick: () -> Unit,
    onJobCreated: (String) -> Unit,
    viewModel: PreviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.jobId) {
        val jobId = uiState.jobId
        if (jobId != null) {
            onJobCreated(jobId)
            viewModel.consumeJobNavigation()
        }
    }

    AppScaffold(
        title = "Generate",
        onBackClick = onBackClick
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GenerationStage()
            SelectionRow(photoId = photoId, clothingId = clothingId)
            if (uiState.isLoading) {
                LoadingState(
                    message = uiState.statusMessage ?: "Creating try-on job."
                )
            }
            if (uiState.errorMessage != null) {
                ErrorState(
                    error = AppError.Network(uiState.errorMessage ?: ""),
                    onRetryClick = { viewModel.submitTryOn(photoId, clothingId) }
                )
            }
            PrimaryActionButton(
                text = if (uiState.isLoading) "Starting" else "Generate Try-On",
                icon = Icons.Filled.AutoAwesome,
                enabled = !uiState.isLoading,
                onClick = { viewModel.submitTryOn(photoId, clothingId) }
            )
        }
    }
}

@Composable
private fun GenerationStage() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ImageSearch,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Ready to Create",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Person + garment selected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ReadyChip(text = "Photo", modifier = Modifier.weight(1f))
                ReadyChip(text = "Garment", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SelectionRow(
    photoId: String,
    clothingId: String
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        SelectionTile(
            title = "Person",
            value = photoId.take(8),
            icon = Icons.Filled.Person,
            modifier = Modifier.weight(1f)
        )
        SelectionTile(
            title = "Garment",
            value = clothingId.take(8),
            icon = Icons.Filled.Style,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SelectionTile(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(text = value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ReadyChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(100.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null)
            Text(text = text, fontWeight = FontWeight.SemiBold)
        }
    }
}
