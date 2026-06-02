package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeRoute(
    onCaptureClick: () -> Unit,
    onWardrobeClick: () -> Unit,
    onSavedResultsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF8F4))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            TopHeader(onSettingsClick = onSettingsClick)
            HeroPanel()
            WorkflowPanel(
                onCaptureClick = onCaptureClick,
                onWardrobeClick = onWardrobeClick
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CompactAction(
                    title = "Saved",
                    subtitle = "Looks",
                    icon = Icons.Filled.Collections,
                    modifier = Modifier.weight(1f),
                    onClick = onSavedResultsClick
                )
                CompactAction(
                    title = "Quality",
                    subtitle = "Ready",
                    icon = Icons.Filled.Verified,
                    modifier = Modifier.weight(1f),
                    onClick = onWardrobeClick
                )
            }
        }
    }
}

@Composable
private fun TopHeader(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Virtual Trial Room",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = Color(0xFF17151A)
            )
            Text(
                text = "AI-powered fashion preview",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF68606A)
            )
        }
        Surface(
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = Color(0xFF17151A)
                )
            }
        }
    }
}

@Composable
private fun HeroPanel() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        color = Color(0xFF17151A),
        shadowElevation = 12.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFF17151A),
                            Color(0xFF6B183D),
                            Color(0xFFE6A23C)
                        )
                    )
                )
                .padding(22.dp)
        ) {
            Surface(
                modifier = Modifier.align(Alignment.TopStart),
                color = Color.White.copy(alpha = 0.14f),
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
                        text = "Photoreal try-on",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(
                modifier = Modifier.align(Alignment.BottomStart),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "See the saree on your photo",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Upload one full-body person photo and one garment photo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.82f)
                )
            }
        }
    }
}

@Composable
private fun WorkflowPanel(
    onCaptureClick: () -> Unit,
    onWardrobeClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Create your look",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Color(0xFF17151A)
            )
            WorkflowAction(
                step = "01",
                title = "Add person photo",
                subtitle = "Use a clear full-body image",
                icon = Icons.Filled.CameraAlt,
                primary = true,
                onClick = onCaptureClick
            )
            WorkflowAction(
                step = "02",
                title = "Add garment photo",
                subtitle = "Upload saree, dress, top, or outfit",
                icon = Icons.Filled.Checkroom,
                primary = false,
                onClick = onWardrobeClick
            )
        }
    }
}

@Composable
private fun WorkflowAction(
    step: String,
    title: String,
    subtitle: String,
    icon: ImageVector,
    primary: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp),
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (primary) MaterialTheme.colorScheme.primary else Color(0xFFF4EFE9),
            contentColor = if (primary) Color.White else Color(0xFF17151A)
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = if (primary) Color.White.copy(alpha = 0.18f) else Color.White
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                text = step,
                fontWeight = FontWeight.Black
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = title, fontWeight = FontWeight.Black)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = if (primary) Color.White.copy(alpha = 0.78f) else Color(0xFF68606A)
            )
        }
    }
}

@Composable
private fun CompactAction(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.height(104.dp),
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE8DED4)),
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(text = title, fontWeight = FontWeight.Black, color = Color(0xFF17151A))
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF68606A))
            }
        }
    }
}
