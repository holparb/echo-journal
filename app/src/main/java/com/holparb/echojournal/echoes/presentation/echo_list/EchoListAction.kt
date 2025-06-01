package com.holparb.echojournal.echoes.presentation.echo_list

import com.holparb.echojournal.echoes.presentation.echo_list.models.EchoFilterChip
import com.holparb.echojournal.echoes.presentation.echo_list.models.TrackSizeInfo
import com.holparb.echojournal.echoes.presentation.models.MoodUi

sealed interface EchoListAction {
    data object OnMoodChipClick: EchoListAction
    data object OnDismissMoodDropdown: EchoListAction
    data object OnDismissTopicDropdown: EchoListAction
    data class OnFilterByMoodClick(val moodUi: MoodUi): EchoListAction
    data class OnFilterByTopicClick(val topic: String): EchoListAction
    data object OnTopicChipClick: EchoListAction
    data class OnRemoveFilters(val filterType: EchoFilterChip): EchoListAction
    data object OnFabClick: EchoListAction
    data object OnFabLongClick: EchoListAction
    data object OnSettingsClick: EchoListAction
    data class OnPlayEchoClick(val echoId: Int): EchoListAction
    data object OnPauseEchoClick: EchoListAction
    data class OnTrackSizeAvailable(val trackSizeInfo: TrackSizeInfo): EchoListAction
    data object OnAudioPermissionGranted: EchoListAction
    data object OnCancelRecording: EchoListAction
    data object OnResumeRecordingClick: EchoListAction
    data object OnPauseRecordingClick: EchoListAction
    data object OnCompleteRecording: EchoListAction
}