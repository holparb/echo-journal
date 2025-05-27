package com.holparb.echojournal.echoes.presentation.models

import com.holparb.echojournal.R
import com.holparb.echojournal.core.presentation.util.UiText

data class MoodChipContent(
    val iconsRes: List<Int> = emptyList(),
    val title: UiText = UiText.StringResource(R.string.all_moods)
)