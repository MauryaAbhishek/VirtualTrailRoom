package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeRoute(
    onCaptureClick: () -> Unit,
    onWardrobeClick: () -> Unit,
    onSavedResultsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF181015),
                        Color(0xFF3D1426),
                        Color(0xFFFAF8F4)
                    ),
                    endY = 1250f
                )
            )
    ) {
        val compact = maxHeight < 720.dp
        val visualHeight = if (compact) 360.dp else 460.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 14.dp else 18.dp)
        ) {
            StudioHeader(onSettingsClick = onSettingsClick)
            EditorialHero(height = visualHeight)
            ActionRail(
                onCaptureClick = onCaptureClick,
                onWardrobeClick = onWardrobeClick,
                onSavedResultsClick = onSavedResultsClick
            )
        }
    }
}

@Composable
private fun StudioHeader(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "VTR",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "AI Fashion Studio",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.72f),
                fontWeight = FontWeight.SemiBold
            )
        }
        FilledTonalIconButton(onClick = onSettingsClick) {
            Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
        }
    }
}

@Composable
private fun EditorialHero(height: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(34.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFE3EC),
                        Color(0xFFE6A23C),
                        Color(0xFFC2185B),
                        Color(0xFF241018)
                    )
                )
            )
            .padding(20.dp)
    ) {
        RunwayArch(modifier = Modifier.align(Alignment.Center))
        FabricStack(modifier = Modifier.align(Alignment.TopEnd))
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(),
            color = Color.Black.copy(alpha = 0.30f),
            contentColor = Color.White,
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null)
                    Text(
                        text = "Instant Try-On",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "Wear any saree before you buy",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Studio-grade preview from your photo and garment image.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.82f)
                )
            }
        }
    }
}

@Composable
private fun RunwayArch(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.74f)
            .aspectRatio(0.68f)
            .clip(RoundedCornerShape(topStart = 140.dp, topEnd = 140.dp, bottomStart = 30.dp, bottomEnd = 30.dp))
            .background(Color.White.copy(alpha = 0.18f))
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 120.dp, topEnd = 120.dp, bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(Color.White.copy(alpha = 0.18f))
        )
        Icon(
            imageVector = Icons.Filled.Style,
            contentDescription = null,
            modifier = Modifier.size(74.dp),
            tint = Color.White.copy(alpha = 0.92f)
        )
    }
}

@Composable
private fun FabricStack(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.width(92.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FabricStrip(Color(0xFFFFF4D6))
        FabricStrip(Color(0xFFD6F3F1))
        FabricStrip(Color(0xFFFFD9E6))
    }
}

@Composable
private fun FabricStrip(color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.86f))
    )
}

@Composable
private fun ActionRail(
    onCaptureClick: () -> Unit,
    onWardrobeClick: () -> Unit,
    onSavedResultsClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(30.dp),
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCaptureClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(imageVector = Icons.Filled.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Start With Person Photo", fontWeight = FontWeight.Bold)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StudioAction(
                    text = "Garment",
                    icon = Icons.Filled.Checkroom,
                    modifier = Modifier.weight(1f),
                    onClick = onWardrobeClick
                )
                StudioAction(
                    text = "Looks",
                    icon = Icons.Filled.Collections,
                    modifier = Modifier.weight(1f),
                    onClick = onSavedResultsClick
                )
            }
        }
    }
}

@Composable
private fun StudioAction(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.height(58.dp),
        onClick = onClick,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontWeight = FontWeight.Bold)
        }
    }
}
