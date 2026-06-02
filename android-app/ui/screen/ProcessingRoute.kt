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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
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
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF21000D),
                        Color(0xFF410118),
                        Color(0xFF2A0010),
                        Color(0xFF160006)
                    )
                )
            )
    ) {
        MagicWaveBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 26.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "AI Magic in Progress ✨",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { if (uiState.outputImageId != null) 1f else 0.72f },
                    modifier = Modifier.size(162.dp),
                    color = Color(0xFFFFB75E),
                    trackColor = Color.White.copy(alpha = 0.16f),
                    strokeWidth = 8.dp
                )
                Text(
                    text = if (uiState.outputImageId != null) "100%" else "72%",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                ProgressStep("Analyzing Face", Icons.Filled.PersonSearch, true)
                ProgressStep("Understanding Body Type", Icons.Filled.Psychology, true)
                ProgressStep("Draping Saree", Icons.Filled.Style, true)
                ProgressStep("Rendering Preview", Icons.Filled.Visibility, uiState.outputImageId != null)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = uiState.statusMessage ?: "This might take a few seconds...",
                style = MaterialTheme.typography.bodySmall,
                color = CreamText.copy(alpha = 0.70f)
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
private fun MagicWaveBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFFF6388).copy(alpha = 0.24f), Color.Transparent),
                center = Offset(size.width * 0.50f, size.height * 0.27f),
                radius = size.width * 0.58f
            ),
            radius = size.width * 0.58f,
            center = Offset(size.width * 0.50f, size.height * 0.27f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Gold.copy(alpha = 0.18f), Color.Transparent),
                center = Offset(size.width * 0.82f, size.height * 0.42f),
                radius = size.width * 0.48f
            ),
            radius = size.width * 0.48f,
            center = Offset(size.width * 0.82f, size.height * 0.42f)
        )

        val mainWave = Path().apply {
            moveTo(-60f, size.height * 0.47f)
            cubicTo(
                size.width * 0.18f, size.height * 0.43f,
                size.width * 0.31f, size.height * 0.52f,
                size.width * 0.47f, size.height * 0.45f
            )
            cubicTo(
                size.width * 0.66f, size.height * 0.37f,
                size.width * 0.78f, size.height * 0.30f,
                size.width + 70f, size.height * 0.40f
            )
        }
        val lowerWave = Path().apply {
            moveTo(-70f, size.height * 0.52f)
            cubicTo(
                size.width * 0.18f, size.height * 0.48f,
                size.width * 0.36f, size.height * 0.55f,
                size.width * 0.58f, size.height * 0.48f
            )
            cubicTo(
                size.width * 0.76f, size.height * 0.43f,
                size.width * 0.90f, size.height * 0.44f,
                size.width + 70f, size.height * 0.34f
            )
        }
        drawPath(
            path = mainWave,
            color = Color(0xFFFF6A8F).copy(alpha = 0.10f),
            style = Stroke(width = 34f, cap = StrokeCap.Round)
        )
        drawPath(
            path = mainWave,
            brush = Brush.horizontalGradient(
                listOf(
                    Color.Transparent,
                    Color(0xFFFF5F84).copy(alpha = 0.52f),
                    Color(0xFFFFB75E).copy(alpha = 0.70f),
                    Color.Transparent
                )
            ),
            style = Stroke(width = 2.4f, cap = StrokeCap.Round)
        )
        drawPath(
            path = lowerWave,
            color = Gold.copy(alpha = 0.09f),
            style = Stroke(width = 26f, cap = StrokeCap.Round)
        )
        drawPath(
            path = lowerWave,
            brush = Brush.horizontalGradient(
                listOf(Color.Transparent, Gold.copy(alpha = 0.48f), Color(0xFFFF6A8F).copy(alpha = 0.38f), Color.Transparent)
            ),
            style = Stroke(width = 1.8f, cap = StrokeCap.Round)
        )

        drawArc(
            color = Color(0xFFFF6A8F).copy(alpha = 0.34f),
            startAngle = 198f,
            sweepAngle = 128f,
            useCenter = false,
            topLeft = Offset(size.width * 0.26f, size.height * 0.13f),
            size = Size(size.width * 0.50f, size.width * 0.50f),
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )
        drawArc(
            color = Gold.copy(alpha = 0.26f),
            startAngle = 23f,
            sweepAngle = 92f,
            useCenter = false,
            topLeft = Offset(size.width * 0.21f, size.height * 0.18f),
            size = Size(size.width * 0.60f, size.width * 0.60f),
            style = Stroke(width = 1.4f, cap = StrokeCap.Round)
        )

        val sparkles = listOf(
            Offset(size.width * 0.12f, size.height * 0.37f),
            Offset(size.width * 0.21f, size.height * 0.42f),
            Offset(size.width * 0.72f, size.height * 0.34f),
            Offset(size.width * 0.84f, size.height * 0.39f),
            Offset(size.width * 0.90f, size.height * 0.47f),
            Offset(size.width * 0.63f, size.height * 0.51f),
            Offset(size.width * 0.34f, size.height * 0.50f),
        )
        sparkles.forEachIndexed { index, center ->
            drawCircle(
                color = if (index % 2 == 0) Gold.copy(alpha = 0.55f) else Color(0xFFFF6A8F).copy(alpha = 0.45f),
                radius = if (index % 3 == 0) 3.2f else 2.0f,
                center = center,
                style = Fill
            )
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
        color = Color.Transparent,
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 11.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (done) Gold.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.06f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (done) Gold.copy(alpha = 0.55f) else CreamText.copy(alpha = 0.20f)
                )
            ) {
                Icon(
                    modifier = Modifier
                        .padding(7.dp)
                        .size(16.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (done) Gold else CreamText.copy(alpha = 0.52f)
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = label,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Surface(
                shape = CircleShape,
                color = if (done) Gold.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.06f)
            ) {
                Icon(
                    modifier = Modifier
                        .padding(5.dp)
                        .size(16.dp),
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = if (done) Gold else Color.White.copy(alpha = 0.22f)
                )
            }
        }
    }
}
