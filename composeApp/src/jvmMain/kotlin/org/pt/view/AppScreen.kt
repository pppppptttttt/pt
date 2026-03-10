package org.pt.view

import kotlinx.io.files.Path

sealed interface AppScreen {
    data object DirectoryPicker : AppScreen
    data class Main(val path: Path) : AppScreen
}