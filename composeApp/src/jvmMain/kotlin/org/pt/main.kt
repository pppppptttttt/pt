package org.pt

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.pt.view.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "pt",
    ) {
        App()
    }
}