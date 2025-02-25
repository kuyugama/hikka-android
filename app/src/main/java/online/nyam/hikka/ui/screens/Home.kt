package online.nyam.hikka.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.launch
import online.nyam.hikka.api.API
import online.nyam.hikka.api.models.Response
import online.nyam.hikka.api.models.responses.Manga
import online.nyam.hikka.api.paging.MangaPagingSource
import online.nyam.hikka.ui.components.MangaCard
import online.nyam.hikka.ui.components.MangaDetailsModal
import online.nyam.hikka.ui.lists.VerticalPagedGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    var showManga by remember { mutableStateOf<Manga?>(null) }

    val pager =
        remember {
            Pager(PagingConfig(15)) {
                MangaPagingSource()
            }
        }

    if (showManga != null) {
        MangaDetailsModal(manga = showManga!!) {
            showManga = null
        }
    }

    VerticalPagedGrid(
        pager,
        { it.slug },
        modifier,
        {
            Text(
                "Loading list...",
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    ) {
        MangaCard(manga = it, onShowDetails = {
            scope.launch {
                when (val response = API.mangaDetails(it.slug)) {
                    is Response.Success -> showManga = response.data
                    is Response.Error -> {}
                }
            }
        })
    }
}
