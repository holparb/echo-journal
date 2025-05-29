@file:OptIn(ExperimentalLayoutApi::class)

package com.holparb.echojournal.echoes.presentation.echo_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.echojournal.core.presentation.designsystem.chips.HashtagChip
import com.holparb.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.holparb.echojournal.core.presentation.util.defaultShadow
import com.holparb.echojournal.echoes.presentation.components.EchoMoodPlayer
import com.holparb.echojournal.echoes.presentation.echo_list.models.TrackSizeInfo
import com.holparb.echojournal.echoes.presentation.models.EchoListItemUi
import com.holparb.echojournal.echoes.presentation.preview.PreviewModels

@Composable
fun EchoListItemCard(
    echoListItemUi: EchoListItemUi,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.defaultShadow(shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = echoListItemUi.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = echoListItemUi.formattedRecordedAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            EchoMoodPlayer(
                moodUi = echoListItemUi.mood,
                playbackState = echoListItemUi.playbackState,
                playerProgress = { echoListItemUi.playbackRatio },
                durationPlayed = echoListItemUi.playbackCurrentDuration,
                totalPlaybackDuration = echoListItemUi.playbackTotalDuration,
                powerRatios = echoListItemUi.amplitudes,
                onPlayClick = onPlayClick,
                onPauseClick = onPauseClick,
                onTrackSizeAvailable = onTrackSizeAvailable
            )
            if(!echoListItemUi.note.isNullOrBlank()) {
                EchoExpandableText(text = echoListItemUi.note)
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                echoListItemUi.topics.forEach { topic ->
                    HashtagChip(text = topic)
                }
            }
        }
    }
}

@Preview
@Composable
private fun EchoListItemCardPreview() {
    EchoJournalTheme {
        EchoListItemCard(
            echoListItemUi = PreviewModels.echoListItemUi,
            onTrackSizeAvailable = {},
            onPauseClick = {},
            onPlayClick = {},
            modifier = Modifier.width(250.dp)
        )
    }
}