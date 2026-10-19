package com.example.paowords.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paowords.data.WORD_DATA
import com.example.paowords.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun PracticeScreen() {
    var gameState by remember { mutableStateOf("setup") }
    var wordCount by remember { mutableStateOf(15) }
    var isCompetitionMode by remember { mutableStateOf(false) }
    
    var currentWords by remember { mutableStateOf(listOf<com.example.paowords.data.WordEntry>()) }
    var userAnswers by remember { mutableStateOf(mapOf<String, String>()) }
    
    var timeLeft by remember { mutableStateOf(5) }
    var recallTimeLeft by remember { mutableStateOf(15 * 60) }
    var totalTimeMs by remember { mutableStateOf(0L) }
    var score by remember { mutableStateOf(0) }

    // Memorization Countdown
    LaunchedEffect(gameState) {
        if (gameState == "memorizing") {
            timeLeft = if (isCompetitionMode) 5 * 60 else 5
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            totalTimeMs = 0L
            recallTimeLeft = 15 * 60
            gameState = "testing"
        }
    }

    // Recall Timer
    LaunchedEffect(gameState) {
        if (gameState == "testing") {
            if (isCompetitionMode) {
                while (recallTimeLeft > 0 && gameState == "testing") {
                    delay(1000L)
                    recallTimeLeft--
                }
                if (recallTimeLeft <= 0) {
                    score = currentWords.count { userAnswers[it.word] == it.word.take(1).uppercase() }
                    gameState = "results"
                }
            } else {
                while (gameState == "testing") {
                    delay(100L)
                    totalTimeMs += 100L
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(DarkBg).padding(16.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🏋️ Speed Recall", color = Orange, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        
        if (gameState == "setup") {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("Mode:", color = LightGrey, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Button(onClick = { isCompetitionMode = false }, colors = ButtonDefaults.buttonColors(containerColor = if (!isCompetitionMode) Orange else DarkSurface)) {
                    Text("Training", color = if (!isCompetitionMode) Color.Black else White, fontSize = 12.sp)
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { isCompetitionMode = true }, colors = ButtonDefaults.buttonColors(containerColor = if (isCompetitionMode) Orange else DarkSurface)) {
                    Text("Competition", color = if (isCompetitionMode) Color.Black else White, fontSize = 12.sp)
                }
            }
            
            Spacer(Modifier.height(16.dp))
            Text("How many words?", color = LightGrey, textAlign = TextAlign.Center)
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf(5, 10, 15).forEach { count ->
                    Button(onClick = { wordCount = count }, colors = ButtonDefaults.buttonColors(containerColor = if (wordCount == count) Orange else DarkSurface, contentColor = if (wordCount == count) Color.Black else White)) {
                        Text("$count")
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf(20, 30, 40).forEach { count ->
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
            val timeString = if (isCompetitionMode) String.format("%02d:%02d", timeLeft / 60, timeLeft % 60) else "${timeLeft}s"
            Text("Memorize! ($timeString)", color = Red, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 400.dp)) {
                items(currentWords) { word ->
                    Card(colors = CardDefaults.cardColors(containerColor = DarkSurface)) {
                        Text(word.word, color = White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp).fillMaxWidth(), textAlign = TextAlign.Center)
                    }
                }
            }
        } 
        
        else if (gameState == "testing") {
            if (isCompetitionMode) {
                Text(String.format("⏱️ %02d:%02d left", recallTimeLeft / 60, recallTimeLeft % 60), color = Red, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            } else {
                Text(String.format("⏱️ %.1fs", totalTimeMs / 1000.0), color = Green, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(Modifier.height(8.dp))
            Text("Type the FIRST LETTER!", color = Orange, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            
            val columns = if (wordCount > 15) GridCells.Fixed(3) else GridCells.Fixed(2)
            LazyVerticalGrid(columns = columns, horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(400.dp)) {
                items(currentWords) { word ->
                    OutlinedTextField(
                        value = userAnswers[word.word] ?: "",
                        onValueChange = { input: String ->
                            if (input.length <= 1) {
                                userAnswers = userAnswers + (word.word to input.uppercase())
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 14.sp, color = White),
                        placeholder = { Text("?", fontSize = 14.sp, color = LightGrey) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, unfocusedBorderColor = Color.Gray),
                        contentPadding = PaddingValues(8.dp)
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    score = currentWords.count { userAnswers[it.word] == it.word.take(1).uppercase() }
                    gameState = "results"
                }, 
                colors = ButtonDefaults.buttonColors(containerColor = Green), 
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Check Answers")
            }
        } 
        
        else if (gameState == "results") {
            Text("Results", color = Orange, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("You got $score out of $wordCount correct!", color = White, fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))
            
            val columns = if (wordCount > 15) GridCells.Fixed(3) else GridCells.Fixed(2)
            LazyVerticalGrid(columns = columns, horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(400.dp)) {
                items(currentWords) { word ->
                    val isCorrect = userAnswers[word.word] == word.word.take(1).uppercase()
                    Card(colors = CardDefaults.cardColors(containerColor = if (isCorrect) Color(0xFF1E2A1E) else Color(0xFF2A1E1E))) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(word.word, color = White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("You wrote: ${userAnswers[word.word] ?: "-"}", color = if (isCorrect) Green else Red, fontSize = 10.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { gameState = "setup" }, 
                colors = ButtonDefaults.buttonColors(containerColor = Orange, contentColor = Color.Black), 
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Play Again")
            }
        }
    }
}
