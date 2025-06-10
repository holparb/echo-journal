package com.holparb.echojournal.echoes.domain.settings

import com.holparb.echojournal.echoes.domain.data_source.Mood
import kotlinx.coroutines.flow.Flow

interface SettingsPreferences {
    suspend fun saveDefaultTopics(topics: List<String>)
    fun observeDefaultTopics(): Flow<List<String>>

    suspend fun saveDefaultMood(mood: Mood)
    fun observeDefaultMood(): Flow<Mood>
}