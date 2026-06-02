package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
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
fun SettingsRoute(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8EF))
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(MaroonDeep, Maroon)))
                .padding(horizontal = 18.dp, vertical = 20.dp)
        ) {
            IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "SAAJ", color = Gold, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Light)
                Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.20f)) {
                    Icon(modifier = Modifier.padding(12.dp).size(34.dp), imageVector = Icons.Filled.AccountCircle, contentDescription = null, tint = Color.White)
                }
            }
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                MoreRow("My Profile", Icons.Filled.AccountCircle)
                MoreRow("Business Details", Icons.Filled.Business)
                MoreRow("Subscription & Credits", Icons.Filled.CreditCard)
                MoreRow("AI Settings", Icons.Filled.Settings)
                MoreRow("WhatsApp Templates", Icons.Filled.Storage)
                MoreRow("Reports", Icons.Filled.BarChart)
                MoreRow("Help & Support", Icons.AutoMirrored.Filled.Help)
                MoreRow("Logout", Icons.AutoMirrored.Filled.Logout, danger = true)
            }
        }
    }
}

@Composable
private fun MoreRow(
    title: String,
    icon: ImageVector,
    danger: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = if (danger) Color(0xFFDC2626) else MaroonDeep)
        Text(modifier = Modifier.weight(1f), text = title, color = if (danger) Color(0xFFDC2626) else MaroonDeep, fontWeight = FontWeight.SemiBold)
        Text(text = "›", color = if (danger) Color(0xFFDC2626) else Color(0xFF8D737A), style = MaterialTheme.typography.titleLarge)
    }
}
