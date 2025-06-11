package com.holparb.echojournal.echoes.presentation.settings

import com.holparb.echojournal.echoes.presentation.models.MoodUi

sealed interface SettingsAction {
    data class OnSearchTextChange(val text: String): SettingsAction
    data class OnSelectMood(val mood: MoodUi): SettingsAction
    data class OnSelectTopic(val topic: String): SettingsAction
    data class OnRemoveTopicClick(val topic: String): SettingsAction
    data object OnBackClick: SettingsAction
    data object OnDismissTopicDropdown: SettingsAction
    data object OnAddButtonClick: SettingsAction
}