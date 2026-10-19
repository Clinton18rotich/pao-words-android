package com.example.paowords.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paowords.data.WORD_DATA
import com.example.paowords.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun PracticeScreen() {
    var gameState by remember { mutableStateOf("setup") } // setup, memorizing, testing, results
    var wordCount by remember { mutableStateOf(5) }
    var currentWords by remember { mutableStateOf(listOf<com.example.paowords.data.WordEntry>()) }
    var userAnswers by remember { mutableStateOf(mapOf<String, String>()) }
    var timeLeft by remember { mutableStateOf(5) }
    var score by remember { mutableStateOf(0) }

    LaunchedEffect(gameState) {
        if (gameState == "memorizing") {
            timeLeft = 5
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            gameState = "testing"
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(DarkBg).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🏋️ Speed Recall", color = Orange, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        
        if (gameState == "setup") {
            Text("How many words do you want to memorize?", color = LightGrey, modifier = Modifier.padding(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(5, 10, 15).forEach { count ->
                    Button(onClick = { wordCount = count }, colors = ButtonDefaults.buttonColors(containerColor = if (wordCount == count) Orange else DarkSurface, contentColor = if (wordCount == count) Color.Black else White)) {
                        Text("$count")
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(onClick = {
                currentWords = WORD_DATA.shuffled().take(wordCount)
                userAnswers = currentWords.associate { it.word to "" }
                gameState = "memorizing"
            }, colors = ButtonDefaults.buttonColors(containerColor = Green)) {
                Text("Start Memorizing")
            }
        } 
        
        else if (gameState == "memorizing") {
            Text("Memorize these words! (${timeLeft}s)", color = Red, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(currentWords) { word ->
                    Card(colors = CardDefaults.cardColors(containerColor = DarkSurface)) {
                        Text(word.word, color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp).fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            }
        } 
        
        else if (gameState == "testing") {
            Text("Type the FIRST LETTER of each word!", color = Orange, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(currentWords) { word ->
                    Column {
                        Text(word.word, color = LightGrey, fontSize = 12.sp)
                        OutlinedTextField(
                            value = userAnswers[word.word] ?: "",
                            onValueChange = { input ->
                                if (input.length <= 1) {
                                    userAnswers = userAnswers + (word.word to input.uppercase())
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedTextColor = White)
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                score = currentWords.count { userAnswers[it.word] == it.word.take(1).uppercase() }
                gameState = "results"
            }, colors = ButtonDefaults.buttonColors(containerColor = Green)) {
                Text("Check Answers")
            }
        } 
        
        else if (gameState == "results") {
            Text("Results", color = Orange, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Text("You got $score out of $wordCount correct!", color = White, fontSize = 20.sp)
            Spacer(Modifier.height(24.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(currentWords) { word ->
                    val isCorrect = userAnswers[word.word] == word.word.take(1).uppercase()
                    Card(colors = CardDefaults.cardColors(containerColor = if (isCorrect) Color(0xFF1E2A1E) else Color(0xFF2A1E1E))) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(word.word, color = White, fontWeight = FontWeight.Bold)
                            Text("You wrote: ${userAnswers[word.word] ?: "-"}", color = if (isCorrect) Green else Red, fontSize = 12.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { gameState = "setup" }, colors = ButtonDefaults.buttonColors(containerColor = Orange, contentColor = Color.Black)) {
                Text("Play Again")
            }
        }
    }
}
