package com.holparb.echojournal.echoes.presentation.create_echo

import com.holparb.echojournal.echoes.presentation.echo_list.models.TrackSizeInfo
import com.holparb.echojournal.echoes.presentation.models.MoodUi

sealed interface CreateEchoAction {
    data object OnNavigateBack: CreateEchoAction
    data class OnTitleChange(val text: String): CreateEchoAction
    data class OnNoteChange(val text: String): CreateEchoAction
    data class OnAddTopicTextChange(val text: String): CreateEchoAction
    data object OnSelectMoodClick: CreateEchoAction
    data object OnDismissMoodSelector: CreateEchoAction
    data class OnMoodClick(val mood: MoodUi): CreateEchoAction
    data object OnConfirmMood: CreateEchoAction
    data class OnTopicClick(val topic: String): CreateEchoAction
    data object OnDismissTopicSuggestions: CreateEchoAction
    data object OnCancelClick: CreateEchoAction
    data object OnSaveClick: CreateEchoAction
    data object OnPlayAudioClick: CreateEchoAction
    data object OnPauseAudioClick: CreateEchoAction
    data class OnTrackSizeAvailable(val trackSizeInfo: TrackSizeInfo): CreateEchoAction
    data class OnRemoveTopicClick(val topic: String): CreateEchoAction
    data object OnGoBack: CreateEchoAction
    data object OnDismissConfirmLeaveDialog: CreateEchoAction
}