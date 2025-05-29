package com.holparb.echojournal.echoes.presentation.echo_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holparb.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.holparb.echojournal.core.presentation.designsystem.theme.bgGradient
import com.holparb.echojournal.echoes.presentation.echo_list.components.EchoFilterRow
import com.holparb.echojournal.echoes.presentation.echo_list.components.EchoList
import com.holparb.echojournal.echoes.presentation.echo_list.components.EchoRecordFloatingActionButton
import com.holparb.echojournal.echoes.presentation.echo_list.components.EchoListTopBar
import com.holparb.echojournal.echoes.presentation.echo_list.components.EmptyEchoList

@Composable
fun EchoListRoot(
    viewModel: EchoListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EchoListScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun EchoListScreen(
    state: EchoListState,
    onAction: (EchoListAction) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            EchoRecordFloatingActionButton(
                onClick = { onAction(EchoListAction.OnFabClick) }
            )
        },
        topBar = {
            EchoListTopBar(
                onSettingsClick = { onAction(EchoListAction.OnSettingsClick) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = MaterialTheme.colorScheme.bgGradient
                )
                .padding(paddingValues)
        ) {
            EchoFilterRow(
                moodChipContent = state.moodChipContent,
                hasActiveMoodFilters = state.hasActiveMoodFilters,
                hasActiveTopicFilters = state.hasActiveTopicFilters,
                selectedEchoFilterChip = state.selectedEchoFilterChip,
                moods = state.moods,
                topicChipTitle = state.topicChipTitle,
                topics = state.topics,
                onAction = onAction,
                modifier = Modifier.fillMaxWidth()
            )
            when {
                state.isLoadingData -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .wrapContentSize(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                !state.hasEchoesRecorded -> {
                    EmptyEchoList(
                        modifier = Modifier.weight(1f)
                    )
                }
                else -> {
                    EchoList(
                        sections = state.echoDaySectionsList,
                        onPlayClick = { EchoListAction.OnPlayEchoClick(it) },
                        onPauseClick = { EchoListAction.OnPauseEchoClick },
                        onTrackSizeAvailable = { trackSizeInfo ->
                            EchoListAction.OnTrackSizeAvailable(trackSizeInfo)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EchoListScreenPreview() {
    EchoJournalTheme {
        EchoListScreen(
            state = EchoListState(isLoadingData = false),
            onAction = {}
        )
    }
}