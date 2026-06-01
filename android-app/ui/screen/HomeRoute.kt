package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.virtualtrialroom.app.ui.components.AppScaffold
import com.virtualtrialroom.app.ui.components.InfoPanel
import com.virtualtrialroom.app.ui.components.PrimaryActionButton
import com.virtualtrialroom.app.ui.components.SecondaryActionButton

@Composable
fun HomeRoute(
    onCaptureClick: () -> Unit,
    onWardrobeClick: () -> Unit,
    onSavedResultsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    AppScaffold(title = "Virtual Trial Room") {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoPanel(
                title = "Ready for a try-on",
                body = "Capture a clear front-facing image, then pair it with a clothing item.",
                icon = Icons.Filled.CameraAlt
            )
            Spacer(modifier = Modifier.height(4.dp))
            PrimaryActionButton(
                text = "Capture Image",
                icon = Icons.Filled.CameraAlt,
                onClick = onCaptureClick
            )
            SecondaryActionButton(
                text = "Choose Clothing",
                icon = Icons.Filled.Checkroom,
                onClick = onWardrobeClick
            )
            SecondaryActionButton(
                text = "Saved Results",
                icon = Icons.Filled.Collections,
                onClick = onSavedResultsClick
            )
            SecondaryActionButton(
                text = "Settings",
                icon = Icons.Filled.Settings,
                onClick = onSettingsClick
            )
        }
    }
}
