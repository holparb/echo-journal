package com.holparb.echojournal.echoes.presentation.echo_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.echojournal.core.presentation.designsystem.theme.EchoJournalTheme
import com.holparb.echojournal.echoes.presentation.echo_list.models.ListItemRelativePosition
import com.holparb.echojournal.echoes.presentation.echo_list.models.TrackSizeInfo
import com.holparb.echojournal.echoes.presentation.models.EchoListItemUi
import com.holparb.echojournal.echoes.presentation.preview.PreviewModels

private val noVerticaLineAboveIconModifier = Modifier.padding(top = 16.dp)
private val noVerticaLineBelowIconModifier = Modifier.height(8.dp)

@Composable
fun EchoListItem(
    echoListItemUi: EchoListItemUi,
    listItemRelativePosition: ListItemRelativePosition,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onTrackSizeAvailable: (TrackSizeInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            if(listItemRelativePosition != ListItemRelativePosition.SINGLE_ENTRY) {
                VerticalDivider(
                    modifier = when(listItemRelativePosition) {
                        ListItemRelativePosition.FIRST -> noVerticaLineAboveIconModifier
                        ListItemRelativePosition.LAST -> noVerticaLineBelowIconModifier
                        ListItemRelativePosition.IN_BETWEEN -> Modifier
                        else -> Modifier
                    }
                )
            }

            Image(
                imageVector = ImageVector.vectorResource(echoListItemUi.mood.iconSet.filled),
                contentDescription = echoListItemUi.mood.title.asString(),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(32.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))

        EchoListItemCard(
            echoListItemUi = echoListItemUi,
            onTrackSizeAvailable = onTrackSizeAvailable,
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Preview
@Composable
private fun EchoListItemPreview() {
    EchoJournalTheme {
        EchoListItem(
            echoListItemUi = PreviewModels.echoListItemUi,
            listItemRelativePosition = ListItemRelativePosition.IN_BETWEEN,
            onPlayClick = {},
            onPauseClick = {},
            onTrackSizeAvailable = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}