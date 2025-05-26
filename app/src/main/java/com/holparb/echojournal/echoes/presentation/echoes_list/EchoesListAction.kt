package com.holparb.echojournal.echoes.presentation.echoes_list

import com.holparb.echojournal.echoes.presentation.echoes_list.model.EchoFilterChip

sealed interface EchoesListAction {
    data object OnMoodChipClick: EchoesListAction
    data object OnTopicChipClick: EchoesListAction
    data class OnRemoveFilters(val filterType: EchoFilterChip): EchoesListAction
    data object OnFabClick: EchoesListAction
    data object OnFabLongClick: EchoesListAction
    data object OnSettingsClick: EchoesListAction
}