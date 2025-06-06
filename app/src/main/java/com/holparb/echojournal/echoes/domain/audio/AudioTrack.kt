package com.holparb.echojournal.echoes.domain.audio

import kotlin.time.Duration

data class AudioTrack(
    val totalDuration: Duration,
    val durationPlayed: Duration,
    val isPlaying: Boolean
)
