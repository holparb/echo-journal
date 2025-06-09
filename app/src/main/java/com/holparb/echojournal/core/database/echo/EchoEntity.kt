package com.holparb.echojournal.core.database.echo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.holparb.echojournal.echoes.presentation.models.MoodUi

@Entity(tableName = "echoes")
data class EchoEntity(
    @PrimaryKey(autoGenerate = true)
    val echoId: Int = 0,
    val title: String,
    val mood: MoodUi,
    val recordedAt: Long,
    val note: String?,
    val audioFilePath: String,
    val audioPlaybackLength: Long,
    val audioAmplitudes: List<Float>
)
