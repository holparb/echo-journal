package com.holparb.echojournal.echoes.presentation.preview

import com.holparb.echojournal.echoes.presentation.models.EchoListItemUi
import com.holparb.echojournal.echoes.presentation.models.MoodUi
import com.holparb.echojournal.echoes.presentation.models.PlaybackState
import java.time.Instant
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

data object PreviewModels {

    val echoListItemUi = EchoListItemUi(
        id = 0,
        title = "My Entry",
        mood = MoodUi.EXCITED,
        recordedAt = Instant.now(),
        note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus id nunc faucibus, tincidunt ante id, consequat massa. Mauris ultricies eros in dignissim semper. Suspendisse tortor est, facilisis vel diam eu, ultrices cursus ipsum. Donec nunc nisi, tristique nec auctor eu, ullamcorper eu nulla. Sed euismod aliquam sagittis. Proin scelerisque turpis in lacinia porttitor. In vehicula sem vitae odio bibendum sodales. Aenean fermentum congue enim, vitae auctor urna dapibus eget. Duis non sapien auctor, rutrum magna sed, imperdiet elit. Donec aliquam, mi a efficitur semper, nisl metus venenatis quam, eget dapibus neque ligula eget mauris.",
        topics = listOf("Work", "Love", "Home"),
        amplitudes = (1..20).map { Random.nextFloat() },
        playbackTotalDuration = 250.seconds,
        playbackCurrentDuration = 120.seconds,
        playbackState = PlaybackState.PAUSED
    )
}