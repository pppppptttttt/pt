package org.pt.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.toKotlinxIoPath
import io.github.vinceglb.filekit.utils.toPath
import org.pt.model.IgnoreConfiguration

data class DirectoryPickerScreenState(
    val path: kotlinx.io.files.Path = System.getProperty("user.home").toPath(),
    val selectBinaries: Boolean = false,
    val replaceBinariesWith: String? = null,
    val ignoreConfiguration: IgnoreConfiguration = IgnoreConfiguration()
)

@Composable
fun DirectoryPickerScreen(
    errorMessage: String? = null,
    onDirectorySelected: (DirectoryPickerScreenState) -> Unit
) {
    var state by remember {
        mutableStateOf(DirectoryPickerScreenState())
    }

    val launcher = rememberDirectoryPickerLauncher { directory ->
        directory?.let {
            state = state.copy(path = directory.toKotlinxIoPath())
            onDirectorySelected(state)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose a project directory",
            style = MaterialTheme.typography.headlineSmall
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Button(
            onClick = { launcher.launch() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Open folder")
        }

        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.ignoreConfiguration.excludeDotGitIgnore,
                onCheckedChange = {
                    state = state.copy(
                        ignoreConfiguration = state.ignoreConfiguration.copy(excludeDotGitIgnore = it)
                    )
                },
            )

            Text(text = "Exclude .git folder")
        }

        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.ignoreConfiguration.considerGitIgnore,
                onCheckedChange = {
                    state = state.copy(
                        ignoreConfiguration = state.ignoreConfiguration.copy(considerGitIgnore = it)
                    )
                },
            )

            Text(text = "Consider .gitignore")
        }

        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.selectBinaries,
                onCheckedChange = { state = state.copy(selectBinaries = it) },
            )

            Text(text = "Select binary files")
        }

        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Checkbox(
//                checked = state.replaceBinariesWith != null,
//                onCheckedChange = {
//                    state = if (it) state.copy(replaceBinariesWith = "")
//                    else state.copy(replaceBinariesWith = null)
//                },
//            )

            Text(text = "Replace binaries with")

            TextField(
                value = state.replaceBinariesWith ?: "",
                onValueChange = { state = state.copy(replaceBinariesWith = it) }
            )
        }
    }
}

