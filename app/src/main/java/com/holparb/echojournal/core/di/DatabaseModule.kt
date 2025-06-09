package com.holparb.echojournal.core.di

import androidx.room.Room
import com.holparb.echojournal.core.database.EchoDatabase
import com.holparb.echojournal.core.database.echo.EchoDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single<EchoDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            EchoDatabase::class.java,
            "echoes.db"
        ).build()
    }
    single<EchoDao> {
        get<EchoDatabase>().echoDao
    }
}