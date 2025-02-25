package online.nyam.hikka.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.launch
import online.nyam.hikka.api.API
import online.nyam.hikka.api.models.Response
import online.nyam.hikka.api.models.responses.Manga
import online.nyam.hikka.api.paging.MangaPagingSource
import online.nyam.hikka.ui.components.MangaCard
import online.nyam.hikka.ui.components.MangaDetailsModal
import online.nyam.hikka.ui.components.SearchField
import online.nyam.hikka.ui.lists.VerticalPagedGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    var showManga by remember { mutableStateOf<Manga?>(null) }

    var query by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val pager =
        remember(query) {
            Pager(PagingConfig(15)) {
                MangaPagingSource(query)
            }
        }

    if (showManga != null) {
        MangaDetailsModal(manga = showManga!!) {
            showManga = null
        }
    }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SearchField(onPromptUpdate = {
            query = if (it.length > 2) it else null
        })

        VerticalPagedGrid(
            pager,
            { it.slug },
            loader = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        ) {
            MangaCard(manga = it, onShowDetails = {
                scope.launch {
                    when (val response = API.mangaDetails(it.slug)) {
                        is Response.Success -> showManga = response.data
                        is Response.Error -> {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(response.abort.message)
                        }
                    }
                }
            })
        }
    }
}
