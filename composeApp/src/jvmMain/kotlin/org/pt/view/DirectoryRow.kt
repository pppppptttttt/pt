package org.pt.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import org.pt.model.DirectoryNode

@Composable
fun DirectoryRow(
    node: DirectoryNode,
    depth: Int,
    expanded: Boolean,
    toggleState: ToggleableState,
    onToggleExpanded: () -> Unit,
    onToggleSelection: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width((depth * 16).dp))

        Text(
            text = if (expanded) "▼" else "▶",
            modifier = Modifier
                .width(20.dp)
                .clickable(onClick = onToggleExpanded)
        )

        TriStateCheckbox(
            state = toggleState,
            onClick = onToggleSelection
        )

        Text(
            text = node.name,
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable(onClick = onToggleExpanded)
        )
    }
}
