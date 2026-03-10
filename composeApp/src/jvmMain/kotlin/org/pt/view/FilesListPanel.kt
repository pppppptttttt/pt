package org.pt.view

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.pt.model.SelectedFiles

@Composable
fun FilesListPanel(
    selectedFiles: SelectedFiles,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val filenames = selectedFiles.filenames()

    Box(modifier = modifier.padding(8.dp)) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 12.dp)
        ) {
            items(filenames) { filename ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = selectedFiles.isSelected(filename),
                        onCheckedChange = { checked ->
                            selectedFiles.setSelected(filename, checked)
                        }
                    )

                    Text(
                        text = filename,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(listState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}
