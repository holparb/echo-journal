package com.holparb.echojournal.echoes.presentation.create_echo

import com.holparb.echojournal.echoes.presentation.models.MoodUi
import com.holparb.echojournal.echoes.presentation.models.PlaybackState
import kotlin.time.Duration

data class CreateEchoState(
    val titleText: String = "",
    val addTopicText: String = "",
    val noteText: String = "",
    val showMoodSelector: Boolean = true,
    val selectedMoodInSelector: MoodUi = MoodUi.NEUTRAL,
    val showTopicSuggestions: Boolean = false,
    val mood: MoodUi? = null,
    val searchResults: List<String> = emptyList(),
    val showCreateTopicOption: Boolean = false,
    val isEchoValid: Boolean = false,
    val playbackAmplitudes: List<Float> = emptyList(),
    val playbackTotalDuration: Duration = Duration.ZERO,
    val durationPlayed: Duration = Duration.ZERO,
    val playbackState: PlaybackState = PlaybackState.STOPPED
) {
    val durationPlayedRatio = (durationPlayed / playbackTotalDuration).toFloat()
}