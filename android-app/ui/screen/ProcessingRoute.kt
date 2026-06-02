package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.virtualtrialroom.app.ui.components.ErrorState
import com.virtualtrialroom.app.util.AppError
import com.virtualtrialroom.app.viewmodel.ProcessingViewModel

@Composable
fun ProcessingRoute(
    requestId: String,
    onResultReady: (String) -> Unit,
    viewModel: ProcessingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(requestId) {
        viewModel.pollJob(requestId)
    }

    LaunchedEffect(uiState.outputImageId) {
        val outputImageId = uiState.outputImageId
        if (outputImageId != null) {
            onResultReady(outputImageId)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(MaroonDeep, Maroon, Color(0xFF160006))))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                brush = Brush.horizontalGradient(listOf(Color.Transparent, Gold.copy(alpha = 0.85f), Color.Transparent)),
                start = Offset(0f, size.height * 0.44f),
                end = Offset(size.width, size.height * 0.34f),
                strokeWidth = 5f,
                cap = StrokeCap.Round
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 34.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            Text(
                text = "AI Magic in Progress ✨",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Black
            )
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { if (uiState.outputImageId != null) 1f else 0.72f },
                    modifier = Modifier.size(176.dp),
                    color = Gold,
                    trackColor = Color.White.copy(alpha = 0.14f),
                    strokeWidth = 10.dp
                )
                Text(
                    text = if (uiState.outputImageId != null) "100%" else "72%",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ProgressStep("Analyzing Face", Icons.Filled.PersonSearch, true)
                ProgressStep("Understanding Body Type", Icons.Filled.Psychology, true)
                ProgressStep("Draping Saree", Icons.Filled.Style, true)
                ProgressStep("Rendering Preview", Icons.Filled.Visibility, uiState.outputImageId != null)
            }
            Text(
                text = uiState.statusMessage ?: "This might take a few seconds...",
                style = MaterialTheme.typography.bodyMedium,
                color = CreamText.copy(alpha = 0.74f)
            )
            if (uiState.errorMessage != null) {
                ErrorState(
                    error = AppError.AiProcessing(uiState.errorMessage ?: "Try-on generation failed."),
                    onRetryClick = { viewModel.pollJob(requestId) }
                )
            }
            if (uiState.outputImageId != null) {
                Button(
                    onClick = { onResultReady(uiState.outputImageId ?: return@Button) },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = MaroonDeep)
                ) {
                    Icon(imageVector = Icons.Filled.Image, contentDescription = null)
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = "View Result", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun ProgressStep(
    label: String,
    icon: ImageVector,
    done: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.07f),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = CircleShape, color = if (done) Gold.copy(alpha = 0.20f) else Color.White.copy(alpha = 0.08f)) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (done) Gold else CreamText.copy(alpha = 0.52f)
                )
            }
            Text(modifier = Modifier.weight(1f), text = label, color = Color.White, fontWeight = FontWeight.SemiBold)
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = if (done) Gold else Color.White.copy(alpha = 0.20f)
            )
        }
    }
}
