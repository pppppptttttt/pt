package org.pt.model

import androidx.compose.ui.state.ToggleableState

fun DirectoryNode.allFilePaths(): List<String> {
    return children.flatMap { child ->
        when (child) {
            is FileNode -> listOf(child.path)
            is DirectoryNode -> child.allFilePaths()
        }
    }
}

fun directoryToggleState(
    node: DirectoryNode,
    selectedFiles: SelectedFiles
): ToggleableState {
    val filePaths = node.allFilePaths()

    if (filePaths.isEmpty()) return ToggleableState.Off

    val selectedCount = filePaths.count { selectedFiles.isSelected(it) }

    return when (selectedCount) {
        0 -> ToggleableState.Off
        filePaths.size -> ToggleableState.On
        else -> ToggleableState.Indeterminate
    }
}