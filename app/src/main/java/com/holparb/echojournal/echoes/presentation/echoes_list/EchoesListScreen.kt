package com.holparb.echojournal.echoes.presentation.echoes_list

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
import com.holparb.echojournal.echoes.presentation.echoes_list.components.EchoRecordFloatingActionButton
import com.holparb.echojournal.echoes.presentation.echoes_list.components.EchoesListTopBar
import com.holparb.echojournal.echoes.presentation.echoes_list.components.EmptyEchoesList

@Composable
fun EchoesListRoot(
    viewModel: EchoesListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EchoesListScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun EchoesListScreen(
    state: EchoesListState,
    onAction: (EchoesListAction) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            EchoRecordFloatingActionButton(
                onClick = { onAction(EchoesListAction.OnFabClick) }
            )
        },
        topBar = {
            EchoesListTopBar(
                onSettingsClick = { onAction(EchoesListAction.OnSettingsClick) }
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
                state.hasEchoesRecorded.not() -> {
                    EmptyEchoesList(
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    EchoJournalTheme {
        EchoesListScreen(
            state = EchoesListState(isLoadingData = false),
            onAction = {}
        )
    }
}