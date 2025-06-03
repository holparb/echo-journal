package com.holparb.echojournal.echoes.presentation.create_echo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

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
            CreateEchoAction.DismissMoodSelector -> {}
            is CreateEchoAction.OnAddTopicTextChange -> {}
            CreateEchoAction.OnCancelClick -> {}
            CreateEchoAction.OnConfirmMood -> {}
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
            is CreateEchoAction.SelectMood -> {}
            CreateEchoAction.OnSelectMoodClick -> {}
        }
    }

}