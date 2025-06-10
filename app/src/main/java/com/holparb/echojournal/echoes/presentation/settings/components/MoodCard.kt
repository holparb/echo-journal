package com.holparb.echojournal.echoes.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.echojournal.R
import com.holparb.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.holparb.echojournal.echoes.presentation.components.MoodSelectorRow
import com.holparb.echojournal.echoes.presentation.models.MoodUi

@Composable
fun MoodCard(
    selectedMood: MoodUi?,
    onSelectMood: (MoodUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.my_mood),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = stringResource(R.string.select_default_mood_to_apply_to_all_new_entries),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        MoodSelectorRow(
            selectedMood = selectedMood,
            onSelectMood = onSelectMood,
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Preview
@Composable
private fun MoodCardPreview() {
    EchoJournalTheme {
        MoodCard(
            selectedMood = MoodUi.EXCITED,
            onSelectMood = {},
        )
    }
}