package com.example.paowords.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paowords.data.WordEntry
import com.example.paowords.data.WORD_DATA
import com.example.paowords.ui.theme.*

@Composable
fun TeachScreen() {
    var input by remember { mutableStateOf("") }
    var active by remember { mutableStateOf<WordEntry?>(null) }
    var error by remember { mutableStateOf("") }
    val scroll = rememberScrollState()

    fun load() {
        val entry = WORD_DATA.find { it.word.equals(input.trim(), ignoreCase = true) }
        if (entry != null) { active = entry; error = "" } else { error = "Word not found. Try 'Justice'."; active = null }
    }

    fun random() { val word = WORD_DATA.random(); input = word.word; active = word; error = "" }

    fun next() {
        val currentWord = active ?: WORD_DATA.first()
        val idx = WORD_DATA.indexOf(currentWord)
        val nextWord = WORD_DATA[(idx + 1) % WORD_DATA.size]
        input = nextWord.word
        active = nextWord
        error = ""
    }

    Column(modifier = Modifier.fillMaxSize().background(DarkBg).verticalScroll(scroll).padding(16.dp)) {
        Text("📖 Teach", color = Orange, fontSize = 26.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
        
        Box(modifier = Modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
            if (active != null) { Text(active!!.emoji, fontSize = 80.sp) } else { Text("📖", fontSize = 80.sp) }
        }
        
        Spacer(Modifier.height(12.dp))
        Text(active?.word ?: "??", color = Orange, fontSize = 44.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
        Text(active?.phonetic ?: "—", color = LightGrey, fontSize = 20.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(8.dp))
        
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Image:", color = LightGrey, fontSize = 15.sp)
            Text(active?.image ?: "—", color = White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
        
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = input, onValueChange = { input = it },
            label = { Text("Enter word (e.g. Justice)") }, singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { load() }),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange, focusedTextColor = White)
        )
        if (error.isNotEmpty()) { Text(error, color = Red, fontSize = 12.sp) }
        Spacer(Modifier.height(8.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { load() }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Green)) { Text("↵ Go") }
            Button(onClick = { random() }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Orange, contentColor = Color.Black)) { Text("🎲") }
            Button(onClick = { next() }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555))) { Text("➡️") }
        }
        
        if (active != null) {
            Spacer(Modifier.height(16.dp))
            Column(modifier = Modifier.fillMaxWidth().background(DarkSurface, RoundedCornerShape(10.dp)).padding(14.dp)) {
                Text("📖 Definition", color = Orange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(active!!.bio, color = Color(0xFFDDDDDD), fontSize = 14.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text("🧠 ${active!!.mnemonic}", color = Color(0xFFDDDDDD), fontSize = 14.sp, modifier = Modifier.background(Color(0xFF1E2A1E), RoundedCornerShape(8.dp)).padding(14.dp))
        }
    }
}
