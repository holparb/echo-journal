package com.holparb.echojournal.echoes.presentation.util

import com.holparb.echojournal.echoes.domain.data_source.Echo
import com.holparb.echojournal.echoes.presentation.models.EchoListItemUi
import com.holparb.echojournal.echoes.presentation.models.MoodUi
import com.holparb.echojournal.echoes.presentation.models.PlaybackState
import kotlin.time.Duration

fun Echo.toEchoListItemUi(
    currentPlaybackDuration: Duration = Duration.ZERO,
    playbackState: PlaybackState = PlaybackState.STOPPED
): EchoListItemUi {
    return EchoListItemUi(
        id = id!!,
        title = title,
        note = note,
        mood = MoodUi.valueOf(mood.name),
        recordedAt = recordedAt,
        topics = topics,
        amplitudes = audioAmplitudes,
        playbackTotalDuration = audioPlaybackLength,
        audioFilePath = audioFilePath,
        playbackCurrentDuration = currentPlaybackDuration,
        playbackState = playbackState
    )
}