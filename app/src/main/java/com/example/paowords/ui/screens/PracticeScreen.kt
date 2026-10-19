package com.example.paowords.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paowords.data.RANDOM_WORD_BANK
import com.example.paowords.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun PracticeScreen() {
    var gameState by remember { mutableStateOf("setup") }
    var wordCount by remember { mutableStateOf(15) }
    var isCompetitionMode by remember { mutableStateOf(false) }
    
    var currentWords by remember { mutableStateOf(listOf<String>()) }
    var userAnswers by remember { mutableStateOf(mapOf<String, String>()) }
    
    var timeLeft by remember { mutableStateOf(5) }
    var recallTimeLeft by remember { mutableStateOf(15 * 60) }
    var totalTimeMs by remember { mutableStateOf(0L) }
    var score by remember { mutableStateOf(0) }

    LaunchedEffect(gameState) {
        if (gameState == "memorizing") {
            timeLeft = if (isCompetitionMode) 5 * 60 else 5
            while (timeLeft > 0 && gameState == "memorizing") {
                delay(1000L)
                timeLeft--
            }
            if (gameState == "memorizing") {
                totalTimeMs = 0L
                recallTimeLeft = 15 * 60
                gameState = "testing"
            }
        }
    }

    LaunchedEffect(gameState) {
        if (gameState == "testing") {
            if (isCompetitionMode) {
                while (recallTimeLeft > 0 && gameState == "testing") {
                    delay(1000L)
                    recallTimeLeft--
                }
                if (recallTimeLeft <= 0) {
                    score = currentWords.count { userAnswers[it] == it.take(1).uppercase() }
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
                currentWords = RANDOM_WORD_BANK.shuffled().take(wordCount)
                userAnswers = currentWords.associateWith { "" }
                gameState = "memorizing"
            }, colors = ButtonDefaults.buttonColors(containerColor = Green)) {
                Text("Start Memorizing")
            }
        } 
        
        else if (gameState == "memorizing") {
            val timeString = if (isCompetitionMode) String.format("%02d:%02d", timeLeft / 60, timeLeft % 60) else "${timeLeft}s"
            Text("Memorize! ($timeString)", color = Red, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            
            currentWords.forEach { word ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = DarkSurface)) {
                    Text(word, color = White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp).fillMaxWidth(), textAlign = TextAlign.Center)
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
            
            currentWords.forEach { word ->
                OutlinedTextField(
                    value = userAnswers[word] ?: "",
                    onValueChange = { input: String ->
                        if (input.length <= 1) {
                            userAnswers = userAnswers + (word to input.uppercase())
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center, color = White),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange)
                )
            }
            
            Spacer(Modifier.height(24.dp))
            Button(onClick = {
                score = currentWords.count { userAnswers[it] == it.take(1).uppercase() }
                gameState = "results"
            }, colors = ButtonDefaults.buttonColors(containerColor = Green), modifier = Modifier.fillMaxWidth()) {
                Text("Check Answers")
            }
        } 
        
        else if (gameState == "results") {
            Text("Results", color = Orange, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("You got $score out of $wordCount correct!", color = White, fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))
            
            currentWords.forEach { word ->
                val isCorrect = userAnswers[word] == word.take(1).uppercase()
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = if (isCorrect) Color(0xFF1E2A1E) else Color(0xFF2A1E1E))) {
                    Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(word, color = White, fontWeight = FontWeight.Bold)
                        Text("You wrote: ${userAnswers[word] ?: "-"}", color = if (isCorrect) Green else Red)
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            Button(onClick = { gameState = "setup" }, colors = ButtonDefaults.buttonColors(containerColor = Orange, contentColor = Color.Black), modifier = Modifier.fillMaxWidth()) {
                Text("Play Again")
            }
        }
    }
}
