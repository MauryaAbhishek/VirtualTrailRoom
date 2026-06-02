package com.virtualtrialroom.app.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.virtualtrialroom.app.ui.components.ErrorState
import com.virtualtrialroom.app.ui.components.LoadingState
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF201017),
                        Color(0xFF5A1833),
                        Color(0xFFFAF8F4)
                    ),
                    endY = 1150f
                )
            )
    ) {
        val compact = maxHeight < 720.dp
        val uploadHeight = if (compact) 300.dp else 390.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GarmentHeader(onBackClick = onBackClick)
            GarmentCanvas(
                height = uploadHeight,
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
            CategoryDock(onClothingSelected = onClothingSelected)
        }
    }
}

@Composable
private fun GarmentHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(48.dp)
                .background(Color.White.copy(alpha = 0.16f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "Garment Studio",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Saree, dress, tops",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.72f),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun GarmentCanvas(
    height: androidx.compose.ui.unit.Dp,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(36.dp),
        shadowElevation = 12.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF8EF),
                            Color(0xFFFFD9E6),
                            Color(0xFFD6F3F1)
                        )
                    )
                )
                .padding(18.dp)
        ) {
            GarmentPreviewShape(modifier = Modifier.align(Alignment.Center))
            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CloudUpload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Upload garment image",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    enabled = enabled,
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(imageVector = Icons.Filled.PhotoLibrary, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Choose From Gallery", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun GarmentPreviewShape(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.70f)
            .height(190.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(0.58f)
                .height(74.dp)
                .clip(RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
                .background(Color(0xFFC2185B).copy(alpha = 0.88f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp, bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(Color(0xFF8A123F).copy(alpha = 0.82f))
        )
        Icon(
            imageVector = Icons.Filled.Style,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(54.dp),
            tint = Color.White.copy(alpha = 0.76f)
        )
    }
}

@Composable
private fun CategoryDock(onClothingSelected: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Checkroom, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    text = "Quick categories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )
            }
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GarmentChip(text = "Sarees", onClick = { onClothingSelected("sarees") })
                GarmentChip(text = "Dresses", onClick = { onClothingSelected("dresses") })
                GarmentChip(text = "Tops", onClick = { onClothingSelected("tops") })
                GarmentChip(text = "Outerwear", onClick = { onClothingSelected("outerwear") })
            }
        }
    }
}

@Composable
private fun GarmentChip(
    text: String,
    onClick: () -> Unit
) {
    FilterChip(
        selected = false,
        onClick = onClick,
        label = {
            Text(text = text, fontWeight = FontWeight.Bold)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Style, contentDescription = null)
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            iconColor = MaterialTheme.colorScheme.primary
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = false,
            borderColor = Color.Transparent
        )
    )
}
