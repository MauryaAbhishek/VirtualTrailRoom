package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.virtualtrialroom.app.ui.components.AppScaffold
import com.virtualtrialroom.app.ui.components.InfoPanel
import com.virtualtrialroom.app.ui.components.PrimaryActionButton
import com.virtualtrialroom.app.ui.components.SecondaryActionButton

@Composable
fun WardrobeRoute(
    onBackClick: () -> Unit,
    onClothingSelected: (String) -> Unit
) {
    AppScaffold(
        title = "Wardrobe",
        onBackClick = onBackClick
    ) {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoPanel(
                title = "Catalog",
                body = "Browse garments by category. If no captured image is attached, selection will return to capture first.",
                icon = Icons.Filled.Checkroom
            )
            PrimaryActionButton(
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
