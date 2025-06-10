package com.holparb.echojournal.echoes.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.holparb.echojournal.echoes.presentation.models.MoodUi

@Composable
fun MoodSelectorRow(
    selectedMood: MoodUi?,
    onSelectMood: (MoodUi) -> Unit,
    modifier: Modifier = Modifier
) {
    val moods = MoodUi.entries.toList()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        moods.forEach { mood ->
            MoodItem(
                mood = mood,
                onClick = { onSelectMood(mood) },
                selected = mood == selectedMood
            )
        }
    }
}