package com.holparb.echojournal.echoes.presentation.echoes_list

import com.holparb.echojournal.echoes.presentation.echoes_list.models.EchoFilterChip
import com.holparb.echojournal.echoes.presentation.models.MoodUi

sealed interface EchoesListAction {
    data object OnMoodChipClick: EchoesListAction
    data object OnDismissMoodDropdown: EchoesListAction
    data object OnDismissTopicDropdown: EchoesListAction
    data class OnFilterByMoodClick(val moodUi: MoodUi): EchoesListAction
    data class OnFilterByTopicClick(val topic: String): EchoesListAction
    data object OnTopicChipClick: EchoesListAction
    data class OnRemoveFilters(val filterType: EchoFilterChip): EchoesListAction
    data object OnFabClick: EchoesListAction
    data object OnFabLongClick: EchoesListAction
    data object OnSettingsClick: EchoesListAction
}