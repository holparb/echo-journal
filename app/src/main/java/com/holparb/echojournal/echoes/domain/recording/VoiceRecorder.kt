package com.holparb.echojournal.echoes.domain.recording

import kotlinx.coroutines.flow.StateFlow

interface VoiceRecorder {
    val recordingDetails: StateFlow<RecordingDetails>
    fun start()
    fun stop()
    fun pause()
    fun resume()
    fun cancel()
}