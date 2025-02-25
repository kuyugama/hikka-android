@file:OptIn(ExperimentalMaterial3Api::class)

package online.nyam.hikka.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.asImage
import online.nyam.hikka.R
import online.nyam.hikka.api.models.responses.Manga
import online.nyam.hikka.api.models.responses.MangaShort
import online.nyam.hikka.ui.MDDocument
import org.commonmark.node.Document
import org.commonmark.parser.Parser

@Composable
fun MangaDetailsModal(
    manga: Manga,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier =
                Modifier
                    .verticalScroll(
                        rememberScrollState()
                    ).padding(16.dp, 8.dp)
        ) {
            UrlImage(
                manga.image,
                "Manga image",
                Modifier
                    .size(236.dp, 330.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                "${manga.title} (${manga.year})",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                manga.genres.forEach {
                    AssistChip(
                        {},
                        {
                            Text(it.name, Modifier.padding(vertical = 2.dp))
                        },
                        border = null,
                        shape = MaterialTheme.shapes.large,
                        colors =
                            AssistChipDefaults.assistChipColors().copy(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                labelColor = MaterialTheme.colorScheme.primary
                            )
                    )
                }
            }
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.tertiary
            ) {
                val root =
                    remember {
                        Parser.builder().build().parse(manga.synopsis) as Document
                    }
                MDDocument(
                    root
                )
            }
        }
    }
}

@Composable
fun MangaCard(
    manga: MangaShort,
    modifier: Modifier = Modifier,
    onOpen: (() -> Unit)? = null,
    onShowDetails: (() -> Unit)? = null
) {
    val context = LocalContext.current

    Surface(
        modifier =
            modifier
                .padding(4.dp)
                .pointerInput(manga.slug) {
                    detectTapGestures(
                        onLongPress = { onShowDetails?.invoke() },
                        onTap = { onOpen?.invoke() }
                    )
                },
        shape = MaterialTheme.shapes.large,
        tonalElevation = 16.dp,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {
            UrlImage(
                url = manga.image,
                contentDescription = "Manga image",
                modifier = Modifier.size(126.dp, 180.dp),
                placeholder = context.resources.getDrawable(R.drawable.kuyugama, context.theme).asImage()
            )
            Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    manga.title,
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    minLines = 2
                )
                Text(
                    (manga.year?.toString() ?: ""),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
