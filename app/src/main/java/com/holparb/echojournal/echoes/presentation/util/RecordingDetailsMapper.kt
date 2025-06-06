package com.holparb.echojournal.echoes.presentation.util

import com.holparb.echojournal.app.navigation.NavigationRoute
import com.holparb.echojournal.echoes.domain.recording.RecordingDetails
import kotlin.time.Duration.Companion.milliseconds

fun RecordingDetails.toCreateEchoRoute(): NavigationRoute.CreateEcho {
    return NavigationRoute.CreateEcho(
        recordingPath = this.filePath ?: throw IllegalArgumentException("Recording cannot be found"),
        duration = this.duration.inWholeMilliseconds,
        amplitudes = this.amplitudes.joinToString(";")
    )
}

fun NavigationRoute.CreateEcho.toRecordingDetails(): RecordingDetails {
    return RecordingDetails(
        filePath = this.recordingPath,
        duration = this.duration.milliseconds,
        amplitudes = this.amplitudes.split(";").map { it.toFloat() }
    )
}