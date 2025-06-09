package com.holparb.echojournal.echoes.data.data_source

import com.holparb.echojournal.core.database.echo.EchoEntity
import com.holparb.echojournal.core.database.echo_topic_relation.EchoWithTopics
import com.holparb.echojournal.core.database.topic.TopicEntity
import com.holparb.echojournal.echoes.domain.data_source.Echo
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds

fun EchoWithTopics.toEcho(): Echo {
    return Echo(
        id = echo.echoId,
        mood = echo.mood,
        title = echo.title,
        note = echo.note,
        audioFilePath = echo.audioFilePath,
        audioAmplitudes = echo.audioAmplitudes,
        audioPlaybackLength = echo.audioPlaybackLength.milliseconds,
        recordedAt = Instant.ofEpochMilli(echo.recordedAt),
        topics = topics.map { it.topic }
    )
}

fun Echo.toEchoWithTopics(): EchoWithTopics {
    return EchoWithTopics(
        echo = EchoEntity(
            echoId = id ?: 0,
            title = title,
            note = note,
            mood = mood,
            audioAmplitudes = audioAmplitudes,
            audioFilePath = audioFilePath,
            audioPlaybackLength = audioPlaybackLength.inWholeMilliseconds,
            recordedAt = recordedAt.toEpochMilli()
        ),
        topics = topics.map { TopicEntity(it) }
    )
}