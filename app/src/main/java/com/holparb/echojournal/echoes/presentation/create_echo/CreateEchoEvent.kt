package com.holparb.echojournal.echoes.presentation.create_echo

sealed interface CreateEchoEvent {
    data object RecordingFileSaveFailed: CreateEchoEvent
    data object EchoSuccessfullySaved: CreateEchoEvent
}