package com.holparb.echojournal.echoes.presentation.echo_list

sealed interface EchoListEvent {
    data object RequestAudioPermission: EchoListEvent
}