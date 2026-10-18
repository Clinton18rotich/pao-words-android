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
import com.example.paowords.data.CARD_DATA
import com.example.paowords.ui.theme.*

@Composable
fun QuizScreen() {
    var current by remember { mutableStateOf(CARD_DATA.random()) }
    var answer by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableStateOf(0) }
    var total by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().background(DarkBg).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🧪 Quiz", color = Orange, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(current.emoji, fontSize = 60.sp)
        Text("${current.rank} of ${current.suit}", color = Orange, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = answer, onValueChange = { answer = it }, placeholder = { Text("Person - Action - Object") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            val correct = "${current.person} - ${current.action} - ${current.objectName}"
            if (answer.trim().equals(correct, true)) { result = "✅ Correct!"; score++ } else { result = "❌ Wrong: $correct" }
            total++
        }, modifier = Modifier.fillMaxWidth()) { Text("Check") }
        if (result != null) { Text(result!!, color = if (result!!.startsWith("✅")) Green else Red) }
        Text("Score: $score / $total")
        Spacer(Modifier.height(8.dp))
        Button(onClick = { current = CARD_DATA.random(); answer = ""; result = null }, modifier = Modifier.fillMaxWidth()) { Text("Next") }
    }
}
