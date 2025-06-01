package com.holparb.echojournal.echoes.presentation.echo_list.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.echojournal.R
import com.holparb.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.holparb.echojournal.core.presentation.designsystem.theme.Microphone
import com.holparb.echojournal.core.presentation.designsystem.theme.Pause
import com.holparb.echojournal.core.presentation.designsystem.theme.buttonGradient
import com.holparb.echojournal.core.presentation.designsystem.theme.pressedButtonGradient
import com.holparb.echojournal.core.presentation.designsystem.theme.primary90
import com.holparb.echojournal.core.presentation.designsystem.theme.primary95

private const val PRIMARY_BUTTON_BUBBLE_SIZE_DP = 128
private const val SECONDARY_BUTTON_SIZE_DP = 48

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EchoRecordingSheet(
    formattedEchoDuration: String,
    isRecording: Boolean,
    onDismiss: () -> Unit,
    onResumeClick: () -> Unit,
    onPauseClick: () -> Unit,
    onCompleteRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        SheetContent(
            formattedEchoDuration = formattedEchoDuration,
            isRecording = isRecording,
            onDismiss = onDismiss,
            onResumeClick = onResumeClick,
            onPauseClick = onPauseClick,
            onCompleteRecording = onCompleteRecording,
            modifier = modifier
        )
    }
}

@Composable
fun SheetContent(
    formattedEchoDuration: String,
    isRecording: Boolean,
    onDismiss: () -> Unit,
    onResumeClick: () -> Unit,
    onPauseClick: () -> Unit,
    onCompleteRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryBubbleSize = PRIMARY_BUTTON_BUBBLE_SIZE_DP.dp
    val secondaryButtonSize = SECONDARY_BUTTON_SIZE_DP.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if(isRecording) {
                    stringResource(R.string.recording_your_memories)
                } else {
                    stringResource(R.string.recording_paused)
                },
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = formattedEchoDuration,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFeatureSettings = "tnum",
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.defaultMinSize(minWidth = 100.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledIconButton(
                onClick = onDismiss,
                modifier = Modifier.size(secondaryButtonSize),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cancel_recording)
                )
            }

            val interactionSource = remember {
                MutableInteractionSource()
            }
            val isPressed by interactionSource.collectIsPressedAsState()
            Box(
                modifier = Modifier
                    .size(primaryBubbleSize)
                    .background(
                        color = if(isRecording) {
                            MaterialTheme.colorScheme.primary95
                        } else {
                            Color.Transparent
                        },
                        shape = CircleShape
                    )
                    .padding(10.dp)
                    .background(
                        color = if(isRecording) {
                            MaterialTheme.colorScheme.primary90
                        } else {
                            Color.Transparent
                        },
                        shape = CircleShape
                    )
                    .padding(16.dp)
                    .background(
                        brush = if(isPressed) {
                            MaterialTheme.colorScheme.pressedButtonGradient
                        } else {
                            MaterialTheme.colorScheme.buttonGradient
                        },
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current,
                        onClick = if(isRecording) onCompleteRecording else onResumeClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if(isRecording) {
                        Icons.Default.Check
                    } else {
                        Icons.Filled.Microphone
                    },
                    contentDescription = if(isRecording) {
                        stringResource(R.string.finish_recording)
                    } else {
                        stringResource(R.string.start_recording)
                    },
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            FilledIconButton(
                onClick = if(isRecording) onPauseClick else onCompleteRecording,
                modifier = Modifier.size(secondaryButtonSize),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if(isRecording) {
                        Icons.Filled.Pause
                    } else {
                        Icons.Default.Check
                    },
                    contentDescription = if(isRecording) {
                        stringResource(R.string.pause_recording)
                    } else {
                        stringResource(R.string.finish_recording)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun SheetContentPreview() {
    EchoJournalTheme {
        SheetContent(
            formattedEchoDuration = "00:12:23",
            isRecording = false,
            onDismiss = {},
            onPauseClick = {},
            onResumeClick = {},
            onCompleteRecording = {}
        )
    }
}