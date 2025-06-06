package com.holparb.echojournal.echoes.presentation.models

import com.holparb.echojournal.core.presentation.util.toReadableTime
import java.time.Instant
import kotlin.time.Duration

data class EchoListItemUi (
    val id: Int,
    val title: String,
    val mood: MoodUi,
    val recordedAt: Instant,
    val note: String?,
    val topics: List<String>,
    val amplitudes: List<Float>,
    val playbackTotalDuration: Duration,
    val playbackCurrentDuration: Duration = Duration.ZERO,
    val playbackState: PlaybackState = PlaybackState.STOPPED,
    val audioFilePath: String = ""
) {
    val formattedRecordedAt = recordedAt.toReadableTime()
    val playbackRatio = (playbackCurrentDuration / playbackTotalDuration).toFloat()
}