package com.holparb.echojournal.echoes.presentation.echo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.echojournal.R
import com.holparb.echojournal.core.presentation.designsystem.dropdowns.Selectable
import com.holparb.echojournal.core.presentation.util.UiText
import com.holparb.echojournal.echoes.domain.recording.VoiceRecorder
import com.holparb.echojournal.echoes.presentation.echo_list.models.AudioCaptureMethod
import com.holparb.echojournal.echoes.presentation.echo_list.models.EchoFilterChip
import com.holparb.echojournal.echoes.presentation.models.MoodChipContent
import com.holparb.echojournal.echoes.presentation.models.MoodUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class EchoListViewModel(
    private val voiceRecorder: VoiceRecorder
) : ViewModel() {

    private var hasLoadedInitialData = false

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
            EchoListAction.OnFabClick -> {
                requestAudioPermission()
                _state.update {
                    it.copy(
                        currentCaptureMethod = AudioCaptureMethod.STANDARD
                    )
                }
            }
            EchoListAction.OnFabLongClick -> {
                requestAudioPermission()
                _state.update {
                    it.copy(
                        currentCaptureMethod = AudioCaptureMethod.QUICK
                    )
                }
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
            EchoListAction.OnPauseEchoClick -> {}
            is EchoListAction.OnPlayEchoClick -> {}
            is EchoListAction.OnTrackSizeAvailable -> {}
            EchoListAction.OnAudioPermissionGranted -> {
                Timber.d("Recording started")
            }
        }
    }

    private fun requestAudioPermission() = viewModelScope.launch {
        _events.send(EchoListEvent.RequestAudioPermission)
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