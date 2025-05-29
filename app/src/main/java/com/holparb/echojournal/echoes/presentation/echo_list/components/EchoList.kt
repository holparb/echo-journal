package com.holparb.echojournal.echoes.presentation.echo_list.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.holparb.echojournal.core.presentation.util.UiText
import com.holparb.echojournal.echoes.presentation.echo_list.models.EchoDaySection
import com.holparb.echojournal.echoes.presentation.echo_list.models.ListItemRelativePosition
import com.holparb.echojournal.echoes.presentation.echo_list.models.TrackSizeInfo
import com.holparb.echojournal.echoes.presentation.preview.PreviewModels
import java.time.Instant
import java.time.ZonedDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EchoList(
    sections: List<EchoDaySection>,
    onPlayClick: (echoId: Int) -> Unit,
    onPauseClick: () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        sections.forEachIndexed { sectionIndex, (dateHeader, echoes) ->
            stickyHeader {
                if(sectionIndex > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Text(
                    text = dateHeader.asString(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            itemsIndexed(
                items = echoes,
                key = { _, echo -> echo.id }
            ) { index, echo ->
                EchoListItem(
                    echoListItemUi = echo,
                    listItemRelativePosition = when {
                        index == 0 && echoes.size == 1 -> ListItemRelativePosition.SINGLE_ENTRY
                        index == 0 -> ListItemRelativePosition.FIRST
                        echoes.lastIndex == index -> ListItemRelativePosition.LAST
                        else -> ListItemRelativePosition.IN_BETWEEN
                    },
                    onPlayClick = { onPlayClick(echo.id) },
                    onPauseClick = onPauseClick,
                    onTrackSizeAvailable = onTrackSizeAvailable
                )
            }
        }
    }
}

@Preview
@Composable
private fun EchoListPreview() {
    val todaysEchos = remember {
        (1..3).map {
            PreviewModels.echoListItemUi.copy(
                id = it,
                recordedAt = Instant.now()
            )
        }
    }
    val yesterdaysEchos = remember {
        (4..6).map {
            PreviewModels.echoListItemUi.copy(
                id = it,
                recordedAt = ZonedDateTime.now().minusDays(1).toInstant()
            )
        }
    }
    val echosFrom2DaysAgo = remember {
        (7..9).map {
            PreviewModels.echoListItemUi.copy(
                id = it,
                recordedAt = ZonedDateTime.now().minusDays(2).toInstant()
            )
        }
    }
    val sections = remember {
        listOf(
            EchoDaySection(
                dateHeader = UiText.Dynamic("Today"),
                echoes = todaysEchos
            ),
            EchoDaySection(
                dateHeader = UiText.Dynamic("Yesterday"),
                echoes = yesterdaysEchos
            ),
            EchoDaySection(
                dateHeader = UiText.Dynamic("2025/05/27"),
                echoes = echosFrom2DaysAgo
            ),
        )
    }
    EchoJournalTheme {
        EchoList(
            sections = sections,
            onPlayClick = {},
            onPauseClick = {},
            onTrackSizeAvailable = {}
        )
    }
}