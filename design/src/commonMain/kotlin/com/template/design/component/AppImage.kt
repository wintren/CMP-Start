package com.template.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.SubcomposeAsyncImage
import com.template.design.preview.AppPreview
import com.template.design.theme.AppTheme

/**
 * Needs the singleton loader `installAppImageLoader()` sets up — Coil ships no network
 * fetcher on iOS or wasmJs, so without it every load fails silently.
 */
@Composable
fun AppImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = AppTheme.shapes.medium,
    contentScale: ContentScale = ContentScale.Crop,
) = SubcomposeAsyncImage(
    model = url,
    contentDescription = contentDescription,
    modifier = modifier.clip(shape),
    contentScale = contentScale,
    loading = { ImagePlaceholder() },
    error = { ImagePlaceholder() },
)

@Composable
private fun ImagePlaceholder() = Box(
    modifier = Modifier.fillMaxSize().background(AppTheme.colors.surfaceRaised),
)

/** Previews have no network, so both render as the placeholder; the desktop catalog loads. */
@Composable
fun AppImageShowcase() {
    AppImage(
        url = "https://picsum.photos/seed/cmpstart/600/300",
        contentDescription = "Example remote image",
        modifier = Modifier.fillMaxWidth().height(WIDE_IMAGE_HEIGHT),
    )
    AppImage(
        url = "https://example.invalid/missing.png",
        contentDescription = null,
        modifier = Modifier.size(AVATAR_SIZE),
        shape = AppTheme.shapes.pill,
    )
}

private val WIDE_IMAGE_HEIGHT = 140.dp
private val AVATAR_SIZE = 64.dp

@Preview
@Composable
private fun AppImageLightPreview() = AppPreview { AppImageShowcase() }

@Preview
@Composable
private fun AppImageDarkPreview() = AppPreview(isDark = true) { AppImageShowcase() }
