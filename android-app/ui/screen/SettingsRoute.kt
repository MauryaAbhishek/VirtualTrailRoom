package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.virtualtrialroom.app.ui.components.AppScaffold
import com.virtualtrialroom.app.ui.components.InfoPanel

@Composable
fun SettingsRoute(onBackClick: () -> Unit) {
    AppScaffold(
        title = "Settings",
        onBackClick = onBackClick
    ) {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoPanel(
                title = "Privacy",
                body = "User images should be processed through authenticated, encrypted storage.",
                icon = Icons.Filled.Security
            )
            InfoPanel(
                title = "Storage",
                body = "Generated results should remain scoped to the signed-in account.",
                icon = Icons.Filled.Storage
            )
        }
    }
}
