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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.paowords.data.CARD_DATA
import com.example.paowords.ui.theme.*

@Composable
fun PracticeScreen() {
    var cards by remember { mutableStateOf(List(3) { CARD_DATA.random() }) }
    var revealed by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(DarkBg).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("🧠 Practice Mode", color = Orange, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Joshua Foer's 3-Card Method", color = LightGrey, fontSize = 14.sp)
        Spacer(Modifier.height(24.dp))

        // The 3 cards using 3D images
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            cards.forEach { card ->
                Card(modifier = Modifier.weight(1f).height(160.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Box(modifier = Modifier.fillMaxSize().padding(4.dp), contentAlignment = Alignment.Center) {
                        AsyncImage(
                            model = "file:///android_asset/cards/${card.imageName}.png",
                            contentDescription = card.card,
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = { revealed = !revealed }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Orange, contentColor = Color.Black)) {
            Text(if (revealed) "Hide PAO Story" else "Reveal PAO Story")
        }

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(visible = revealed, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
            Column(modifier = Modifier.fillMaxWidth().background(DarkSurface, RoundedCornerShape(10.dp)).padding(16.dp)) {
                Text("Card 1 (Person): ${cards[0].person}", color = White, fontWeight = FontWeight.Bold)
                Text("Card 2 (Action): ${cards[1].action}", color = White, fontWeight = FontWeight.Bold)
                Text("Card 3 (Object): ${cards[2].objectName}", color = White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Story: ${cards[0].person} is ${cards[1].action} a ${cards[2].objectName}.", color = Green, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = { cards = List(3) { CARD_DATA.random() }; revealed = false }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF555555))) {
            Text("🎲 Shuffle New Hand")
        }
    }
}
