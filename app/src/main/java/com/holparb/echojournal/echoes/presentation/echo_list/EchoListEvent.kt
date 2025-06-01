package com.holparb.echojournal.echoes.presentation.echo_list

sealed interface EchoListEvent {
    data object RequestAudioPermission: EchoListEvent
    data object RecordingTooShort: EchoListEvent
    data object OnDoneRecording: EchoListEvent
}