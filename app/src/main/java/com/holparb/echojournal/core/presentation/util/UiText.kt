package com.holparb.echojournal.core.presentation.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

@Stable
sealed interface UiText {
    data class Dynamic(val value: String): UiText

    @Stable
    data class StringResource(
        @StringRes val id: Int,
        val args: Array<Any> = arrayOf()
    ): UiText

    @Composable
    fun asString(): String {
        return when(this) {
            is Dynamic -> value
            is StringResource -> stringResource(id, *args)
        }
    }
}