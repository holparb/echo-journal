package com.holparb.echojournal.echoes.presentation.settings.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.holparb.echojournal.R
import com.holparb.echojournal.core.presentation.designsystem.chips.HashtagChip
import com.holparb.echojournal.core.presentation.designsystem.dropdowns.Selectable.Companion.asUnselectedItems
import com.holparb.echojournal.core.presentation.designsystem.dropdowns.SelectableDropDownOptionsMenu
import com.holparb.echojournal.core.presentation.designsystem.dropdowns.SelectableOptionsExtra
import com.holparb.echojournal.core.presentation.designsystem.text_fields.TransparentHintTextField
import com.holparb.echojournal.core.presentation.designsystem.theme.Gray6

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopicSelectorCard(
    topics: List<String>,
    searchText: String,
    topicSuggestions: List<String>,
    showCreateTopicOption: Boolean,
    showSuggestionsDropDown: Boolean,
    canInputText: Boolean,
    onSearchTextChange: (String) -> Unit,
    onAddTopicClick: (String) -> Unit,
    onToggleCanInputText: () -> Unit,
    onRemoveTopicClick: (String) -> Unit,
    onDismissSuggestionsDropDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topicTextFocusRequester = remember {
        FocusRequester()
    }
    var topicSuggestionsDropDownVerticalOffset by remember {
        mutableIntStateOf(0)
    }
    val unselectedSuggestion = remember(topicSuggestions) {
        topicSuggestions.asUnselectedItems()
    }

    LaunchedEffect(canInputText) {
        if(canInputText) {
            topicTextFocusRequester.requestFocus()
        }
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(14.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.my_topics),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = stringResource(R.string.select_default_topics_to_apply_to_all_new_entries),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier.onSizeChanged {
                topicSuggestionsDropDownVerticalOffset = it.height
            }
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                topics.forEach { topic ->
                    HashtagChip(
                        text = topic,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.remove_topic, topic),
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        onRemoveTopicClick(topic)
                                    }
                            )
                        }
                    )
                }
                if(canInputText) {
                    TransparentHintTextField(
                        text = searchText,
                        onValueChange = onSearchTextChange,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .focusRequester(topicTextFocusRequester),
                        hintText = null,
                        maxLines = 1,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.None
                        )
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Gray6)
                            .clickable(onClick = onToggleCanInputText),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_new_entry),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }
            }

            if(showSuggestionsDropDown) {
                SelectableDropDownOptionsMenu(
                    items = unselectedSuggestion,
                    itemDisplayText = { it },
                    onDismiss = onDismissSuggestionsDropDown,
                    key = { it },
                    onItemClick = {
                        onAddTopicClick(it.item)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.hashtag),
                            contentDescription = null
                        )
                    },
                    maxDropdownHeight = (LocalConfiguration.current.screenHeightDp * 0.3).dp,
                    dropDownOffset = IntOffset(
                        x = 0,
                        y = topicSuggestionsDropDownVerticalOffset
                    ),
                    dropDownExtras = if(showCreateTopicOption) {
                        SelectableOptionsExtra(
                            text = searchText,
                            onClick = {
                                onAddTopicClick(searchText)
                            }
                        )
                    } else null
                )
            }
        }
    }
}