package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun ShareCustomerRoute(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8EF))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SimpleHeader(title = "SHARE WITH CUSTOMER", onBackClick = onBackClick)
        Surface(shape = RoundedCornerShape(24.dp), color = Color.White, shadowElevation = 8.dp) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Hi Priya Sharma,", color = MaroonDeep, fontWeight = FontWeight.Black)
                Text(text = "Here is your AI Try-On Look from Saaj 💖", color = Color(0xFF5D4650))
                Box(
                    modifier = Modifier.fillMaxWidth().height(310.dp).background(Brush.verticalGradient(listOf(Color(0xFFFFD7B8), Maroon)), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Style, contentDescription = null, tint = Color.White, modifier = Modifier.padding(72.dp))
                }
                Text(text = "Red Banarasi Silk Saree\n₹4,599", color = MaroonDeep, fontWeight = FontWeight.Black)
                Text(text = "How do you like the look?\nLet us know! ✨", color = Color(0xFF5D4650))
            }
        }
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
            Text(text = "  Send on WhatsApp", fontWeight = FontWeight.Black)
        }
    }
}
