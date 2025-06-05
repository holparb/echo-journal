package com.holparb.echojournal.echoes.presentation.create_echo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.holparb.echojournal.app.navigation.NavigationRoute
import com.holparb.echojournal.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import com.holparb.echojournal.echoes.domain.recording.RecordingStorage
import com.holparb.echojournal.echoes.presentation.models.MoodUi
import com.holparb.echojournal.echoes.presentation.util.toRecordingDetails
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEchoViewModel(
    private val recordingStorage: RecordingStorage,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val route = savedStateHandle.toRoute<NavigationRoute.CreateEcho>()
    private val recordingDetails = route.toRecordingDetails()

    private val _state = MutableStateFlow(CreateEchoState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeAddTopicText()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateEchoState()
        )

    private val _events = Channel<CreateEchoEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CreateEchoAction) {
        when (action) {
            is CreateEchoAction.OnTitleChange -> onTitleChange(action.text)
            is CreateEchoAction.OnNoteChange -> onNoteChange(action.text)

            is CreateEchoAction.OnAddTopicTextChange -> onAddTopicTextChange(action.text)
            CreateEchoAction.OnDismissTopicSuggestions -> onDismissTopicSuggestions()
            is CreateEchoAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            is CreateEchoAction.OnTopicClick -> onTopicClick(action.topic)

            CreateEchoAction.OnPauseAudioClick -> {}
            CreateEchoAction.OnPlayAudioClick -> {}
            CreateEchoAction.OnSaveClick -> onSaveClick()
            is CreateEchoAction.OnTrackSizeAvailable -> {}

            CreateEchoAction.OnConfirmMood -> onConfirmMood()
            is CreateEchoAction.OnMoodClick -> onMoodClick(action.mood)
            CreateEchoAction.OnSelectMoodClick -> onSelectMoodClick()
            CreateEchoAction.OnDismissMoodSelector -> onDismissMoodSelector()

            CreateEchoAction.OnDismissConfirmLeaveDialog -> onDismissConfirmLeaveDialog()
            CreateEchoAction.OnCancelClick,
            CreateEchoAction.OnNavigateBack,
            CreateEchoAction.OnGoBack -> onShowConfirmLeaveDialog()
        }
    }

    private fun onSaveClick() {
        if(recordingDetails.filePath == null) {
            return
        }

        viewModelScope.launch {
            val savedFilePath = recordingStorage.savePersistently(
                tempFilePath = recordingDetails.filePath
            )
            if(savedFilePath == null) {
                _events.send(CreateEchoEvent.RecordingFileSaveFailed)
                return@launch
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeAddTopicText() {
        state
            .map { it.addTopicText }
            .distinctUntilChanged()
            .debounce(300)
            .onEach { query ->
                _state.update {
                    it.copy(
                        showTopicSuggestions = query.isNotBlank() && query.trim() !in it.topics,
                        searchResults = listOf("Search", "Results").asUnselectedItems()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onShowConfirmLeaveDialog() {
        _state.update {
            it.copy(showConfirmLeaveDialog = true)
        }
    }

    private fun onDismissConfirmLeaveDialog() {
        _state.update {
            it.copy(showConfirmLeaveDialog = false)
        }
    }

    private fun onTitleChange(text: String) {
        _state.update {
            it.copy(
                titleText = text
            )
        }
    }

    private fun onNoteChange(text: String) {
        _state.update {
            it.copy(
                noteText = text
            )
        }
    }

    private fun onTopicClick(topic: String) {
        _state.update {
            it.copy(
                addTopicText = "",
                topics = (it.topics + topic).distinct()
            )
        }
    }

    private fun onRemoveTopicClick(topic: String) {
        _state.update {
            it.copy(
                topics = it.topics - topic
            )
        }
    }

    private fun onDismissTopicSuggestions() {
        _state.update {
            it.copy(showTopicSuggestions = false)
        }
    }

    private fun onAddTopicTextChange(text: String) {
        _state.update {
            it.copy(addTopicText = text.filter { char ->
                char.isLetterOrDigit()
            })
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