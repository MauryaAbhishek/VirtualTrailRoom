package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InsightsRoute(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8EF))
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SimpleHeader(title = "INSIGHTS", onBackClick = onBackClick)
        Surface(shape = RoundedCornerShape(22.dp), color = Maroon, shadowElevation = 8.dp) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Revenue Influenced", color = CreamText)
                Text(text = "₹42,350", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text(text = "+28% from last month", color = Color(0xFF5FE08A))
                Box(modifier = Modifier.fillMaxWidth().height(60.dp), contentAlignment = Alignment.CenterEnd) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Gold)
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            InsightMetric("Total Try-Ons", "182", "+35%", Modifier.weight(1f))
            InsightMetric("Conversion Rate", "24%", "+12%", Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RankingCard("Top Colors", listOf("Red", "Pink", "Peach"), Modifier.weight(1f))
            RankingCard("Top Categories", listOf("Wedding", "Silk", "Festive"), Modifier.weight(1f))
        }
    }
}

@Composable
private fun InsightMetric(title: String, value: String, growth: String, modifier: Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(18.dp), color = Color.White) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, color = Color(0xFF8D737A), style = MaterialTheme.typography.bodySmall)
            Text(text = value, color = MaroonDeep, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            Text(text = growth, color = Color(0xFF16A34A), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun RankingCard(title: String, rows: List<String>, modifier: Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(18.dp), color = Color.White) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = title, color = MaroonDeep, fontWeight = FontWeight.Black)
            rows.forEachIndexed { index, row ->
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = row, color = Color(0xFF5D4650), style = MaterialTheme.typography.bodySmall)
                    Box(modifier = Modifier.height(5.dp).fillMaxWidth(0.38f).background(if (index == 0) Maroon else Color(0xFFE8D8CB), RoundedCornerShape(100.dp)))
                }
            }
        }
    }
}
