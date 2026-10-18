package com.example.paowords.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paowords.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun FullDeckScreen() {
    var seconds by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            seconds++
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(DarkBg).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("⏱️ Words Timer", color = Orange, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(32.dp))
        Text(String.format("%02d:%02d", seconds / 60, seconds % 60), color = Orange, fontSize = 80.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(32.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { isRunning = !isRunning }, colors = ButtonDefaults.buttonColors(containerColor = if (isRunning) Red else Green)) {
                Text(if (isRunning) "⏸ Pause" else "▶ Start")
            }
            Button(onClick = { isRunning = false; seconds = 0 }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555))) {
                Text("⏹ Reset")
            }
        }
    }
}
