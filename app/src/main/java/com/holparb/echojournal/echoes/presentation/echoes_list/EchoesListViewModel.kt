package com.holparb.echojournal.echoes.presentation.echoes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class EchoesListViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(EchoesListState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EchoesListState()
        )

    fun onAction(action: EchoesListAction) {
        when (action) {
            EchoesListAction.OnFabClick -> {}
            EchoesListAction.OnFabLongClick -> {}
            EchoesListAction.OnMoodChipClick -> {}
            is EchoesListAction.OnRemoveFilters -> {}
            EchoesListAction.OnSettingsClick -> {}
            EchoesListAction.OnTopicChipClick -> {}
        }
    }

}