package online.nyam.hikka.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.Image
import coil3.compose.AsyncImage
import coil3.request.ImageRequest

@Composable
fun UrlImage(
    url: String,
    modifier: Modifier = Modifier,
    contentDescription: String = "Web image",
    placeholder: Image? = null,
    fallback: Image? = null,
    contentScale: ContentScale = ContentScale.FillBounds
) {
    AsyncImage(
        model =
            ImageRequest
                .Builder(LocalContext.current)
                .data(url)
                .placeholder(placeholder)
                .error(fallback)
                .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}
