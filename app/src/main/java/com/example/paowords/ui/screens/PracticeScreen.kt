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
import com.example.paowords.data.WORD_DATA
import com.example.paowords.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun PracticeScreen() {
    // Game State
    var gameState by remember { mutableStateOf("setup") } // setup, memorizing, testing, results
    var wordCount by remember { mutableStateOf(15) }
    var isCompetitionMode by remember { mutableStateOf(false) }
    
    // Data State
    var currentWords by remember { mutableStateOf(listOf<com.example.paowords.data.WordEntry>()) }
    var userAnswers by remember { mutableStateOf(mapOf<String, String>()) }
    
    // Timer State
    var timeLeft by remember { mutableStateOf(5) }
    var recallTimeLeft by remember { mutableStateOf(15 * 60) }
    var totalTimeMs by remember { mutableStateOf(0L) }
    var score by remember { mutableStateOf(0) }

    // 1. MEMORIZATION COUNTDOWN
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

    // 2. RECALL TIMER
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

    // MAIN LAYOUT (Scrollable to prevent button getting lost)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🏋️ Speed Recall", color = Orange, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        if (gameState == "setup") {
            // Mode Selector
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { isCompetitionMode = false },
                    colors = ButtonDefaults.buttonColors(containerColor = if (!isCompetitionMode) Orange else DarkSurface)
                ) { Text("Training", color = if (!isCompetitionMode) Color.Black else White, fontSize = 12.sp) }
                
                Button(
                    onClick = { isCompetitionMode = true },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isCompetitionMode) Orange else DarkSurface)
                ) { Text("Competition", color = if (isCompetitionMode) Color.Black else White, fontSize = 12.sp) }
            }
            
            Spacer(Modifier.height(24.dp))
            Text("How many words?", color = LightGrey)
            
            // Word Count Selector
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf(5, 10, 15).forEach { count ->
                    Button(
                        onClick = { wordCount = count },
                        colors = ButtonDefaults.buttonColors(containerColor = if (wordCount == count) Orange else DarkSurface),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) { Text("$count", color = if (wordCount == count) Color.Black else White) }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf(20, 30, 40).forEach { count ->
                    Button(
                        onClick = { wordCount = count },
                        colors = ButtonDefaults.buttonColors(containerColor = if (wordCount == count) Orange else DarkSurface),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) { Text("$count", color = if (wordCount == count) Color.Black else White) }
                }
            }

            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    currentWords = WORD_DATA.shuffled().take(wordCount)
                    userAnswers = currentWords.associate { it.word to "" }
                    gameState = "memorizing"
                },
                colors = ButtonDefaults.buttonColors(containerColor = Green),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Start Memorizing") }
        }

        else if (gameState == "memorizing") {
            val timeString = if (isCompetitionMode) String.format("%02d:%02d", timeLeft / 60, timeLeft % 60) else "${timeLeft}s"
            Text("Memorize! ($timeString)", color = Red, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            
            // Simple Column List of words instead of Grid (prevents crashes)
            currentWords.forEach { word ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Text(word.word, color = White, fontSize = 16.sp, modifier = Modifier.padding(12.dp).fillMaxWidth(), textAlign = TextAlign.Center)
                }
            }
        }

        else if (gameState == "testing") {
            // Timer Display
            if (isCompetitionMode) {
                Text(String.format("⏱️ %02d:%02d left", recallTimeLeft / 60, recallTimeLeft % 60), color = Red, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            } else {
                Text(String.format("⏱️ %.1fs", totalTimeMs / 1000.0), color = Green, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(Modifier.height(8.dp))
            Text("Type the FIRST LETTER!", color = Orange, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            // Input fields stacked vertically to guarantee no layout conflicts
            currentWords.forEach { word ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = userAnswers[word.word] ?: "",
                        onValueChange = { input: String ->
                            if (input.length <= 1) {
                                userAnswers = userAnswers + (word.word to input.uppercase())
                            }
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center, color = White),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange)
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
            ) { Text("Check Answers") }
        }

        else if (gameState == "results") {
            Text("Results", color = Orange, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("You got $score out of $wordCount correct!", color = White, fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))

            currentWords.forEach { word ->
                val isCorrect = userAnswers[word.word] == word.word.take(1).uppercase()
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isCorrect) Color(0xFF1E2A1E) else Color(0xFF2A1E1E))
                ) {
                    Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(word.word, color = White, fontWeight = FontWeight.Bold)
                        Text("You wrote: ${userAnswers[word.word] ?: "-"}", color = if (isCorrect) Green else Red)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { gameState = "setup" },
                colors = ButtonDefaults.buttonColors(containerColor = Orange, contentColor = Color.Black),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Play Again") }
        }
    }
}
