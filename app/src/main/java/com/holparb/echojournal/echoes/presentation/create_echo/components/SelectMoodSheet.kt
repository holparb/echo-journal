package com.holparb.echojournal.echoes.presentation.create_echo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.echojournal.R
import com.holparb.echojournal.core.presentation.designsystem.buttons.PrimaryButton
import com.holparb.echojournal.core.presentation.designsystem.buttons.SecondaryButton
import com.holparb.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.holparb.echojournal.echoes.presentation.components.MoodSelectorRow
import com.holparb.echojournal.echoes.presentation.models.MoodUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMoodSheet(
    onSelectMood: (MoodUi) -> Unit,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedMood: MoodUi? = null,
) {
    val moods = MoodUi.entries.toList()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.how_are_you_doing),
                style = MaterialTheme.typography.headlineMedium
            )
            MoodSelectorRow(
                selectedMood = selectedMood,
                onSelectMood = onSelectMood
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecondaryButton(
                    text = stringResource(R.string.cancel),
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxHeight()
                )
                PrimaryButton(
                    text = stringResource(R.string.confirm),
                    onClick = {
                        if(selectedMood != null) {
                            onConfirmClick()
                        }
                    },
                    enabled = selectedMood != null,
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.confirm)
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectMoodSheetPreview() {
    EchoJournalTheme {
        SelectMoodSheet(
            onSelectMood = {},
            onDismiss = {},
            onConfirmClick = {}
        )
    }
}