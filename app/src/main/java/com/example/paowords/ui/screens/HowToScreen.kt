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
        Text("🧠 How to Memorize Words", color = Orange, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Text("1. Phonetic Substitution", color = Orange, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Abstract words are hard to visualize. Turn them into concrete images that sound the same. (e.g., 'Justice' = 'Just Ice').", color = Color(0xFFDDDDDD), fontSize = 14.sp)
        Spacer(Modifier.height(16.dp))
        Text("2. Link the Images", color = Orange, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("If you need to memorize a list of words, turn each into an image and link them together into one crazy story.", color = Color(0xFFDDDDDD), fontSize = 14.sp)
    }
}
