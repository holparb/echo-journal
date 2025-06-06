package com.holparb.echojournal.echoes.presentation.echo_list

import com.holparb.echojournal.echoes.domain.recording.RecordingDetails

sealed interface EchoListEvent {
    data object RequestAudioPermission: EchoListEvent
    data object RecordingTooShort: EchoListEvent
    data class OnDoneRecording(val recordingDetails: RecordingDetails): EchoListEvent
}