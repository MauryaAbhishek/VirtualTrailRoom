package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.virtualtrialroom.app.ui.components.AppScaffold
import com.virtualtrialroom.app.ui.components.PrimaryActionButton
import com.virtualtrialroom.app.ui.components.SecondaryActionButton

@Composable
fun HomeRoute(
    onCaptureClick: () -> Unit,
    onWardrobeClick: () -> Unit,
    onSavedResultsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    AppScaffold(title = "VTR Studio") {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            StudioHero()
            RunwaySteps()
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StudioMetric(
                    title = "Person",
                    label = "Full photo",
                    icon = Icons.Filled.PhotoCamera,
                    modifier = Modifier.weight(1f)
                )
                StudioMetric(
                    title = "Garment",
                    label = "Saree image",
                    icon = Icons.Filled.Style,
                    modifier = Modifier.weight(1f)
                )
            }
            PrimaryActionButton(
                text = "Add Person Photo",
                icon = Icons.Filled.CameraAlt,
                onClick = onCaptureClick
            )
            SecondaryActionButton(
                text = "Add Garment Photo",
                icon = Icons.Filled.Checkroom,
                onClick = onWardrobeClick
            )
            SecondaryActionButton(
                text = "Saved Looks",
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

@Composable
private fun StudioHero() {
    BoxWithConstraints {
        val heroHeight = if (maxHeight < 720.dp) 190.dp else 238.dp
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heroHeight)
                .clip(RoundedCornerShape(30.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1F1519),
                            Color(0xFF8A123F),
                            Color(0xFFC2185B),
                            Color(0xFFE6A23C)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 8.dp, y = (-4).dp)
                    .rotate(-7f),
                color = Color.White.copy(alpha = 0.16f),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .size(width = 118.dp, height = 146.dp)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    FashionSwatch(Color(0xFFFFD9E6))
                    FashionSwatch(Color(0xFFB88A56))
                    FashionSwatch(Color(0xFFD6F3F1))
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(0.78f),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.16f),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null)
                        Text(
                            text = "AI Fashion Studio",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(
                    text = "Try the exact outfit on you",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Person photo + saree photo = realistic look.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.88f)
                )
            }
        }
    }
}

@Composable
private fun FashionSwatch(color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color)
    )
}

@Composable
private fun RunwaySteps() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StepChip(text = "Photo", modifier = Modifier.weight(1f))
        StepChip(text = "Garment", modifier = Modifier.weight(1f))
        StepChip(text = "Result", modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StepChip(text: String, modifier: Modifier = Modifier) {
    AssistChip(
        modifier = modifier,
        onClick = {},
        leadingIcon = {
            Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null)
        },
        label = {
            Text(text = text, fontWeight = FontWeight.SemiBold)
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface,
            leadingIconContentColor = MaterialTheme.colorScheme.primary
        ),
        border = AssistChipDefaults.assistChipBorder(
            enabled = true,
            borderColor = MaterialTheme.colorScheme.outlineVariant
        )
    )
}

@Composable
private fun StudioMetric(
    title: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
