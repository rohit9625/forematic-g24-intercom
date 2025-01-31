package com.forematic.intercom.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forematic.intercom.ui.theme.G24IntercomTheme

@Composable
fun EditableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var canEdit by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(canEdit) {
        if(canEdit) focusRequester.requestFocus()
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.focusRequester(focusRequester),
        label = { Text(label) },
        trailingIcon = {
            IconButton(
                onClick = { canEdit = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Change",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        enabled = canEdit,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurface,
            disabledTrailingIconColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview
@Composable
private fun LabeledTextFieldPreview() {
    G24IntercomTheme {
        Surface {
            EditableTextField(
                value = "1234",
                onValueChange = { },
                label = "Programming Password",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}