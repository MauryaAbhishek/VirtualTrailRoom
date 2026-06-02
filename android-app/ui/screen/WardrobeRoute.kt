package com.virtualtrialroom.app.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.PhotoLibrary
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.virtualtrialroom.app.ui.components.AppScaffold
import com.virtualtrialroom.app.ui.components.ErrorState
import com.virtualtrialroom.app.ui.components.InfoPanel
import com.virtualtrialroom.app.ui.components.LoadingState
import com.virtualtrialroom.app.ui.components.PrimaryActionButton
import com.virtualtrialroom.app.ui.components.SecondaryActionButton
import com.virtualtrialroom.app.util.AppError
import com.virtualtrialroom.app.viewmodel.WardrobeViewModel

@Composable
fun WardrobeRoute(
    onBackClick: () -> Unit,
    onClothingSelected: (String) -> Unit,
    viewModel: WardrobeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val garmentPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.importGarment(uri)
            }
        }
    )

    LaunchedEffect(uiState.jobId) {
        val importedGarmentId = uiState.jobId
        if (importedGarmentId != null) {
            onClothingSelected(importedGarmentId)
            viewModel.consumeImportedGarment()
        }
    }

    AppScaffold(
        title = "Garment",
        onBackClick = onBackClick
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            UploadStudioPanel(
                enabled = !uiState.isLoading,
                onClick = { garmentPicker.launch("image/*") }
            )
            if (uiState.isLoading) {
                LoadingState(message = uiState.statusMessage ?: "Importing garment image.")
            }
            if (uiState.errorMessage != null) {
                ErrorState(
                    error = AppError.Validation(uiState.errorMessage ?: "Unable to import garment image."),
                    onRetryClick = { garmentPicker.launch("image/*") }
                )
            }
            InfoPanel(
                title = "Best result",
                body = "Use a front-facing garment image with clear fabric and minimal background.",
                icon = Icons.Filled.Checkroom
            )
            Text(
                text = "Quick categories",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            CategoryButton(text = "Sarees", onClick = { onClothingSelected("sarees") })
            CategoryButton(text = "Dresses", onClick = { onClothingSelected("dresses") })
            CategoryButton(text = "Tops", onClick = { onClothingSelected("tops") })
            CategoryButton(text = "Outerwear", onClick = { onClothingSelected("outerwear") })
        }
    }
}

@Composable
private fun UploadStudioPanel(
    enabled: Boolean,
    onClick: () -> Unit
) {
    BoxWithConstraints {
        val imageHeight = if (maxHeight < 720.dp) 132.dp else 180.dp
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CloudUpload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Upload Saree / Outfit",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Photo from gallery",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                PrimaryActionButton(
                    text = "Choose Garment Image",
                    icon = Icons.Filled.PhotoLibrary,
                    enabled = enabled,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
private fun CategoryButton(
    text: String,
    onClick: () -> Unit
) {
    SecondaryActionButton(
        text = text,
        icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        onClick = onClick
    )
}
