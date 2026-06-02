package com.virtualtrialroom.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SavedResultsRoute(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8EF))
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaroonDeep)
            }
            Text(modifier = Modifier.weight(1f), text = "CUSTOMERS", color = MaroonDeep, fontWeight = FontWeight.Black)
            IconButton(onClick = {}) {
                Icon(Icons.Filled.FilterList, contentDescription = "Filter", tint = MaroonDeep)
            }
        }
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            placeholder = { Text("Search customers...") },
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomerChip("All", true)
            CustomerChip("Interested", false)
            CustomerChip("Purchased", false)
        }
        CustomerRow("Priya Sharma", "3 Try-Ons", "Interested")
        CustomerRow("Neha Gupta", "5 Try-Ons", "Interested")
        CustomerRow("Anjali Singh", "2 Try-Ons", "Follow Up")
        CustomerRow("Kavya Verma", "4 Try-Ons", "Purchased")
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Maroon)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Text(text = "  Add Customer", fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun CustomerChip(text: String, selected: Boolean) {
    FilterChip(
        selected = selected,
        onClick = {},
        label = { Text(text, fontWeight = FontWeight.SemiBold) },
        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Maroon, selectedLabelColor = Color.White, containerColor = Color.White)
    )
}

@Composable
private fun CustomerRow(name: String, tries: String, status: String) {
    Surface(shape = RoundedCornerShape(18.dp), color = Color.White) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = Color(0xFFFFE2E9)) {
                Icon(modifier = Modifier.padding(10.dp), imageVector = Icons.Filled.Person, contentDescription = null, tint = Maroon)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, color = MaroonDeep, fontWeight = FontWeight.Black)
                Text(text = tries, color = Color(0xFF8D737A), style = MaterialTheme.typography.bodySmall)
            }
            Surface(color = if (status == "Purchased") Color(0xFFE5F8E8) else Color(0xFFFFF1D8), shape = RoundedCornerShape(100.dp)) {
                Text(modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp), text = status, color = if (status == "Purchased") Color(0xFF15803D) else Color(0xFFC07600), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}
