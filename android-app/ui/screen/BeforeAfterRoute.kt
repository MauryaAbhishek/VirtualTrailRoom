package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Style
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BeforeAfterRoute(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8EF))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SimpleHeader(title = "BEFORE & AFTER", onBackClick = onBackClick)
        Surface(shape = RoundedCornerShape(26.dp), color = Color.White, shadowElevation = 8.dp) {
            Row(modifier = Modifier.fillMaxWidth().aspectRatio(0.72f)) {
                ComparePanel("Before", Color(0xFFFFEAD6), false, Modifier.weight(1f))
                Box(modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 0.dp), contentAlignment = Alignment.Center) {
                    Surface(shape = RoundedCornerShape(100.dp), color = Color.White, shadowElevation = 6.dp) {
                        Icon(modifier = Modifier.padding(8.dp), imageVector = Icons.AutoMirrored.Filled.CompareArrows, contentDescription = null, tint = Maroon)
                    }
                }
                ComparePanel("After", Maroon, true, Modifier.weight(1f))
            }
        }
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Maroon)
        ) {
            Icon(Icons.Filled.Save, contentDescription = null)
            Text(text = "  Save Comparison", fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun ComparePanel(label: String, color: Color, styled: Boolean, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (styled) Brush.verticalGradient(listOf(Color(0xFFFFD7B8), color)) else Brush.verticalGradient(listOf(Color(0xFFFFF2E4), color))),
        contentAlignment = Alignment.Center
    ) {
        Surface(modifier = Modifier.align(Alignment.TopStart).padding(10.dp), color = Color.Black.copy(alpha = 0.34f), shape = RoundedCornerShape(100.dp)) {
            Text(modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp), text = label, color = Color.White, style = MaterialTheme.typography.labelSmall)
        }
        Icon(imageVector = if (styled) Icons.Filled.Style else Icons.Filled.Save, contentDescription = null, tint = if (styled) Color.White else Maroon, modifier = Modifier.padding(40.dp))
    }
}
