package com.holparb.echojournal.echoes.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holparb.echojournal.echoes.domain.data_source.EchoDataSource
import com.holparb.echojournal.echoes.domain.data_source.Mood
import com.holparb.echojournal.echoes.domain.settings.SettingsPreferences
import com.holparb.echojournal.echoes.presentation.models.MoodUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val echoDataSource: EchoDataSource
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SettingsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeSettings()
                observeTopicsSearchResults()
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
            SettingsAction.OnAddButtonClick -> onAddButtonClick()
            is SettingsAction.OnSelectTopic -> onSelectTopic(action.topic)
            SettingsAction.OnDismissTopicDropdown -> onDismissTopicDropdown()
            is SettingsAction.OnSelectMood -> onSelectMood(action.mood)
            is SettingsAction.OnRemoveTopicClick -> onRemoveTopic(action.topic)
            is SettingsAction.OnSearchTextChange -> onSearchTextChange(action.text)
            else -> Unit
        }
    }

    private fun onDismissTopicDropdown() {
        _state.update {
            it.copy(
                isTopicSuggestionsVisible = false
            )
        }
    }

    private fun onAddButtonClick() {
        _state.update {
            it.copy(
                isTopicTextInputVisible = !it.isTopicTextInputVisible
            )
        }
    }

    private fun onSelectTopic(topic: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isTopicTextInputVisible = false,
                    isTopicSuggestionsVisible = false,
                    searchText = ""
                )
            }
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

    @OptIn(FlowPreview::class)
    private fun observeTopicsSearchResults() {
        state
            .distinctUntilChangedBy { it.searchText }
            .map { it.searchText }
            .debounce(300)
            .flatMapLatest { query ->
                if(query.isNotBlank()) {
                    echoDataSource.searchTopics(query)
                } else emptyFlow()
            }
            .onEach { filteredResults ->
                _state.update {
                    val filteredNonDefaultResults = filteredResults - it.topics
                    val searchText = it.searchText.trim()
                    val isNewTopic = searchText !in filteredNonDefaultResults
                            && searchText !in it.topics && searchText.isNotBlank()
                    it.copy(
                        suggestedTopics = filteredNonDefaultResults,
                        isTopicSuggestionsVisible = filteredResults.isNotEmpty() || isNewTopic,
                        showCreateTopicOption = isNewTopic
                    )
                }

            }
            .launchIn(viewModelScope)
    }
}