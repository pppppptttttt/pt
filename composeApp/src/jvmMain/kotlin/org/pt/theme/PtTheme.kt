package org.pt.theme

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Background = Color(0xFF0D1117)
private val SurfaceBase = Color(0xFF161B22)
private val SurfaceRaised = Color(0xFF1C2128)
private val SurfaceHigher = Color(0xFF21262D)

private val TextPrimary = Color(0xFFE6EDF3)
private val TextSecondary = Color(0xFFAFB8C1)

private val Blue = Color(0xFF7AA2F7)
private val BlueStrong = Color(0xFFA5C8FF)
private val Cyan = Color(0xFF79C0FF)
private val Green = Color(0xFF7EE787)
private val Red = Color(0xFFFF7B72)

private val Border = Color(0xFF3B4552)
private val BorderSoft = Color(0xFF2A3440)

private val ScrollbarThumb = Color(0xFF5B6B7E)
private val ScrollbarThumbHover = Color(0xFF7C93AD)

private val PtDarkColorScheme = darkColorScheme(
    primary = Blue,
    onPrimary = Background,

    secondary = Cyan,
    onSecondary = Background,

    tertiary = Green,
    onTertiary = Background,

    background = Background,
    onBackground = TextPrimary,

    surface = SurfaceBase,
    onSurface = TextPrimary,

    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,

    primaryContainer = SurfaceHigher,
    onPrimaryContainer = BlueStrong,

    secondaryContainer = SurfaceHigher,
    onSecondaryContainer = Cyan,

    tertiaryContainer = SurfaceHigher,
    onTertiaryContainer = Green,

    error = Red,
    onError = Background,
    errorContainer = SurfaceHigher,
    onErrorContainer = Red,

    outline = Border,
    outlineVariant = BorderSoft,

    inverseSurface = TextPrimary,
    inverseOnSurface = Background,
    inversePrimary = BlueStrong,

    scrim = Color(0x99000000)
)

@Composable
fun PtTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PtDarkColorScheme
    ) {
        CompositionLocalProvider(
            LocalScrollbarStyle provides defaultScrollbarStyle().copy(
                minimalHeight = 32.dp,
                thickness = 10.dp,
                shape = MaterialTheme.shapes.small,
                hoverDurationMillis = 300,
                unhoverColor = ScrollbarThumb,
                hoverColor = ScrollbarThumbHover
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                content()
            }
        }
    }
}
