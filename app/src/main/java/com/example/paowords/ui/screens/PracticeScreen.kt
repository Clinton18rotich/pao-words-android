package com.example.paowords.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paowords.data.WORD_DATA
import com.example.paowords.ui.theme.*

@Composable
fun PracticeScreen() {
    var words by remember { mutableStateOf(List(3) { WORD_DATA.random() }) }
    var revealed by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(DarkBg).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("🧠 Practice Mode", color = Orange, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Link the 3 Words into a Story", color = LightGrey, fontSize = 14.sp)
        Spacer(Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            words.forEach { word ->
                Card(modifier = Modifier.weight(1f).height(160.dp), colors = CardDefaults.cardColors(containerColor = DarkSurface)) {
                    Column(modifier = Modifier.fillMaxSize().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(word.emoji, fontSize = 40.sp)
                        Text(word.word, color = Orange, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = { revealed = !revealed }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Orange, contentColor = Color.Black)) {
            Text(if (revealed) "Hide Story" else "Reveal Story")
        }
        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(visible = revealed, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
            Column(modifier = Modifier.fillMaxWidth().background(DarkSurface, RoundedCornerShape(10.dp)).padding(16.dp)) {
                Text("Link them: ${words[0].word} + ${words[1].word} + ${words[2].word}", color = White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Imagine: ${words[0].image} is doing something with ${words[1].image} and ${words[2].image}.", color = Green, fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = { words = List(3) { WORD_DATA.random() }; revealed = false }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555))) {
            Text("🎲 Shuffle New Words")
        }
    }
}
