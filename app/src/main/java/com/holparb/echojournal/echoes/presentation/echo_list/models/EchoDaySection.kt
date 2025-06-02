package com.holparb.echojournal.echoes.presentation.echo_list.models

import com.holparb.echojournal.core.presentation.util.UiText
import com.holparb.echojournal.echoes.presentation.models.EchoListItemUi

data class EchoDaySection(
    val dateHeader: UiText,
    val echoes: List<EchoListItemUi>
)
