package org.pt.view

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.pt.model.SelectedFiles

private const val MIN_LEFT_PANEL_WIDTH = 180f

@Composable
fun FileSelectionScreen(
    selectedFiles: SelectedFiles,
    currentPath: kotlinx.io.files.Path,
    onBack: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        var leftPanelWidth by remember { mutableFloatStateOf(320f) }
        val maxAllowed = (maxWidth.value * 0.7f).coerceAtLeast(MIN_LEFT_PANEL_WIDTH)

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Button(onClick = onBack) {
                    Text("Back")
                }

                Text(
                    text = currentPath.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 12.dp, top = 10.dp)
                )
            }

            Row(modifier = Modifier.fillMaxSize()) {
                FilesTreePanel(
                    selectedFiles = selectedFiles,
                    modifier = Modifier
                        .width(leftPanelWidth.dp)
                        .fillMaxHeight()
                )

                ResizeDivider(
                    onResize = { delta ->
                        leftPanelWidth = (leftPanelWidth + delta)
                            .coerceIn(MIN_LEFT_PANEL_WIDTH, maxAllowed)
                    }
                )

                SelectedContentPanel(
                    selectedFiles = selectedFiles,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
            }
        }
    }
}
