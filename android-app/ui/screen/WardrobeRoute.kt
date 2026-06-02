package com.virtualtrialroom.app.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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

private data class SareeProduct(
    val id: String,
    val name: String,
    val price: String,
    val color: Color,
    val accent: Color
)

private val sarees = listOf(
    SareeProduct("red-banarasi", "Red Banarasi Silk", "₹4,599", Color(0xFFB91C1C), Color(0xFFFFC15A)),
    SareeProduct("peach-organza", "Peach Organza", "₹3,899", Color(0xFFF2A477), Color(0xFFFFE1C7)),
    SareeProduct("royal-kanjivaram", "Royal Kanjivaram", "₹5,999", Color(0xFFC026D3), Color(0xFFFFC15A)),
    SareeProduct("wine-zari", "Wine Zari Weave", "₹4,299", Color(0xFF7F1233), Color(0xFFE6A23C)),
    SareeProduct("emerald-silk", "Emerald Silk", "₹4,899", Color(0xFF047857), Color(0xFFFFC15A)),
    SareeProduct("gold-tissue", "Gold Tissue Saree", "₹6,499", Color(0xFFB7791F), Color(0xFFFFE8A3))
)

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
            .background(Color(0xFFFFF8EF))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CatalogTopBar(onBackClick = onBackClick)
        SearchBar()
        CategoryChips(onClothingSelected)
        UploadStrip(
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sarees) { product ->
                SareeProductCard(
                    product = product,
                    onClick = { onClothingSelected(product.id) }
                )
            }
        }
    }
}

@Composable
private fun CatalogTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaroonDeep)
        }
        Text(
            text = "SELECT SAREE",
            style = MaterialTheme.typography.titleMedium,
            color = MaroonDeep,
            fontWeight = FontWeight.Black
        )
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Filled.FilterList, contentDescription = "Filter", tint = MaroonDeep)
        }
    }
}

@Composable
private fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        singleLine = true,
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = Color(0xFF7A6570))
        },
        placeholder = {
            Text(text = "Search sarees...", color = Color(0xFF9A848C))
        },
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.78f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.78f),
            focusedBorderColor = Color(0xFFE7D6C8),
            unfocusedBorderColor = Color(0xFFE7D6C8)
        )
    )
}

@Composable
private fun CategoryChips(onClothingSelected: (String) -> Unit) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CatalogChip("All", true) { onClothingSelected("sarees") }
        CatalogChip("Wedding", false) { onClothingSelected("wedding") }
        CatalogChip("Festive", false) { onClothingSelected("festive") }
        CatalogChip("Silk", false) { onClothingSelected("silk") }
        CatalogChip("Party", false) { onClothingSelected("party") }
    }
}

@Composable
private fun CatalogChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text = text, fontWeight = FontWeight.SemiBold) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Maroon,
            selectedLabelColor = Color.White,
            containerColor = Color.White.copy(alpha = 0.74f),
            labelColor = MaroonDeep
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = Color.Transparent,
            selectedBorderColor = Color.Transparent
        )
    )
}

@Composable
private fun UploadStrip(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.82f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(shape = CircleShape, color = Color(0xFFFFE2E9)) {
                Icon(
                    modifier = Modifier.padding(10.dp),
                    imageVector = Icons.Filled.CloudUpload,
                    contentDescription = null,
                    tint = Maroon
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Have your own saree?", color = MaroonDeep, fontWeight = FontWeight.Black)
                Text(text = "Upload exact garment image", color = Color(0xFF8D737A), style = MaterialTheme.typography.bodySmall)
            }
            Button(
                onClick = onClick,
                enabled = enabled,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Maroon)
            ) {
                Icon(imageVector = Icons.Filled.PhotoLibrary, contentDescription = null)
            }
        }
    }
}

@Composable
private fun SareeProductCard(
    product: SareeProduct,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 5.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.88f)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFFFEAD6), product.accent.copy(alpha = 0.45f), product.color)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                SareeIllustration(product.color, product.accent)
            }
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = product.price, color = MaroonDeep, fontWeight = FontWeight.Black)
                    Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = null, tint = Maroon)
                }
                Text(text = product.name, color = Color(0xFF51323A), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SareeIllustration(
    color: Color,
    accent: Color
) {
    Box(
        modifier = Modifier.size(width = 92.dp, height = 138.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFFEBC7A8))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(74.dp)
                .height(105.dp)
                .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp, bottomStart = 14.dp, bottomEnd = 14.dp))
                .background(color)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(36.dp)
                .height(122.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(accent.copy(alpha = 0.88f))
        )
        Icon(
            imageVector = Icons.Filled.Style,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.72f)
        )
    }
}
