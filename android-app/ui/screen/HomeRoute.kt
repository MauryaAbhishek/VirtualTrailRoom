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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

internal val Maroon = Color(0xFF4C061D)
internal val MaroonDeep = Color(0xFF21000D)
internal val Gold = Color(0xFFFFC15A)
internal val CreamText = Color(0xFFFFF2DD)

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
            .background(
                Brush.verticalGradient(
                    colors = listOf(MaroonDeep, Maroon, Color(0xFF140006))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            DashboardTopBar(onSettingsClick = onSettingsClick)
            BrandHeader()
            GreetingBlock()
            MetricCard(
                title = "Today's Try-Ons",
                value = "12",
                caption = "+4 from yesterday",
                icon = Icons.AutoMirrored.Filled.TrendingUp
            )
            CreditCard()
            Button(
                onClick = onCaptureClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gold,
                    contentColor = Color(0xFF24000E)
                )
            ) {
                Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "New AI Try-On", fontWeight = FontWeight.Black)
            }
            RecentHeader(onSavedResultsClick)
            RecentTryOnList()
            Spacer(modifier = Modifier.height(66.dp))
        }
        DashboardBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            onHomeClick = {},
            onStudioClick = onCaptureClick,
            onCatalogClick = onWardrobeClick,
            onCustomersClick = onSavedResultsClick,
            onMoreClick = onSettingsClick
        )
    }
}

@Composable
private fun DashboardTopBar(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onSettingsClick) {
            Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu", tint = CreamText)
        }
        Surface(shape = CircleShape, color = Gold) {
            Icon(
                modifier = Modifier.padding(9.dp),
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifications",
                tint = MaroonDeep
            )
        }
    }
}

@Composable
private fun BrandHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "SAAJ",
            style = MaterialTheme.typography.displaySmall,
            color = Gold,
            fontWeight = FontWeight.Light
        )
        Text(
            text = "AI Fashion Assistant",
            style = MaterialTheme.typography.bodyMedium,
            color = CreamText.copy(alpha = 0.78f)
        )
    }
}

@Composable
private fun GreetingBlock() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = "Good Morning,", style = MaterialTheme.typography.bodyMedium, color = CreamText.copy(alpha = 0.78f))
        Text(text = "Priya Boutique", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    caption: String,
    icon: ImageVector
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(text = title, style = MaterialTheme.typography.labelLarge, color = CreamText)
                Text(text = value, style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Black)
                Text(text = caption, style = MaterialTheme.typography.bodySmall, color = CreamText.copy(alpha = 0.7f))
            }
            Icon(imageVector = icon, contentDescription = null, tint = Gold, modifier = Modifier.size(42.dp))
        }
    }
}

@Composable
private fun CreditCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.08f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "Credits Remaining", style = MaterialTheme.typography.labelLarge, color = CreamText)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = "88", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Black)
                Text(text = " / 100", style = MaterialTheme.typography.titleMedium, color = CreamText.copy(alpha = 0.72f))
            }
            LinearProgressIndicator(
                progress = { 0.88f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(100.dp)),
                color = Gold,
                trackColor = Color.White.copy(alpha = 0.13f)
            )
        }
    }
}

@Composable
private fun RecentHeader(onSavedResultsClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Recent Try-Ons", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
        Surface(onClick = onSavedResultsClick, color = Color.Transparent) {
            Text(text = "See All", style = MaterialTheme.typography.labelLarge, color = Gold, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun RecentTryOnList() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        RecentTryOn(name = "Priya Sharma", garment = "Red Banarasi Saree", time = "2 min ago", color = Color(0xFFB91C1C))
        RecentTryOn(name = "Neha Gupta", garment = "Peach Organza Saree", time = "15 min ago", color = Color(0xFFE59F7B))
    }
}

@Composable
private fun RecentTryOn(
    name: String,
    garment: String,
    time: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SareeThumb(color = color, modifier = Modifier.size(58.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(text = name, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(text = garment, color = CreamText.copy(alpha = 0.72f), style = MaterialTheme.typography.bodySmall)
            Text(text = time, color = Gold, style = MaterialTheme.typography.bodySmall)
        }
        Icon(imageVector = Icons.Filled.MoreHoriz, contentDescription = null, tint = CreamText.copy(alpha = 0.48f))
    }
}

@Composable
private fun SareeThumb(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(color.copy(alpha = 0.95f), Color(0xFFFFD37A), color)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = Icons.Filled.Checkroom, contentDescription = null, tint = Color.White)
    }
}

@Composable
private fun DashboardBottomBar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onStudioClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onCustomersClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF180008).copy(alpha = 0.96f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomItem("Home", Icons.Filled.Home, true, onHomeClick)
            BottomItem("Studio", Icons.Filled.CameraAlt, false, onStudioClick)
            BottomItem("Catalog", Icons.Filled.Checkroom, false, onCatalogClick)
            BottomItem("Looks", Icons.Filled.Collections, false, onCustomersClick)
            BottomItem("More", Icons.Filled.AccountCircle, false, onMoreClick)
        }
    }
}

@Composable
private fun BottomItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(onClick = onClick, color = Color.Transparent) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = label, tint = if (selected) Gold else CreamText.copy(alpha = 0.64f))
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = if (selected) Gold else CreamText.copy(alpha = 0.64f))
        }
    }
}
