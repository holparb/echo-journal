package com.holparb.echojournal.echoes.presentation.create_echo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.holparb.echojournal.app.navigation.NavigationRoute
import com.holparb.echojournal.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import com.holparb.echojournal.echoes.domain.audio.AudioPlayer
import com.holparb.echojournal.echoes.domain.data_source.Echo
import com.holparb.echojournal.echoes.domain.data_source.EchoDataSource
import com.holparb.echojournal.echoes.domain.data_source.Mood
import com.holparb.echojournal.echoes.domain.recording.RecordingStorage
import com.holparb.echojournal.echoes.domain.settings.SettingsPreferences
import com.holparb.echojournal.echoes.presentation.echo_list.models.TrackSizeInfo
import com.holparb.echojournal.echoes.presentation.models.MoodUi
import com.holparb.echojournal.echoes.presentation.models.PlaybackState
import com.holparb.echojournal.echoes.presentation.util.AmplitudeNormalizer
import com.holparb.echojournal.echoes.presentation.util.toRecordingDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import kotlin.time.Duration

class CreateEchoViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val recordingStorage: RecordingStorage,
    private val audioPlayer: AudioPlayer,
    private val echoDataSource: EchoDataSource,
    private val settingsPreferences: SettingsPreferences
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val route = savedStateHandle.toRoute<NavigationRoute.CreateEcho>()
    private val recordingDetails = route.toRecordingDetails()

    private val restoredTopics = savedStateHandle.get<String>("topics")?.split(",")
    private val _state = MutableStateFlow(CreateEchoState(
        playbackTotalDuration = recordingDetails.duration,
        titleText = savedStateHandle["titleText"] ?: "",
        noteText = savedStateHandle["noteText"] ?: "",
        topics = restoredTopics ?: emptyList(),
        mood = savedStateHandle.get<String>("mood")?.let { MoodUi.valueOf(it) },
        showMoodSelector = savedStateHandle.get<String>("mood") == null,
        isEchoValid = savedStateHandle.get<Boolean>("isEchoValid") == true
    ))
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeAddTopicText()
                fetchDefaultSettings()
                hasLoadedInitialData = true
            }
        }
        .onEach { state ->
            savedStateHandle["titleText"] = state.titleText
            savedStateHandle["mood"] = state.mood?.name
            savedStateHandle["noteText"] = state.noteText
            savedStateHandle["topics"] = state.topics.joinToString(",")
            savedStateHandle["isEchoValid"] = state.isEchoValid
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateEchoState()
        )

    private val _events = Channel<CreateEchoEvent>()
    val events = _events.receiveAsFlow()

    private var durationJob: Job? = null

    fun onAction(action: CreateEchoAction) {
        when (action) {
            is CreateEchoAction.OnTitleChange -> onTitleChange(action.text)
            is CreateEchoAction.OnNoteChange -> onNoteChange(action.text)

            is CreateEchoAction.OnAddTopicTextChange -> onAddTopicTextChange(action.text)
            CreateEchoAction.OnDismissTopicSuggestions -> onDismissTopicSuggestions()
            is CreateEchoAction.OnRemoveTopicClick -> onRemoveTopicClick(action.topic)
            is CreateEchoAction.OnTopicClick -> onTopicClick(action.topic)

            CreateEchoAction.OnPauseAudioClick -> onPauseAudioClick()
            CreateEchoAction.OnPlayAudioClick -> onPlayAudioClick()
            CreateEchoAction.OnSaveClick -> onSaveClick()
            is CreateEchoAction.OnTrackSizeAvailable -> onTrackSizeAvailable(action.trackSizeInfo)

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

    private fun fetchDefaultSettings() {
        settingsPreferences
            .observeDefaultMood()
            .take(1)
            .onEach { defaultMood ->
                val moodUi = MoodUi.valueOf(defaultMood.name)
                _state.update {
                    it.copy(
                        selectedMoodInSelector = moodUi,
                        mood = moodUi,
                        showMoodSelector = false
                    )
                }
            }
            .launchIn(viewModelScope)

        settingsPreferences
            .observeDefaultTopics()
            .take(1)
            .onEach { defaultTopic ->
                _state.update {
                    it.copy(
                        topics = defaultTopic
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onPlayAudioClick() {
        if(state.value.playbackState == PlaybackState.PAUSED) {
            audioPlayer.resume()
        } else {
            audioPlayer.play(
                filePath = recordingDetails.filePath ?: throw IllegalArgumentException(
                    "File path cannot be null"
                ),
                onComplete = {
                    _state.update {
                        it.copy(
                            playbackState = PlaybackState.STOPPED,
                            durationPlayed = Duration.ZERO
                        )
                    }
                }
            )

            durationJob = audioPlayer
                .activeTrack
                .filterNotNull()
                .onEach { track ->
                    _state.update {
                        it.copy(
                            playbackState = if(track.isPlaying) {
                                PlaybackState.PLAYING
                            } else {
                                PlaybackState.PAUSED
                            },
                            durationPlayed = track.durationPlayed,
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun onPauseAudioClick() {
        audioPlayer.pause()
    }

    private fun onSaveClick() {
        if(recordingDetails.filePath == null || !state.value.isEchoValid) {
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

            val currentState = state.value
            val echo = Echo(
                mood = currentState.mood?.let {
                    Mood.valueOf(it.name)
                } ?: throw IllegalStateException("Mood is null"),
                title = currentState.titleText.trim(),
                note = currentState.noteText.ifBlank { null },
                topics = currentState.topics,
                audioAmplitudes = recordingDetails.amplitudes,
                audioFilePath = savedFilePath,
                audioPlaybackLength = currentState.playbackTotalDuration,
                recordedAt = Instant.now()
            )

            echoDataSource.insertEcho(echo)
            _events.send(CreateEchoEvent.EchoSuccessfullySaved)
        }
    }

    private fun onTrackSizeAvailable(trackSizeInfo: TrackSizeInfo) {
        viewModelScope.launch(Dispatchers.Default) {
            val finalAmplitudes = AmplitudeNormalizer.normalize(
                sourceAmplitudes = recordingDetails.amplitudes,
                trackWidth = trackSizeInfo.trackWidth,
                barWidth = trackSizeInfo.barWidth,
                spacing = trackSizeInfo.spacing
            )

            _state.update {
                it.copy(
                    playbackAmplitudes = finalAmplitudes
                )
            }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeAddTopicText() {
        state
            .map { it.addTopicText }
            .distinctUntilChanged()
            .debounce(300)
            .filter { query ->
                query.isNotBlank()
            }
            .flatMapLatest { query ->
                _state.update {
                    it.copy(
                        showTopicSuggestions = query.trim() !in it.topics,
                    )
                }
                echoDataSource.searchTopics(query)
            }
            .onEach { searchResults ->
                _state.update {
                    it.copy(
                        searchResults = searchResults.asUnselectedItems()
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
                titleText = text,
                isEchoValid = text.isNotBlank() && it.mood != null
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
                topics = (it.topics + topic).distinct(),
                showCreateTopicOption = false
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
            it.copy(
                addTopicText = text.filter { char ->
                    char.isLetterOrDigit()
                },
                showCreateTopicOption = text.isNotBlank()
            )
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