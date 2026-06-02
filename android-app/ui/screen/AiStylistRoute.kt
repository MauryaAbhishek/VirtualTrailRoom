package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
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
fun AiStylistRoute(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8EF))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SimpleHeader(title = "AI STYLIST", onBackClick = onBackClick)
        Text(text = "Complete Your Look", color = MaroonDeep, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge)
        Text(text = "We recommend these for you ✨", color = Color(0xFF8D737A))
        StylistHero()
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AccessoryCard("Jhumka Earrings", "₹699")
            AccessoryCard("Gold Bracelet", "₹999")
            AccessoryCard("Embroidered Clutch", "₹1,299")
        }
        Surface(shape = RoundedCornerShape(22.dp), color = Color.White) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(100.dp), color = Color(0xFFFFF1D8)) {
                    Text(modifier = Modifier.padding(14.dp), text = "94", color = MaroonDeep, fontWeight = FontWeight.Black)
                }
                Column {
                    Text(text = "Look Score", fontWeight = FontWeight.Black, color = MaroonDeep)
                    Text(text = "94/100\nAmazing match! You look gorgeous ✨", color = Color(0xFF8D737A))
                }
            }
        }
    }
}

@Composable
private fun StylistHero() {
    Box(modifier = Modifier.fillMaxWidth().height(210.dp).background(Brush.verticalGradient(listOf(Color(0xFFFFD7B8), Color(0xFFB7791F))), RoundedCornerShape(22.dp)), contentAlignment = Alignment.Center) {
        Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.padding(70.dp))
    }
}

@Composable
private fun AccessoryCard(name: String, price: String) {
    Surface(modifier = Modifier.width(116.dp), shape = RoundedCornerShape(18.dp), color = Color.White, shadowElevation = 4.dp) {
        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(82.dp).background(Color(0xFFFFE8C0), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = Gold)
            }
            Text(text = name, color = MaroonDeep, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Black)
            Text(text = price, color = Color(0xFF8D737A), style = MaterialTheme.typography.bodySmall)
        }
    }
}
