package com.holparb.echojournal.echoes.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.holparb.echojournal.echoes.presentation.models.MoodUi

@Composable
fun MoodItem(
    mood: MoodUi,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .width(64.dp)
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val iconRes = if(selected){
            mood.iconSet.filled
        } else {
            mood.iconSet.outlined
        }
        Image(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = mood.title.asString(),
            modifier = Modifier.height(40.dp),
            contentScale = ContentScale.FillHeight
        )
        Text(
            text = mood.title.asString(),
            style = MaterialTheme.typography.bodySmall,
            color = if(selected) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.outline
            }
        )
    }
}