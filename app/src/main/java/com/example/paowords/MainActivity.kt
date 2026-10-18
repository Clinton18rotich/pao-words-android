package com.example.paowords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.paowords.ui.navigation.WordNavGraph
import com.example.paowords.ui.theme.PAOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PAOTheme {
                WordNavGraph()
            }
        }
    }
}
