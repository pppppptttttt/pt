package org.pt.view

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import org.pt.model.*

@Composable
fun FilesTreePanel(
    selectedFiles: SelectedFiles,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val tree = remember(selectedFiles.filenames()) {
        buildFileTree(selectedFiles.filenames())
    }

    val expanded = remember {
        mutableStateMapOf<String, Boolean>().apply {
            put("", true)
        }
    }

    Box(modifier = modifier.padding(8.dp)) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 12.dp)
        ) {
            renderDirectoryChildren(
                directory = tree,
                depth = 0,
                expanded = expanded,
                selectedFiles = selectedFiles
            )
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(listState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}
private fun LazyListScope.renderDirectoryChildren(
    directory: DirectoryNode,
    depth: Int,
    expanded: MutableMap<String, Boolean>,
    selectedFiles: SelectedFiles
) {
    directory.children.forEach { node ->
        when (node) {
            is DirectoryNode -> {
                item(key = "dir:${node.path}") {
                    val toggleState = directoryToggleState(node, selectedFiles)

                    DirectoryRow(
                        node = node,
                        depth = depth,
                        expanded = expanded[node.path] == true,
                        toggleState = toggleState,
                        onToggleExpanded = {
                            expanded[node.path] = expanded[node.path] != true
                        },
                        onToggleSelection = {
                            val shouldSelect = toggleState != ToggleableState.On
                            selectedFiles.setSelectedForPrefix(node.path, shouldSelect)
                        }
                    )
                }

                if (expanded[node.path] == true) {
                    renderDirectoryChildren(
                        directory = node,
                        depth = depth + 1,
                        expanded = expanded,
                        selectedFiles = selectedFiles
                    )
                }
            }

            is FileNode -> {
                item(key = "file:${node.path}") {
                    FileTreeRow(
                        node = node,
                        depth = depth,
                        checked = selectedFiles.isSelected(node.path),
                        onCheckedChange = { checked ->
                            selectedFiles.setSelected(node.path, checked)
                        }
                    )
                }
            }
        }
    }
}
