package com.example.paowords.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paowords.ui.theme.*

@Composable
fun HowToScreen() {
    Column(modifier = Modifier.fillMaxSize().background(DarkBg).verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("🧠 How to Memorize Cards", color = Orange, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Text("1. Joshua Foer's Method", color = Orange, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Cards are memorized in groups of 3. Card 1 = Person, Card 2 = Action, Card 3 = Object. This forms one vivid scene. 18 scenes memorize the whole 52-card deck.", color = Color(0xFFDDDDDD), fontSize = 14.sp)
        Spacer(Modifier.height(16.dp))
        Text("2. The 4 Suits", color = Orange, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("♥ Hearts = Family\n♦ Diamonds = Celebrities\n♣ Clubs = Athletes\n♠ Spades = Historical Villains", color = Color(0xFFDDDDDD), fontSize = 14.sp)
        Spacer(Modifier.height(16.dp))
        Text("3. Use the App", color = Orange, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Type 'A♥' in Teach to see the Person, Action, and Object. Use Flashcards to test yourself. The stories include smells, tastes, and emotions to lock the cards in your memory.", color = Color(0xFFDDDDDD), fontSize = 14.sp)
    }
}
