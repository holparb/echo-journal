package com.holparb.echojournal.echoes.presentation.echoes_list

data class EchoesListState(
    val hasEchoesRecorded: Boolean = false,
    val hasActiveTopicFilters: Boolean = false,
    val hasActiveMoodFilters: Boolean = false,
    val isLoadingData: Boolean = false
)