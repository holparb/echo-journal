package com.holparb.echojournal.echoes.presentation.echo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.echojournal.R
import com.holparb.echojournal.core.presentation.designsystem.dropdowns.Selectable
import com.holparb.echojournal.core.presentation.util.UiText
import com.holparb.echojournal.echoes.domain.audio.AudioPlayer
import com.holparb.echojournal.echoes.domain.recording.VoiceRecorder
import com.holparb.echojournal.echoes.presentation.echo_list.models.AudioCaptureMethod
import com.holparb.echojournal.echoes.presentation.echo_list.models.EchoFilterChip
import com.holparb.echojournal.echoes.presentation.echo_list.models.RecordingState
import com.holparb.echojournal.echoes.presentation.echo_list.models.TrackSizeInfo
import com.holparb.echojournal.echoes.presentation.models.MoodChipContent
import com.holparb.echojournal.echoes.presentation.models.MoodUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class EchoListViewModel(
    private val voiceRecorder: VoiceRecorder,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    companion object {
        private val MIN_RECORDING_DURATION = 1.5.seconds
    }

    private var hasLoadedInitialData = false

    private val playingEchoId = MutableStateFlow<Int?>(null)

    private val _state = MutableStateFlow(EchoListState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeFilters()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EchoListState()
        )

    private val selectedMoodFilters = MutableStateFlow<List<MoodUi>>(emptyList())
    private val selectedTopicFilters = MutableStateFlow<List<String>>(emptyList())

    private val _events = Channel<EchoListEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: EchoListAction) {
        when (action) {
            EchoListAction.OnRecordFabClick -> {
                requestAudioPermission()
                _state.update {
                    it.copy(
                        currentCaptureMethod = AudioCaptureMethod.STANDARD
                    )
                }
            }
            EchoListAction.OnRequestPermissionQuickRecording -> {
                requestAudioPermission()
                _state.update {
                    it.copy(
                        currentCaptureMethod = AudioCaptureMethod.QUICK
                    )
                }
            }
            EchoListAction.OnRecordFabLongClick -> {
                startRecording(AudioCaptureMethod.QUICK)
            }
            EchoListAction.OnMoodChipClick -> {
                _state.update {
                    it.copy(selectedEchoFilterChip = EchoFilterChip.MOODS)
                }
            }
            EchoListAction.OnTopicChipClick -> {
                _state.update {
                    it.copy(selectedEchoFilterChip = EchoFilterChip.TOPICS)
                }
            }
            is EchoListAction.OnRemoveFilters -> {
                when(action.filterType) {
                    EchoFilterChip.MOODS -> selectedMoodFilters.update { emptyList() }
                    EchoFilterChip.TOPICS -> selectedTopicFilters.update { emptyList() }
                }
            }
            EchoListAction.OnDismissMoodDropdown,
            EchoListAction.OnDismissTopicDropdown -> {
                _state.update {
                    it.copy(selectedEchoFilterChip = null)
                }
            }
            is EchoListAction.OnFilterByMoodClick -> {
                toggleMoodFilter(action.moodUi)
            }
            is EchoListAction.OnFilterByTopicClick -> {
                toggleTopicFilter(action.topic)
            }
            EchoListAction.OnSettingsClick -> {}
            EchoListAction.OnPauseEchoClick -> onPauseEchoClick()
            is EchoListAction.OnPlayEchoClick -> onPlayEchoClick(action.echoId)
            is EchoListAction.OnTrackSizeAvailable -> onTrackSizeAvailable(action.trackSizeInfo)

            EchoListAction.OnAudioPermissionGranted -> {
                startRecording(audioCaptureMethod = AudioCaptureMethod.STANDARD)
            }
            EchoListAction.OnCancelRecording -> cancelRecording()
            EchoListAction.OnCompleteRecording -> stopRecording()
            EchoListAction.OnPauseRecordingClick -> pauseRecording()
            EchoListAction.OnResumeRecordingClick -> resumeRecording()
        }
    }

    private fun requestAudioPermission() = viewModelScope.launch {
        _events.send(EchoListEvent.RequestAudioPermission)
    }

    private fun onTrackSizeAvailable(trackSizeInfo: TrackSizeInfo) {

    }

    private fun onPlayEchoClick(echoId: Int) {
        val selectedEcho = state.value.echoes.values.flatten().first { it.id == echoId }
        val activeTrack = audioPlayer.activeTrack.value
        val isNewEcho = playingEchoId.value != echoId
        val isSameEchoPlayingFromBeginning = echoId == playingEchoId.value && activeTrack != null
                && activeTrack.durationPlayed == Duration.ZERO

        when {
            isNewEcho || isSameEchoPlayingFromBeginning -> {
                playingEchoId.update { echoId }
                audioPlayer.stop()
                audioPlayer.play(
                    filePath = selectedEcho.audioFilePath,
                    onComplete = ::completePlayback
                )
            }
            else -> audioPlayer.resume()
        }
    }

    private fun completePlayback() {
        _state.update {
            it.copy(
                echoes = it.echoes.mapValues { (_, echoes) ->
                    echoes.map { echo ->
                        echo.copy(
                            playbackCurrentDuration = Duration.ZERO
                        )
                    }
                }
            )
        }
        playingEchoId.update { null }
    }

    private fun onPauseEchoClick() {
        audioPlayer.pause()
    }

    private fun startRecording(audioCaptureMethod: AudioCaptureMethod) {
        _state.update {
            it.copy(
                recordingState = when(audioCaptureMethod) {
                    AudioCaptureMethod.STANDARD -> RecordingState.NORMAL_RECORDING
                    AudioCaptureMethod.QUICK -> RecordingState.QUICK_RECORDING
                }
            )
        }

        voiceRecorder.start()

        if(audioCaptureMethod == AudioCaptureMethod.STANDARD) {
            voiceRecorder
                .recordingDetails
                .distinctUntilChangedBy { it.duration }
                .map { it.duration }
                .onEach { duration ->
                    _state.update {
                        it.copy(
                            recordingElapsedDuration = duration
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun pauseRecording() {
        voiceRecorder.pause()
        _state.update {
            it.copy(
                recordingState = RecordingState.PAUSED
            )
        }
    }

    private fun resumeRecording() {
        voiceRecorder.resume()
        _state.update {
            it.copy(
                recordingState = RecordingState.NORMAL_RECORDING
            )
        }
    }

    private fun cancelRecording() {
        _state.update {
            it.copy(
                recordingState = RecordingState.NOT_RECORDING
            )
        }
        voiceRecorder.cancel()
    }

    private fun stopRecording() {
        voiceRecorder.stop()
        _state.update {
            it.copy(
                recordingState = RecordingState.NOT_RECORDING
            )
        }

        val recordingDetails = voiceRecorder.recordingDetails.value
        viewModelScope.launch {
            if(recordingDetails.duration < MIN_RECORDING_DURATION) {
                _events.send(EchoListEvent.RecordingTooShort)
            } else {
                _events.send(EchoListEvent.OnDoneRecording(recordingDetails))
            }
        }
    }

    private fun toggleMoodFilter(moodUi: MoodUi) {
        selectedMoodFilters.update { selectedMoods ->
            if(moodUi in selectedMoods) {
                selectedMoods - moodUi
            } else {
                selectedMoods + moodUi
            }
        }
    }

    private fun toggleTopicFilter(topic: String) {
        selectedTopicFilters.update { selectedTopics ->
            if(topic in selectedTopics) {
                selectedTopics - topic
            } else {
                selectedTopics + topic
            }
        }
    }

    private fun observeFilters() {
        combine(
            selectedTopicFilters,
            selectedMoodFilters
        ) {
            selectedTopics, selectedMoods ->
            _state.update {
                it.copy(
                    // Hardcode topics in state for now, later they will come from the db
                    topics = it.topics.map { selectableTopic ->
                        Selectable(
                            item = selectableTopic.item,
                            selected = selectedTopics.contains(selectableTopic.item)
                        )
                    },
                    moods = MoodUi.entries.map { moodUi ->
                        Selectable(
                            item = moodUi,
                            selected = selectedMoods.contains(moodUi)
                        )
                    },
                    hasActiveMoodFilters = selectedMoods.isNotEmpty(),
                    hasActiveTopicFilters = selectedTopics.isNotEmpty(),
                    topicChipTitle = selectedTopics.deriveTopicsToText(),
                    moodChipContent = selectedMoods.asMoodChipContent()
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun List<String>.deriveTopicsToText(): UiText {
        return when(size) {
            0 -> UiText.StringResource(R.string.all_topics)
            1 -> UiText.Dynamic(this.first())
            2 -> UiText.Dynamic("${this.first()}, ${this.last()}")
            else -> {
                val extraElementCount = size - 2
                UiText.Dynamic("${this.first()}, ${this[1]} +$extraElementCount")
            }
        }
    }

    private fun List<MoodUi>.asMoodChipContent(): MoodChipContent {
        if(this.isEmpty()) {
            return MoodChipContent()
        }
        val icons = this.map { it.iconSet.filled }
        val moodNames = this.map { it.title }

        return when(size) {
            1 -> MoodChipContent(iconsRes = icons, title = moodNames.first())
            2 -> MoodChipContent(iconsRes = icons, title = UiText.Combined(
                format = "%s, %s",
                uiTexts = moodNames.toTypedArray())
            )
            else -> {
                val extraElementCount = size - 2
                MoodChipContent(
                    iconsRes = icons,
                    title = UiText.Combined(
                        format = "%s, %s +$extraElementCount",
                        uiTexts = moodNames.take(2).toTypedArray()
                    )
                )
            }
        }
    }
}