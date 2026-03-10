package org.pt.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FileContentBlock(
    content: String
) {
    SelectionContainer {
        Column {
            Text(text = content)
            HorizontalDivider(modifier = Modifier.padding(top = 12.dp))
        }
    }
}