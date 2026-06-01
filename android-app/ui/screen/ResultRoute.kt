package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.virtualtrialroom.app.ui.components.AppScaffold
import com.virtualtrialroom.app.ui.components.InfoPanel
import com.virtualtrialroom.app.ui.components.PrimaryActionButton
import com.virtualtrialroom.app.viewmodel.ResultViewModel

@Composable
fun ResultRoute(
    resultId: String,
    onBackToHomeClick: () -> Unit,
    viewModel: ResultViewModel = hiltViewModel()
) {
    val imageUrl = viewModel.imageUrlFor(resultId)

    AppScaffold(title = "Result") {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Generated try-on image"
            )
            InfoPanel(
                title = "Generated image",
                body = "Result ID: $resultId",
                icon = Icons.Filled.Image
            )
            PrimaryActionButton(
                text = "Back To Home",
                icon = Icons.Filled.Home,
                onClick = onBackToHomeClick
            )
        }
    }
}
