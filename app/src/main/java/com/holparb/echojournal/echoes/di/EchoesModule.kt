package com.holparb.echojournal.echoes.di

import com.holparb.echojournal.echoes.data.recording.AndroidVoiceRecorder
import com.holparb.echojournal.echoes.domain.recording.VoiceRecorder
import com.holparb.echojournal.echoes.presentation.echo_list.EchoListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val echoesModule = module {
    single {
        AndroidVoiceRecorder(
            context = androidApplication(),
            applicationScope = get()
        )
    } bind  VoiceRecorder::class

    viewModelOf(::EchoListViewModel)
}