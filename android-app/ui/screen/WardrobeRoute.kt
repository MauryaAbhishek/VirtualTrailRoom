package com.virtualtrialroom.app.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
        title = "Wardrobe",
        onBackClick = onBackClick
    ) {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoPanel(
                title = "Garment",
                body = "Upload the clothing photo the user should wear, such as a saree, dress, or top.",
                icon = Icons.Filled.Checkroom
            )
            PrimaryActionButton(
                text = "Upload Garment Photo",
                icon = Icons.Filled.PhotoLibrary,
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
            SecondaryActionButton(
                text = "Tops",
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                onClick = { onClothingSelected("tops") }
            )
            SecondaryActionButton(
                text = "Dresses",
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                onClick = { onClothingSelected("dresses") }
            )
            SecondaryActionButton(
                text = "Outerwear",
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                onClick = { onClothingSelected("outerwear") }
            )
        }
    }
}
