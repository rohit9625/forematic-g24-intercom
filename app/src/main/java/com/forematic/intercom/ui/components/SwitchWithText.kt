package com.forematic.intercom.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun SwitchWithText(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textOnOFF: Pair<String, String> = Pair("ON", "OFF")
) {
    val switchWidth = 64.dp
    val switchHeight = 32.dp
    val thumbSize = 24.dp

    val backgroundColor = if (isChecked) Color(0xFF4CAF50) else Color.Red // Green or Gray
    val textColor = Color.White

    val thumbOffset by animateDpAsState(
        targetValue = if (isChecked) switchWidth - thumbSize - 6.dp else 6.dp,
        label = "Thumb Offset Animation"
    )

    Box(
        modifier = modifier
            .size(width = switchWidth, height = switchHeight)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable { onCheckedChange(!isChecked) }
    ) {
        Text(
            text = if (isChecked) textOnOFF.first else textOnOFF.second,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .align(if (isChecked) Alignment.CenterStart else Alignment.CenterEnd)
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(thumbSize)
                .offset {
                    IntOffset(x = thumbOffset.roundToPx(), y = 0)
                }
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}