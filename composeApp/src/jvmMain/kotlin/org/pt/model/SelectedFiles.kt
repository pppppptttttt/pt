package org.pt.model

import androidx.compose.runtime.mutableStateMapOf

data class SelectedFiles(
    private val files: MutableMap<String, String> = mutableStateMapOf(),
    private val toggled: MutableMap<String, Boolean> = mutableStateMapOf(),
) {
    fun put(filename: String, content: String, selected: Boolean = true) {
        files[filename] = content
        toggled[filename] = selected
    }

    fun setSelected(filename: String, selected: Boolean) {
        if (filename in toggled) {
            toggled[filename] = selected
        }
    }

    fun setSelectedForPrefix(directoryPath: String, selected: Boolean) {
        val prefix = if (directoryPath.endsWith("/")) directoryPath else "$directoryPath/"

        toggled.keys
            .parallelStream()
            .filter { it == directoryPath || it.startsWith(prefix) }
            .forEach { toggled[it] = selected }
    }

    fun isSelected(filename: String): Boolean {
        return toggled[filename] == true
    }

    fun getContent(filename: String): String {
        return files[filename] ?: ""
    }

    fun filenames(): List<String> {
        return files.keys.sorted()
    }

    fun selectedEntries(): List<String> {
        return filenames()
            .filter { isSelected(it) }
            .map { getContent(it) }
    }

    fun selectedContent(): String {
        return filenames()
            .filter { isSelected(it) }
            .joinToString("\n") { getContent(it) }
    }

    fun hasSelectedEntries(): Boolean {
        return selectedEntries().isNotEmpty()
    }
}

