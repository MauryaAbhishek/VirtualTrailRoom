package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.virtualtrialroom.app.ui.components.AppScaffold
import com.virtualtrialroom.app.ui.components.InfoPanel

@Composable
fun SavedResultsRoute(onBackClick: () -> Unit) {
    AppScaffold(
        title = "Saved Results",
        onBackClick = onBackClick
    ) {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoPanel(
                title = "No saved results",
                body = "Generated try-on images can be saved to this collection.",
                icon = Icons.Filled.Collections
            )
        }
    }
}
