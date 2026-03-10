package org.pt.view

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import kotlinx.coroutines.launch
import org.pt.model.SelectedFiles
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

private const val PAGE_SIZE = 20

@Composable
fun SelectedContentPanel(
    selectedFiles: SelectedFiles,
    modifier: Modifier = Modifier
) {
    val entries = selectedFiles.selectedEntries()
    val hasSelectedFiles = entries.isNotEmpty()

    var visibleCount by remember(entries) {
        mutableIntStateOf(PAGE_SIZE.coerceAtMost(entries.size))
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val visibleEntries = entries.take(visibleCount)
    val hasMore = visibleCount < entries.size
    val scrollState = rememberScrollState()

    val scope = rememberCoroutineScope()
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard

    val saveLauncher = rememberFileSaverLauncher(dialogSettings = FileKitDialogSettings()) { file ->
        errorMessage = null

        runCatching {
            require(file != null) { "Save was cancelled" }
            file.file.writeText(selectedFiles.selectedContent())
        }.onFailure { error ->
            if (error.message != "Save was cancelled") {
                errorMessage = error.message ?: "Failed to save file"
            }
        }
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 12.dp, bottom = 40.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            visibleEntries.forEach { FileContentBlock(content = it) }

            if (hasMore) {
                Button(
                    onClick = {
                        visibleCount = (visibleCount + PAGE_SIZE).coerceAtMost(entries.size)
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("Load $PAGE_SIZE more files")
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.align(Alignment.CenterVertically),
            ) {
                Text(
                    text = "$visibleCount / ${entries.size} shown",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    scope.launch {
                        val selection = StringSelection(selectedFiles.selectedContent())
                        clipboard.setContents(selection, selection)
                    }
                },
                enabled = hasSelectedFiles
            ) {
                Text("Copy")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    saveLauncher.launch("pt-export", "txt")
                    errorMessage = null
                },
                enabled = hasSelectedFiles
            ) {
                Text("Save as")
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}
