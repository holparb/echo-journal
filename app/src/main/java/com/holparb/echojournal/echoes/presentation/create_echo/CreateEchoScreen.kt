package com.holparb.echojournal.echoes.presentation.create_echo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.holparb.echojournal.R
import com.holparb.echojournal.core.presentation.designsystem.buttons.PrimaryButton
import com.holparb.echojournal.core.presentation.designsystem.buttons.SecondaryButton
import com.holparb.echojournal.core.presentation.designsystem.text_fields.TransparentHintTextField
import com.holparb.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.holparb.echojournal.core.presentation.designsystem.theme.secondary70
import com.holparb.echojournal.core.presentation.designsystem.theme.secondary95
import com.holparb.echojournal.echoes.presentation.components.EchoMoodPlayer
import com.holparb.echojournal.echoes.presentation.create_echo.components.SelectMoodSheet
import com.holparb.echojournal.echoes.presentation.models.MoodUi
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

@Composable
fun CreateEchoRoot(
    viewModel: CreateEchoViewModel = koinViewModel<CreateEchoViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreateEchoScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEchoScreen(
    state: CreateEchoState,
    onAction: (CreateEchoAction) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.new_entry),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(CreateEchoAction.OnNavigateBack)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val descriptionFocusRequester = remember {
            FocusRequester()
        }
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if(state.mood == null) {
                    FilledIconButton(
                        onClick = { onAction(CreateEchoAction.OnSelectMoodClick) },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondary95,
                            contentColor = MaterialTheme.colorScheme.secondary70
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add_mood)
                        )
                    }
                } else {
                    Image(
                        imageVector = ImageVector.vectorResource(state.mood.iconSet.filled),
                        contentDescription = state.mood.title.asString(),
                        modifier = Modifier
                            .height(32.dp)
                            .clickable {
                                onAction(CreateEchoAction.OnSelectMoodClick)
                            },
                        contentScale = ContentScale.FillHeight
                    )
                }

                TransparentHintTextField(
                    text = state.titleText,
                    onValueChange = {
                        onAction(CreateEchoAction.OnTitleChange(it))
                    },
                    hintText = stringResource(R.string.add_title),
                    textStyle = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            descriptionFocusRequester.requestFocus()
                        }
                    )
                )
            }

            EchoMoodPlayer(
                moodUi = state.mood,
                playbackState = state.playbackState,
                playerProgress = { state.durationPlayedRatio },
                durationPlayed = state.durationPlayed,
                totalPlaybackDuration = state.playbackTotalDuration,
                powerRatios = state.playbackAmplitudes,
                onPlayClick = {
                    onAction(CreateEchoAction.OnPlayAudioClick)
                },
                onPauseClick = {
                    onAction(CreateEchoAction.OnPauseAudioClick)
                },
                onTrackSizeAvailable = {
                    onAction(CreateEchoAction.OnTrackSizeAvailable(it))
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = stringResource(R.string.add_description),
                    tint = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.size(16.dp)
                )
                TransparentHintTextField(
                    text = state.noteText,
                    onValueChange = {
                        onAction(CreateEchoAction.OnNoteChange(it))
                    },
                    hintText = stringResource(R.string.add_description),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(descriptionFocusRequester),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.cancel),
                    onClick = {
                        onAction(CreateEchoAction.OnCancelClick)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
                PrimaryButton(
                    text = stringResource(R.string.save),
                    onClick = {
                        onAction(CreateEchoAction.OnSaveClick)
                    },
                    enabled = state.isEchoValid,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if(state.showMoodSelector) {
            SelectMoodSheet(
                selectedMood = state.selectedMoodInSelector,
                onMoodSelected = {
                    onAction(CreateEchoAction.OnMoodClick(it))
                },
                onDismiss = {
                    onAction(CreateEchoAction.OnDismissMoodSelector)
                },
                onCancelClick = {
                    onAction(CreateEchoAction.OnCancelClick)
                },
                onConfirmClick = {
                    onAction(CreateEchoAction.OnConfirmMood)
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    EchoJournalTheme {
        CreateEchoScreen(
            state = CreateEchoState(
                mood = MoodUi.EXCITED,
                titleText = "",
                playbackAmplitudes = (1 .. 20).map { Random.nextFloat() }
            ),
            onAction = {}
        )
    }
}