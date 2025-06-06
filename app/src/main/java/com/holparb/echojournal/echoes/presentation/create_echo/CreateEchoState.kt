package com.holparb.echojournal.echoes.presentation.create_echo

import com.holparb.echojournal.core.presentation.designsystem.dropdowns.Selectable
import com.holparb.echojournal.echoes.presentation.models.MoodUi
import com.holparb.echojournal.echoes.presentation.models.PlaybackState
import kotlin.time.Duration

data class CreateEchoState(
    val titleText: String = "",
    val topics: List<String> = listOf("Work", "Love"),
    val addTopicText: String = "",
    val noteText: String = "",
    val showMoodSelector: Boolean = true,
    val selectedMoodInSelector: MoodUi? = null,
    val showTopicSuggestions: Boolean = false,
    val mood: MoodUi? = null,
    val searchResults: List<Selectable<String>> = emptyList(),
    val showCreateTopicOption: Boolean = false,
    val isEchoValid: Boolean = false,
    val playbackAmplitudes: List<Float> = emptyList(),
    val playbackTotalDuration: Duration = Duration.ZERO,
    val durationPlayed: Duration = Duration.ZERO,
    val playbackState: PlaybackState = PlaybackState.STOPPED,
    val showConfirmLeaveDialog: Boolean = false
) {
    val durationPlayedRatio = (durationPlayed / playbackTotalDuration).toFloat()
}