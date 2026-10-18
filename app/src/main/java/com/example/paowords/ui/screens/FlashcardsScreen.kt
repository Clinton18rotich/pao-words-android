package com.example.paowords.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.paowords.data.CARD_DATA
import com.example.paowords.ui.theme.*

@Composable
fun FlashcardsScreen() {
    var current by remember { mutableStateOf(CARD_DATA.random()) }
    var revealed by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(DarkBg).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        
        Card(
            modifier = Modifier.fillMaxWidth(0.85f).height(480.dp).shadow(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                AsyncImage(
                    model = "file:///android_asset/cards/${current.imageName}.png",
                    contentDescription = current.card,
                    modifier = Modifier.height(220.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.height(16.dp))
                
                if (!revealed) { 
                    Button(onClick = { revealed = true }, colors = ButtonDefaults.buttonColors(containerColor = Orange, contentColor = Color.Black)) { Text("Reveal PAO") } 
                } else {
                    Text(current.person, color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(current.action, color = Color.DarkGray, fontSize = 16.sp)
                    Text(current.objectName, color = Color.DarkGray, fontSize = 16.sp)
                }
            }
        }
        
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { current = CARD_DATA.random(); revealed = false }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555))) { Text("🎲 Random") }
            Button(onClick = { val idx = (CARD_DATA.indexOf(current) + 1) % CARD_DATA.size; current = CARD_DATA[idx]; revealed = false }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555))) { Text("➡️ Next") }
        }
    }
}
