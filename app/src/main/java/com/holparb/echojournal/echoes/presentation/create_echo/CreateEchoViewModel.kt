package com.holparb.echojournal.echoes.presentation.create_echo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.echojournal.echoes.presentation.models.MoodUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CreateEchoViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(CreateEchoState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateEchoState()
        )

    fun onAction(action: CreateEchoAction) {
        when (action) {
            CreateEchoAction.OnDismissMoodSelector -> onDismissMoodSelector()
            is CreateEchoAction.OnAddTopicTextChange -> {}
            CreateEchoAction.OnCancelClick -> {}
            CreateEchoAction.OnConfirmMood -> onConfirmMood()
            CreateEchoAction.OnCreateNewTopicClick -> {}
            CreateEchoAction.OnDismissTopicSuggestions -> {}
            CreateEchoAction.OnNavigateBack -> {}
            is CreateEchoAction.OnNoteChange -> {}
            CreateEchoAction.OnPauseAudioClick -> {}
            CreateEchoAction.OnPlayAudioClick -> {}
            is CreateEchoAction.OnRemoveTopicClick -> {}
            CreateEchoAction.OnSaveClick -> {}
            is CreateEchoAction.OnTitleChange -> {}
            is CreateEchoAction.OnTopicClick -> {}
            is CreateEchoAction.OnTrackSizeAvailable -> {}
            is CreateEchoAction.OnMoodClick -> onMoodClick(action.mood)
            CreateEchoAction.OnSelectMoodClick -> onSelectMoodClick()
        }
    }

    private fun onSelectMoodClick() {
        _state.update {
            it.copy(
                showMoodSelector = true
            )
        }
    }

    private fun onDismissMoodSelector() {
        _state.update {
            it.copy(
                showMoodSelector = false
            )
        }
    }

    private fun onMoodClick(mood: MoodUi) {
        _state.update {
            it.copy(
                selectedMoodInSelector = mood
            )
        }
    }

    private fun onConfirmMood() {
        _state.update {
            it.copy(
                mood = it.selectedMoodInSelector,
                isEchoValid = it.titleText.isNotBlank(),
                showMoodSelector = false
            )
        }
    }

}