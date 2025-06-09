package com.holparb.echojournal.echoes.domain.data_source

import java.time.Instant
import kotlin.time.Duration

data class Echo(
    val id: Int? = null,
    val mood: Mood,
    val title: String,
    val topics: List<String>,
    val note: String?,
    val audioFilePath: String,
    val audioPlaybackLength: Duration,
    val audioAmplitudes: List<Float>,
    val recordedAt: Instant
)
