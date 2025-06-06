package com.holparb.echojournal.echoes.di

import com.holparb.echojournal.echoes.data.audio.AndroidAudioPlayer
import com.holparb.echojournal.echoes.data.recording.AndroidVoiceRecorder
import com.holparb.echojournal.echoes.data.recording.InternalRecordingStorage
import com.holparb.echojournal.echoes.domain.audio.AudioPlayer
import com.holparb.echojournal.echoes.domain.recording.RecordingStorage
import com.holparb.echojournal.echoes.domain.recording.VoiceRecorder
import com.holparb.echojournal.echoes.presentation.create_echo.CreateEchoViewModel
import com.holparb.echojournal.echoes.presentation.echo_list.EchoListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val echoesModule = module {
    singleOf(::AndroidVoiceRecorder) bind  VoiceRecorder::class
    singleOf(::InternalRecordingStorage) bind RecordingStorage::class
    singleOf(::AndroidAudioPlayer) bind AudioPlayer::class

    viewModelOf(::EchoListViewModel)
    viewModelOf(::CreateEchoViewModel)
}