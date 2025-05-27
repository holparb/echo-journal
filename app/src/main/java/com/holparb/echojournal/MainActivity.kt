package com.holparb.echojournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.holparb.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.holparb.echojournal.echoes.presentation.echoes_list.EchoesListRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EchoJournalTheme {
                EchoesListRoot()
            }
        }
    }
}