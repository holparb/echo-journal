package com.holparb.echojournal.echoes.presentation.echoes_list

import com.holparb.echojournal.R
import com.holparb.echojournal.core.presentation.designsystem.dropdowns.Selectable
import com.holparb.echojournal.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import com.holparb.echojournal.core.presentation.util.UiText
import com.holparb.echojournal.echoes.presentation.echoes_list.models.EchoFilterChip
import com.holparb.echojournal.echoes.presentation.models.MoodChipContent
import com.holparb.echojournal.echoes.presentation.models.MoodUi

data class EchoesListState(
    val hasEchoesRecorded: Boolean = false,
    val hasActiveTopicFilters: Boolean = false,
    val hasActiveMoodFilters: Boolean = false,
    val isLoadingData: Boolean = false,
    val moods: List<Selectable<MoodUi>> = emptyList(),
    val topics: List<Selectable<String>> = listOf("Love", "Happy", "Work").asUnselectedItems(),
    val moodChipContent: MoodChipContent = MoodChipContent(),
    val selectedEchoFilterChip: EchoFilterChip? = null,
    val topicChipTitle: UiText = UiText.StringResource(R.string.all_topics)
)