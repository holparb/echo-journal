package com.holparb.echojournal.echoes.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.echojournal.echoes.domain.data_source.Mood
import com.holparb.echojournal.echoes.domain.settings.SettingsPreferences
import com.holparb.echojournal.echoes.presentation.models.MoodUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsPreferences: SettingsPreferences
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SettingsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeSettings()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SettingsState()
        )

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnAddButtonClick -> {}
            SettingsAction.OnBackClick -> {}
            is SettingsAction.OnSelectTopic -> onSelectTopic(action.topic)
            SettingsAction.OnDismissTopicDropdown -> {}
            is SettingsAction.OnSelectMood -> onSelectMood(action.mood)
            is SettingsAction.OnRemoveTopicClick -> onRemoveTopic(action.topic)
            is SettingsAction.OnSearchTextChange -> onSearchTextChange(action.text)
        }
    }

    private fun onSelectTopic(topic: String) {
        viewModelScope.launch {
            settingsPreferences.saveDefaultTopics(
                (state.value.topics + topic).distinct()
            )
        }
    }

    private fun onRemoveTopic(topic: String) {
        viewModelScope.launch {
            settingsPreferences.saveDefaultTopics(
                (state.value.topics - topic).distinct()
            )
        }
    }

    private fun onSearchTextChange(text: String) {
        _state.update {
            it.copy(
                searchText = text
            )
        }
    }

    private fun onSelectMood(mood: MoodUi) {
        viewModelScope.launch {
            settingsPreferences.saveDefaultMood(Mood.valueOf(mood.name))
        }
    }

    private fun observeSettings() {
        combine(
            settingsPreferences.observeDefaultTopics(),
            settingsPreferences.observeDefaultMood()
        ) { topics, mood ->
            _state.update {
                it.copy(
                    topics = topics,
                    selectedMood = MoodUi.valueOf(mood.name)
                )
            }
        }.launchIn(viewModelScope)
    }
}