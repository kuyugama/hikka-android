package online.nyam.hikka.activities.main.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import online.nyam.hikka.activities.main.viewmodels.MangaScreenViewModel
import online.nyam.hikka.ui.MDDocument
import online.nyam.hikka.ui.components.UrlImage
import org.commonmark.node.Document
import org.commonmark.parser.Parser
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MangaScreen(
    modifier: Modifier = Modifier,
    mangaModel: MangaScreenViewModel = koinViewModel()
) {
    val state by mangaModel.state.collectAsState()

    if (state.manga == null) {
        Column(modifier.fillMaxWidth()) {
            CircularProgressIndicator()
        }
        return
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val manga = state.manga!!

            UrlImage(
                url = manga.image,
                contentDescription = "Manga image",
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(MaterialTheme.shapes.medium)
                        .size(220.dp, 360.dp)
            )

            Text(
                if (manga.year == null) manga.title else "${manga.title} (${manga.year})",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.tertiary) {
                MDDocument(
                    Parser.builder().build().parse(manga.synopsis) as Document
                )
            }
        }
        FloatingActionButton(
            onClick = {
                // TODO: open edit creation screen(add edit creation screen first. Suggested name: MangaEditScreen)
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Create edit")
        }
    }
}
