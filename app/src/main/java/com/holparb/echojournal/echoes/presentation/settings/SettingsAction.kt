package com.holparb.echojournal.echoes.presentation.settings

import com.holparb.echojournal.echoes.presentation.models.MoodUi

sealed interface SettingsAction {
    data class OnSearchTextChange(val text: String): SettingsAction
    data class OnMoodSelected(val mood: MoodUi?): SettingsAction
    data object OnCreateTopicClick: SettingsAction
    data class OnRemoveTopicClick(val topic: String): SettingsAction
    data object OnBackClick: SettingsAction
    data object OnDismissTopicDropdown: SettingsAction
    data object OnAddButtonClick: SettingsAction
}