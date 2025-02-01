package com.forematic.intercom.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forematic.intercom.data.model.Message

@Composable
fun MessageBubble(message: Message) {
    val backgroundColor = if (message.isSentByIntercom) MaterialTheme.colorScheme.inversePrimary
    else MaterialTheme.colorScheme.tertiaryContainer

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isSentByIntercom) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = message.content,
                fontSize = 16.sp,
                color = contentColorFor(backgroundColor)
            )
        }
    }
}