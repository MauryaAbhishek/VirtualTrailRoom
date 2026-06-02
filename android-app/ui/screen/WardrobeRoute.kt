package com.virtualtrialroom.app.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF8F4))
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        GarmentTopBar(onBackClick = onBackClick)
        UploadCard(
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
        TipsCard()
        CategorySection(onClothingSelected = onClothingSelected)
    }
}

@Composable
private fun GarmentTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(shape = CircleShape, color = Color.White, shadowElevation = 4.dp) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF17151A)
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "Garment",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = Color(0xFF17151A)
            )
            Text(
                text = "Upload clothing reference",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF68606A)
            )
        }
    }
}

@Composable
private fun UploadCard(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = Color.White,
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color(0xFFFFF8EF),
                                Color(0xFFFFD9E6),
                                Color(0xFFD6F3F1)
                            )
                        ),
                        shape = RoundedCornerShape(26.dp)
                    )
                    .padding(18.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.74f),
                    shadowElevation = 4.dp
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(32.dp)
                            .size(58.dp),
                        imageVector = Icons.Filled.CloudUpload,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Choose saree or outfit image",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF17151A)
                )
                Text(
                    text = "Use the exact garment photo you want the person to wear.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF68606A)
                )
            }
            Button(
                onClick = onClick,
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(imageVector = Icons.Filled.PhotoLibrary, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Upload From Gallery", fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun TipsCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE8DED4))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                Icon(
                    modifier = Modifier.padding(10.dp),
                    imageVector = Icons.Filled.Checkroom,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "For best output",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Front-facing garment, good lighting, minimal background.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF68606A)
                )
            }
        }
    }
}

@Composable
private fun CategorySection(onClothingSelected: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Quick category",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = Color(0xFF17151A)
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CategoryChip(text = "Sarees", onClick = { onClothingSelected("sarees") })
            CategoryChip(text = "Dresses", onClick = { onClothingSelected("dresses") })
            CategoryChip(text = "Tops", onClick = { onClothingSelected("tops") })
            CategoryChip(text = "Outerwear", onClick = { onClothingSelected("outerwear") })
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    onClick: () -> Unit
) {
    FilterChip(
        selected = false,
        onClick = onClick,
        label = { Text(text = text, fontWeight = FontWeight.Bold) },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Style, contentDescription = null)
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            labelColor = Color(0xFF17151A),
            iconColor = MaterialTheme.colorScheme.primary
        )
    )
}
