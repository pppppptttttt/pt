package org.pt.view

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import io.github.vinceglb.filekit.utils.toFile
import org.pt.model.DirectoryReader
import org.pt.theme.PtTheme

@Composable
@Preview
fun App() = PtTheme { ScreenFlow() }

@Composable
private fun ScreenFlow() {
    var screen: AppScreen by remember { mutableStateOf(AppScreen.DirectoryPicker) }
    var state by remember { mutableStateOf(DirectoryPickerScreenState()) }

    val onDirectorySelected = { newState: DirectoryPickerScreenState ->
        state = newState
        screen = AppScreen.Main(state.path)
    }

    val onBack = { screen = AppScreen.DirectoryPicker }

    when (val current = screen) {
        is AppScreen.DirectoryPicker -> {
            DirectoryPickerScreen(
                onDirectorySelected = onDirectorySelected
            )
        }

        is AppScreen.Main -> {
            val loadedData = remember(current.path) {
                loadData(state)
            }

            if (loadedData.reader != null) {
                FileSelectionScreen(
                    selectedFiles = loadedData.reader.files,
                    currentPath = current.path,
                    onBack = onBack
                )
            } else {
                DirectoryPickerScreen(
                    errorMessage = """
                        Failed to open directory: ${current.path}.
                        Reason: ${loadedData.errorMessage}.
                    """.trimIndent(),
                    onDirectorySelected = onDirectorySelected
                )
            }
        }
    }
}

private data class LoadDataResult(val reader: DirectoryReader?, val errorMessage: String)

private fun loadData(directoryPickerScreenState: DirectoryPickerScreenState): LoadDataResult {
    directoryPickerScreenState.apply {
        try {
            val reader = DirectoryReader(
                baseDirectory = path,
                ignoreConfiguration = ignoreConfiguration
            ).apply {
                readDirectory(
                    prefixFunc = { "<file=${it.relativeTo(path.toFile()).path}>\n" },
                    postfixFunc = { "</file>\n" },
                    selectBinaries = selectBinaries,
                    binaryFunc = { replaceBinariesWith ?: it.readText() },
                )

            }
            return LoadDataResult(reader, "")
        } catch (e: Throwable) {
            e.printStackTrace()
            return LoadDataResult(null, e.cause.toString())
        }
    }
}