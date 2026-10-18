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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.paowords.data.CardEntry
import com.example.paowords.data.CARD_DATA
import com.example.paowords.data.getCard
import com.example.paowords.ui.theme.*

@Composable
fun TeachScreen() {
    var input by remember { mutableStateOf("") }
    var active by remember { mutableStateOf<CardEntry?>(null) }
    var error by remember { mutableStateOf("") }
    val scroll = rememberScrollState()

    fun load() {
        val entry = getCard(input.trim())
        if (entry != null) { active = entry; error = "" } else { error = "Card not found. Try A♥ or 5♦"; active = null }
    }

    fun random() { val card = CARD_DATA.random(); input = card.card; active = card; error = "" }

    fun next() {
        val currentCard = active ?: CARD_DATA.first()
        val idx = CARD_DATA.indexOf(currentCard)
        val nextCard = CARD_DATA[(idx + 1) % CARD_DATA.size]
        input = nextCard.card
        active = nextCard
        error = ""
    }

    Column(modifier = Modifier.fillMaxSize().background(DarkBg).verticalScroll(scroll).padding(16.dp)) {
        Text("🃏 Teach", color = Orange, fontSize = 26.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
        
        Box(modifier = Modifier.fillMaxWidth().height(240.dp), contentAlignment = Alignment.Center) {
            if (active != null) {
                AsyncImage(
                    model = "file:///android_asset/cards/${active!!.imageName}.png",
                    contentDescription = active!!.card,
                    modifier = Modifier.height(240.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text("🃏", fontSize = 80.sp)
            }
        }
        
        Spacer(Modifier.height(12.dp))
        Text(active?.card ?: "??", color = Orange, fontSize = 44.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(8.dp))
        
        PAORow("Person:", active?.person ?: "—")
        PAORow("Action:", active?.action ?: "—")
        PAORow("Object:", active?.objectName ?: "—")
        
        Spacer(Modifier.height(12.dp))
        
        OutlinedTextField(
            value = input, 
            onValueChange = { if (it.length <= 4) input = it },
            label = { Text("Enter card (e.g. A, 5, 10)") }, 
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { load() }),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Orange, focusedLabelColor = Orange, focusedTextColor = White)
        )

        Spacer(Modifier.height(8.dp))
        
        // Suit Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { input += "♥" }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A1010))) { Text("♥", fontSize = 20.sp, color = Color.Red) }
            Button(onClick = { input += "♦" }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A1010))) { Text("♦", fontSize = 20.sp, color = Color.Red) }
            Button(onClick = { input += "♣" }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222222))) { Text("♣", fontSize = 20.sp, color = White) }
            Button(onClick = { input += "♠" }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222222))) { Text("♠", fontSize = 20.sp, color = White) }
        }

        if (error.isNotEmpty()) { Text(error, color = Red, fontSize = 12.sp) }
        
        Spacer(Modifier.height(8.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { load() }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Green)) { Text("↵ Go") }
            Button(onClick = { random() }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Orange, contentColor = Color.Black)) { Text("🎲") }
            Button(onClick = { next() }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555))) { Text("➡️") }
        }
        
        if (active != null) {
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.background(if (active!!.color == "Red") Color(0xFF4A1010) else Color(0xFF222222), RoundedCornerShape(20.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) { Text(active!!.suit, color = if (active!!.color == "Red") Color(0xFFFF6B6B) else LightGrey, fontSize = 12.sp) }
                Box(modifier = Modifier.background(Color(0xFF1E2A1E), RoundedCornerShape(20.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) { Text(active!!.category, color = Green, fontSize = 12.sp) }
            }
            Spacer(Modifier.height(12.dp))
            Column(modifier = Modifier.fillMaxWidth().background(DarkSurface, RoundedCornerShape(10.dp)).padding(14.dp)) {
                Text("📖 Bio", color = Orange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(active!!.bio, color = Color(0xFFDDDDDD), fontSize = 14.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text("🧠 ${active!!.mnemonic}", color = Color(0xFFDDDDDD), fontSize = 14.sp, modifier = Modifier.background(Color(0xFF1E2A1E), RoundedCornerShape(8.dp)).padding(14.dp))
        }
    }
}

@Composable private fun PAORow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = LightGrey, fontSize = 15.sp); Text(value, color = White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}
