package com.example.paowords.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paowords.data.CARD_DATA
import com.example.paowords.ui.theme.*

@Composable
fun TableScreen() {
    Column(modifier = Modifier.fillMaxSize().background(DarkBg).padding(8.dp)) {
        Text("📊 Master Table", color = Orange, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
        LazyColumn {
            items(CARD_DATA) { entry ->
                Row(modifier = Modifier.fillMaxWidth().background(if (entry.color == "Red") Color(0xFF2A1010) else Color(0xFF1A1A1A)).padding(10.dp)) {
                    Text(entry.card, color = Orange, fontWeight = FontWeight.Bold, modifier = Modifier.width(50.dp))
                    Column { Text(entry.person, color = White); Text("${entry.action} ${entry.objectName}", color = LightGrey, fontSize = 12.sp) }
                }
            }
        }
    }
}
