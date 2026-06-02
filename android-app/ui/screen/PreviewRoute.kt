package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.virtualtrialroom.app.viewmodel.PreviewViewModel

@Composable
fun PreviewRoute(
    photoId: String,
    clothingId: String,
    onBackClick: () -> Unit,
    onJobCreated: (String) -> Unit,
    viewModel: PreviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.jobId) {
        val jobId = uiState.jobId
        if (jobId != null) {
            onJobCreated(jobId)
            viewModel.consumeJobNavigation()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8EF))
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaroonDeep)
            }
            Text(
                modifier = Modifier.weight(1f),
                text = "CREATE NEW LOOK",
                style = MaterialTheme.typography.titleMedium,
                color = MaroonDeep,
                fontWeight = FontWeight.Black
            )
        }
        StepBar()
        CustomerPhotoPreview()
        SelectionCard("Customer Photo", photoId.take(8), Icons.Filled.Person)
        SelectionCard("Selected Saree", clothingId.take(8), Icons.Filled.Style)
        if (uiState.isLoading) {
            LoadingState(message = uiState.statusMessage ?: "Creating try-on job.")
        }
        if (uiState.errorMessage != null) {
            ErrorState(
                error = AppError.Network(uiState.errorMessage ?: ""),
                onRetryClick = { viewModel.submitTryOn(photoId, clothingId) }
            )
        }
        Button(
            onClick = { viewModel.submitTryOn(photoId, clothingId) },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Maroon, contentColor = Color.White)
        ) {
            Text(text = if (uiState.isLoading) "Starting" else "Continue →", fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun StepBar() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        StepBubble("1", "Customer", true)
        StepBubble("2", "Saree", true)
        StepBubble("3", "Generate", false)
    }
}

@Composable
private fun StepBubble(number: String, label: String, active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Surface(shape = CircleShape, color = if (active) Maroon else Color(0xFFE8D8CB)) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                text = number,
                color = if (active) Color.White else MaroonDeep,
                fontWeight = FontWeight.Black
            )
        }
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaroonDeep)
    }
}

@Composable
private fun CustomerPhotoPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = "Customer Photo", color = MaroonDeep, fontWeight = FontWeight.Black)
        Text(text = "Add a clear front-facing photo", color = Color(0xFF7A6570), style = MaterialTheme.typography.bodyMedium)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(210.dp)
                    .clip(CircleShape)
                    .background(Brush.verticalGradient(listOf(Color(0xFFFFEAD6), Color(0xFFF7C9A2)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(96.dp), tint = Maroon.copy(alpha = 0.72f))
            }
            Surface(modifier = Modifier.align(Alignment.BottomCenter), shape = CircleShape, color = Color.White, shadowElevation = 6.dp) {
                Icon(modifier = Modifier.padding(12.dp), imageVector = Icons.Filled.CameraAlt, contentDescription = null, tint = Maroon)
            }
        }
    }
}

@Composable
private fun SelectionCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Surface(shape = RoundedCornerShape(18.dp), color = Color.White.copy(alpha = 0.9f), shadowElevation = 3.dp) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(shape = CircleShape, color = Color(0xFFFFE2E9)) {
                Icon(modifier = Modifier.padding(9.dp), imageVector = icon, contentDescription = null, tint = Maroon)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Black, color = MaroonDeep)
                Text(text = value, color = Color(0xFF7A6570), style = MaterialTheme.typography.bodyMedium)
            }
            Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A))
        }
    }
}
